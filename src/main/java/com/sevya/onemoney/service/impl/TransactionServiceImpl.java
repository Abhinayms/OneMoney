package com.sevya.onemoney.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.cloudsearchv2.model.ResourceNotFoundException;
import com.google.gson.Gson;
import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.AccountDto;
import com.sevya.onemoney.dto.AccountTransactionsDto;
import com.sevya.onemoney.dto.CAASDto;
import com.sevya.onemoney.dto.CAASRequestDto;
import com.sevya.onemoney.dto.CAASResponseDto;
import com.sevya.onemoney.dto.CategoricalExpensesDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.PaginationDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.TransactionDto;
import com.sevya.onemoney.dto.TxRequestDto;
import com.sevya.onemoney.dto.mapper.CAASDtoMapper;
import com.sevya.onemoney.dto.mapper.PaginationDtoMapper;
import com.sevya.onemoney.dto.mapper.TransactionDtoMapper;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.model.SplitTransactions;
import com.sevya.onemoney.model.Transaction;
import com.sevya.onemoney.repository.CategoryTransactionMapperRepository;
import com.sevya.onemoney.repository.SplitTransactionsRepository;
import com.sevya.onemoney.repository.TransactionRepository;
import com.sevya.onemoney.service.AccountService;
import com.sevya.onemoney.service.CategoryService;
import com.sevya.onemoney.service.TransactionService;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.OneMoneyConstants;
import com.sevya.onemoney.utility.Purpose;

@Service
@PropertySource(value = { "classpath:constants.properties" })
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private Environment env;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SplitTransactionsRepository splitTransactionsRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private CategoryTransactionMapperRepository categoryTransactionMapperRepository;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void uploadAccountTransactions(User user, RequestDto requestDto) throws Exception {
		
		List<Account> accounts = new ArrayList<>();
		
		List<AccountTransactionsDto> accountTransactionsDtos = requestDto.getAccountTransactions();
		if(accountTransactionsDtos==null || accountTransactionsDtos.isEmpty()) {
			throw new ResourceNotFoundException("AccountTransactions need to send...!!!");
		}
		
		for(AccountTransactionsDto accountTransactionsDto : accountTransactionsDtos) {
			
			String accountCode = accountTransactionsDto.getAccountId();
			
			if(accountCode == null )
				throw new ResourceNotFoundException("Account ID need to send...!!!");
			
			Account account = accountService.getAccountByCode(user,accountCode);
			
			if(account == null )
				throw new ResourceNotFoundException("Account doesn't match...!!!");

			if(accountTransactionsDto.getTransactions() != null) {
				saveTransactions(accountTransactionsDto.getTransactions(), user, accountCode);
			}
			
			if(accountTransactionsDto.getUpdatedAt() != null){
				account.setUpdatedAt(DateUtility.DATE_FORMATTER_FOR_SQL_FORMAT.parse(accountTransactionsDto.getUpdatedAt()));
				accounts.add(account);
			} else {
				throw new NullPointerException("Account sync date is mandatory...!!!");
			}
		}
		
		accountService.saveAccounts(accounts);
		
		List<Transaction> unCategorizedTransactions = transactionRepository.getTransactionsByCategorized(user.getId(),false);
		
		if(unCategorizedTransactions!=null && !unCategorizedTransactions.isEmpty()) {
		
			List<CAASDto> caasDtos = CAASDtoMapper.toCAASDtos(unCategorizedTransactions);
			CAASRequestDto caasRequestDto = new CAASRequestDto();
			caasRequestDto.setData(caasDtos);
			
			String url = "https://sandbox-api.ewise.com/caas/fintech_solutions_in/"+user.getUuid()+"/categorise";
			String token = env.getProperty("constans.CAAS_server_token");
			
			CAASResponseDto caasResponseDto = categorizeTransactions(token, url, caasRequestDto);
			
			if(caasResponseDto.getStatus() == 200 && !caasResponseDto.getPayload().isEmpty()) {
				saveCategoryTransactionMapperAfterGettingReponseFromCass(user, caasResponseDto.getPayload());
			}
		}

	}
	
	public void saveTransactions(List<TransactionDto> transactionDts,User user,String accountCode) throws Exception{
		
		List<Transaction> transactions = new ArrayList<>();
		
		for(TransactionDto transactionDto : transactionDts) {
			
			if(transactionDto.getFingerprint() == null || "".equals(transactionDto.getFingerprint()))
				throw new ResourceNotFoundException("Fingerprint should not be empty or null...!!!");
			
			if(transactionDto.getDescription() == null || "".equals(transactionDto.getDescription()))
				throw new ResourceNotFoundException("description should not be empty or null...!!!");
			
			Transaction existedTransaction = transactionRepository.getTransactionByFingerprint(user.getId(), transactionDto.getFingerprint());
 			if(existedTransaction == null) {
				Transaction transaction = TransactionDtoMapper.toTransaction(accountCode, transactionDto);
				transactions.add(transaction);
			}
		}
		transactionRepository.save(transactions);
	}

	public void saveCategoryTransactionMapperAfterGettingReponseFromCass(User user,List<CAASDto> caastDtos) throws Exception{
		
		
		List<CategoryTransactionMapper> categoryTransactionMappers = new ArrayList<>();
		
		for (CAASDto caasDto : caastDtos) {
		
			String transactionId = caasDto.getTransaction_id();
			String categoryUUid  = caasDto.getCategory_id();
			Boolean txCateriozed = caasDto.getApi_categorised();
			
			Transaction transaction = transactionRepository.getTransactionByFingerprint(user.getId(), transactionId);
			
			Category category = null;
			
			if(txCateriozed && categoryUUid != null) {
				category = categoryService.getCategoryByUuid(categoryUUid);
			}
			
			transaction.setIsCategorized(true);
			CategoryTransactionMapper categoryTransactionMapper = categoryTransactionMapperRepository.getCategoryTransactionMapperByUserIdAndTxFingerPrint(user.getId(), transactionId);
			
			if(categoryTransactionMapper == null) {
				categoryTransactionMapper = new CategoryTransactionMapper();
			}
			
			String accountCode = transaction.getAccountCode();
			Account account = accountService.getAccountByCode(user,accountCode);
			categoryTransactionMapper.setAccount(account);
			categoryTransactionMapper.setTransaction(transaction);
			categoryTransactionMapper.setCategory(category);
			categoryTransactionMappers.add(categoryTransactionMapper);
		}
		
		categoryTransactionMapperRepository.save(categoryTransactionMappers);
		
	}
	
	public CAASResponseDto categorizeTransactions(String token,String url,CAASRequestDto caasRequestDto) throws RestClientException {
		
			try {
				
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authentication-Token",token);
				headers.add("Content-Type","application/json");
				HttpEntity<CAASRequestDto> request = new HttpEntity<>(caasRequestDto,headers);
				ResponseEntity<CAASResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, request, CAASResponseDto.class);
				return  response.getBody();
				
			} catch (HttpStatusCodeException exception) {
				
				 CAASResponseDto responseDto = new Gson().fromJson(exception.getResponseBodyAsString(), CAASResponseDto.class);
				 throw new RestClientException(responseDto.getMessage());
				 
			}
		
	}
	
	
	public CAASResponseDto reCategorizeTransactions(String token, String url, CAASRequestDto caasRequestDto) throws RestClientException {
		
		try {
		
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authentication-Token",token);
			headers.add("Content-Type","application/json");
			HttpEntity<CAASRequestDto> request = new HttpEntity<>(caasRequestDto,headers);
			ResponseEntity<CAASResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, request, CAASResponseDto.class);
			return  response.getBody();
			
		} catch (HttpStatusCodeException exception) {
			
			 CAASResponseDto responseDto = new Gson().fromJson(exception.getResponseBodyAsString(), CAASResponseDto.class);
			 throw new RestClientException(responseDto.getMessage());
			 
		}
	
}
	
	

	@Override
	public TransactionDto recategoriseTransaction(User user,TransactionDto transactionDto) throws Exception {
		
		String token = env.getProperty("constans.CAAS_server_token");
		String url = "https://sandbox-api.ewise.com/caas/fintech_solutions_in/"+user.getUuid()+"/recategorise";
		
		String fingerPrint = transactionDto.getFingerprint();
		CategoryTransactionMapper ctMapper = categoryTransactionMapperRepository.getCategoryTransactionMapperByUserIdAndTxFingerPrint(user.getId(),fingerPrint);
		if(ctMapper == null) {
			throw new ResourceNotFoundException("Fingerprint doesn't match...!!!");
		}
		Category category = categoryService.getCategoryByUuid(transactionDto.getCategoryCode());
		
		if(category == null) {
			throw new ResourceNotFoundException("Category doesn't exists...!!!");
		}
		
		List<CAASDto> caasDtos = CAASDtoMapper.toCAASDtos(ctMapper.getTransaction(),category);
		CAASRequestDto caasRequestDto = new CAASRequestDto();
		caasRequestDto.setData(caasDtos);
		
		reCategorizeTransactions(token, url, caasRequestDto);
		ctMapper.setCategory(category);
		categoryTransactionMapperRepository.save(ctMapper);
		
		return getTransactionDetailsByCategoryTransactionMapper(ctMapper);
		
	}
	
	
	@Override
	public void deleteTransactionsFromCAASServer(User user,String accountCode) throws RestClientException {
		
		String token = env.getProperty("constans.CAAS_server_token");
		String url = "https://sandbox-api.ewise.com/caas/fintech_solutions_in/"+user.getUuid()+"/delete_by";
		
		try {
			
			CAASDto caasDto = CAASDtoMapper.toCAASDto(accountCode);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authentication-Token",token);
			headers.add("Content-Type","application/json");
			String inputString = new Gson().toJson(caasDto);
			HttpEntity<String> request = new HttpEntity<>(inputString,headers);
			restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		
		} catch (HttpStatusCodeException exception) {
			
			 CAASResponseDto responseDto = new Gson().fromJson(exception.getResponseBodyAsString(), CAASResponseDto.class);
			 throw new RestClientException(responseDto.getMessage());
			 
		}
		
	}
	

	@Override
	public PaginationDto getAllTransactions(User user, TxRequestDto txRequestDto) throws Exception {
		
		PaginationDto paginationDto = new PaginationDto();
		
		Map<String,Object> data = new HashMap<>();
		Pageable pagable = PaginationDtoMapper.toPagable(txRequestDto);
		
			
			Page<CategoryTransactionMapper> categoryTransactionMappers;
			List<CategoryTransactionMapper>  categoryTransactionMappersList;
			
			
			if(txRequestDto.getDataType() == OneMoneyConstants.dataType_transactions) {
				
				categoryTransactionMappers = getCategoryTransactionMappers(user, txRequestDto, pagable, OneMoneyConstants.dataType_transactions);
				data.put("transactions",toTransactionDtos(categoryTransactionMappers));
				paginationDto.setData(data);
				paginationDto.setPageNo(categoryTransactionMappers.getNumber());
				paginationDto.setPageSize(categoryTransactionMappers.getSize());
				paginationDto.setTotalPages(categoryTransactionMappers.getTotalPages());
				
			
			} else if(txRequestDto.getDataType() == OneMoneyConstants.dataType_categoricalExpenses) {
			
				categoryTransactionMappersList = getCategoryTransactionMappers(user, txRequestDto, OneMoneyConstants.dataType_categoricalExpenses);
				data.put("categoricalExpenses",calculateCategoicalExpenses(categoryTransactionMappersList));
				paginationDto.setData(data);
			
			} else if(txRequestDto.getDataType() == OneMoneyConstants.dataType_transactionsAndCategoricalExpenses) {
				
				categoryTransactionMappers = getCategoryTransactionMappers(user, txRequestDto, pagable, OneMoneyConstants.dataType_transactions);
				data.put("transactions",toTransactionDtos(categoryTransactionMappers));
				paginationDto.setPageNo(categoryTransactionMappers.getNumber());
				paginationDto.setPageSize(categoryTransactionMappers.getSize());
				paginationDto.setTotalPages(categoryTransactionMappers.getTotalPages());
				
				
				categoryTransactionMappersList = getCategoryTransactionMappers(user, txRequestDto, OneMoneyConstants.dataType_categoricalExpenses);
				data.put("categoricalExpenses",calculateCategoicalExpenses(categoryTransactionMappersList));
				paginationDto.setData(data);
				
			} else if(txRequestDto.getDataType() == OneMoneyConstants.dataType_unCategoryTransactions) {
				
				categoryTransactionMappers = getCategoryTransactionMappers(user, txRequestDto, pagable, OneMoneyConstants.dataType_unCategoryTransactions);
				data.put("transactions",toTransactionDtos(categoryTransactionMappers));
				paginationDto.setData(data);
				paginationDto.setPageNo(categoryTransactionMappers.getNumber());
				paginationDto.setPageSize(categoryTransactionMappers.getSize());
				paginationDto.setTotalPages(categoryTransactionMappers.getTotalPages());
			
			} else {
				throw new ResourceNotFoundException("Enter valid dataType...!!!");
			}
			
			return paginationDto;
	}

	public Page<CategoryTransactionMapper> getCategoryTransactionMappers(User user, TxRequestDto txRequestDto, Pageable pagable, int dataType) {
		Specification<CategoryTransactionMapper> specObj = specifications(user, txRequestDto, dataType);
		return categoryTransactionMapperRepository.findAll(specObj,pagable);
	}
	
	
	public List<CategoryTransactionMapper> getCategoryTransactionMappers(User user, TxRequestDto txRequestDto, int dataType) {
		Specification<CategoryTransactionMapper> specObj = specifications(user, txRequestDto, dataType);
		return categoryTransactionMapperRepository.findAll(specObj);
	}
	
	
	public Specification<CategoryTransactionMapper> specifications(User user, TxRequestDto txRequestDto, int dataType){
		
			return new Specification<CategoryTransactionMapper>() {
			@Override
			public Predicate toPredicate(Root<CategoryTransactionMapper> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
					
				 	List<Predicate> predicates = new ArrayList<>();
				 	
				 	Join<CategoryTransactionMapper, Transaction> tJoin= root.join("transaction", JoinType.LEFT);
				 	Join<CategoryTransactionMapper, Category> cJoin= root.join("category", JoinType.LEFT);
				 	Join<CategoryTransactionMapper, Account> aJoin= root.join("account", JoinType.LEFT);
				 	Join<CategoryTransactionMapper, User> uJoin= root.join("createdBy", JoinType.LEFT);
				 	
				 	predicates.add(builder.equal(uJoin.get("id"),user.getId()));
				 	predicates.add(builder.equal(root.get("isActive"),true));
				 	predicates.add(builder.equal(aJoin.get("isActive"),true));
				 	predicates.add(builder.equal(tJoin.get("isActive"),true));
				 	query.orderBy(builder.desc(tJoin.get("transactionDate")));
				 	
				 	
				 	if(dataType == OneMoneyConstants.dataType_categoricalExpenses) {
				 	
				 		predicates.add(builder.isNotNull(cJoin));
					 	predicates.add(builder.lessThan(tJoin.get(OneMoneyConstants.transaction_amount),0));
				 	
				 	} else if(dataType == OneMoneyConstants.dataType_unCategoryTransactions) {
				 		
				 		predicates.add(builder.isNull(cJoin));
				 		
				 	}
				 	
					if (txRequestDto.getAccounts() != null && !txRequestDto.getAccounts().isEmpty()) {
		                predicates.add(builder.isTrue(aJoin.get("accountCode").in(txRequestDto.getAccounts())));
					}
					if (txRequestDto.getCategories() != null && !txRequestDto.getCategories().isEmpty()) {
						predicates.add(builder.isTrue(cJoin.get("uuid").in(txRequestDto.getCategories())));
					}
					if (txRequestDto.getMinAmount()!=null){
						Predicate minAmount = builder.greaterThanOrEqualTo(tJoin.get(OneMoneyConstants.transaction_amount), txRequestDto.getMinAmount());
						predicates.add(minAmount);
					}
					if( txRequestDto.getMaxAmount()!=null) {
						Predicate maxAmount = builder.lessThanOrEqualTo(tJoin.get(OneMoneyConstants.transaction_amount),txRequestDto.getMaxAmount());
						predicates.add(maxAmount);
					}
					if(txRequestDto.getTransactionType()!=null) {
						
						if(OneMoneyConstants.transaction_type_debit.equals(txRequestDto.getTransactionType())){
							Predicate amount = builder.lessThan(tJoin.get(OneMoneyConstants.transaction_amount),0);
							predicates.add(amount);
						}
						
						if(OneMoneyConstants.transaction_type_credit.equals(txRequestDto.getTransactionType())){
							Predicate amount = builder.greaterThan(tJoin.get(OneMoneyConstants.transaction_amount),0);
							predicates.add(amount);
						}
					}
	                if (txRequestDto.getStartDate()!= null) {
	                	
	                	try {
	                		
							Date date = DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(txRequestDto.getStartDate());
							Predicate startDate = builder.greaterThanOrEqualTo(tJoin.get("transactionDate"),date);
							predicates.add(startDate);
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
	                	
	                }
	                if (txRequestDto.getEndDate() != null) {
	                	
	                	try {
	                		
						Date date = DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(txRequestDto.getEndDate());

						Date toDateObj = DateUtility.getEndDate(date);

						Predicate endDate = builder.lessThanOrEqualTo(tJoin.get("transactionDate"), toDateObj);
						predicates.add(endDate);
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
	                
	                }
	                if (txRequestDto.getSearchStr() != null) {
	                	Predicate searchString = builder.like(tJoin.get("description"),"%" + txRequestDto.getSearchStr() + "%");
						predicates.add(searchString);
	                }
	                return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	
	
	public List<TransactionDto> toTransactionDtos(Page<CategoryTransactionMapper> categoryTransactionMappers) throws Exception {
		
		List<TransactionDto> transactionDtos = new ArrayList<>();
		
		for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMappers.getContent()) {
			
			Transaction transaction = categoryTransactionMapper.getTransaction();
			TransactionDto transactionDto = TransactionDtoMapper.toTransactionDto(transaction);
			Category category = categoryTransactionMapper.getCategory();
			
			if(category != null) {
				CategoryDto categoryDto = new CategoryDto();
				categoryDto.setCategoryCode(category.getUuid());
				categoryDto.setCategoryName(category.getName());
				transactionDto.setCategory(categoryDto);
			}
			
			Account account = categoryTransactionMapper.getAccount();
			AccountDto accountDto = new AccountDto();
			accountDto.setInstCode(accountService.getFinancialInstitutionIdByAccountId(account.getId()));
			accountDto.setAccountName(account.getAccountName());
			accountDto.setAccountId(account.getAccountCode());
			transactionDto.setAccount(accountDto);
			transactionDtos.add(transactionDto);
		}
		return transactionDtos;
	}
	
	
	public List<CategoricalExpensesDto> calculateCategoicalExpenses(List<CategoryTransactionMapper> categoryTransactionMapperList) {
		
		Map<Long,List<Transaction>> categoryObj = new LinkedHashMap<>();
		List<CategoricalExpensesDto> categoricalExpensesDtos = new ArrayList<>();
		
		for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMapperList) {
			
			List<Transaction> obj = new LinkedList<>();
			Transaction transaction = categoryTransactionMapper.getTransaction();
			Category category = categoryTransactionMapper.getCategory();
			
			if(category.getParentId() == null) {
				
				List<Transaction> transactions = categoryObj.get(category.getId());
				if(transactions != null ) {
					transactions.add(transaction);
					categoryObj.put(category.getId(), transactions);
				} else {
					
					obj.add(transaction);
					categoryObj.put(category.getId(),obj);
				}
			
			} else {
				List<Transaction> transactions = categoryObj.get(category.getParentId());
				if(transactions != null ) {
					categoryObj.put(category.getParentId(), transactions);
				} else {
					categoryObj.put(category.getParentId(),obj);
				}
			}
	
		}
		
		for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMapperList) {
			
			Transaction transaction = categoryTransactionMapper.getTransaction();
			Category category = categoryTransactionMapper.getCategory();
			
			if(category.getParentId() != null) {
				
				List<Transaction> transactions = categoryObj.get(category.getParentId());
				if(transactions != null ) {
					transactions.add(transaction);
					categoryObj.put(category.getParentId(), transactions);
				}
			} 
		}
		
		for (Map.Entry<Long, List<Transaction>> entry : categoryObj.entrySet()) {
 			
			Long categoryId = entry.getKey();
			Category category = categoryService.getCategoryById(categoryId);
			CategoricalExpensesDto categoricalExpensesDto = new CategoricalExpensesDto();
			List<Transaction> transactions = entry.getValue();
			Float amount = 0f;
			for(Transaction transaction :transactions){
				amount = amount + transaction.getAmount();
			}
			categoricalExpensesDto.setAmount(amount == null ? 0 : Math.abs(amount));
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setCategoryCode(category.getUuid());
			categoryDto.setCategoryName(category.getName());
			categoricalExpensesDto.setCategory(categoryDto);
			categoricalExpensesDtos.add(categoricalExpensesDto);
		   
		}
		
		return categoricalExpensesDtos;
		
	}
	
	
	
	
	@Override
	public TransactionDto splitTransactions(User user,TransactionDto transactionDto) throws Exception {
		
		CategoryTransactionMapper categoryTransactionMapper = categoryTransactionMapperRepository.getCategoryTransactionMapperByUserIdAndTxFingerPrint(user.getId(),transactionDto.getFingerprint());
		if(categoryTransactionMapper == null){
			throw new NullPointerException("Fingersprint doesn't match...!!!");
		}
		Transaction transaction = categoryTransactionMapper.getTransaction();
		
		/* deleting old splittedTransactions */
		
		List<SplitTransactions> oldSplittedtransactions = new ArrayList<>();
		List<SplitTransactions> oldSplitTransactions = splitTransactionsRepository.getSplitTransactionsByTransactionId(transaction.getId());
		
		if(oldSplitTransactions != null && !oldSplitTransactions.isEmpty()) {
		
			oldSplitTransactions.forEach(oldSplitTransaction -> {
				oldSplitTransaction.setIsActive(false);
				oldSplittedtransactions.add(oldSplitTransaction);
			});
			splitTransactionsRepository.save(oldSplittedtransactions);
			
		}
		
		/* Adding old splittedTransactions */
		
		if(transactionDto.getSplittedTransactions() != null) {
			
			List<SplitTransactions> newSplittedtransactions = new ArrayList<>();
			transaction.setSplitted(true);
			
			transactionDto.getSplittedTransactions().forEach(transactions -> {
				
				Category category = categoryService.getCategoryByUuid(transactions.getCategoryCode());
				if(category == null){
					throw new NullPointerException("Category doesn't match...!!!");
				}
				SplitTransactions splitTransaction = new SplitTransactions();
				splitTransaction.setCategory(category);
				splitTransaction.setTransaction(transaction);
				splitTransaction.setAmount(transactions.getAmount());
				newSplittedtransactions.add(splitTransaction);
			
			});
			
			splitTransactionsRepository.save(newSplittedtransactions);

			Account account = categoryTransactionMapper.getAccount();
			Integer instCode = accountService.getFinancialInstitutionIdByAccountId(account.getId());
			Category category = categoryTransactionMapper.getCategory();
			List<TransactionDto> splitTransactionsDtos = TransactionDtoMapper.toTransactionDtos(newSplittedtransactions);
			
			return TransactionDtoMapper.toTransactionDto(instCode, account, transaction, category, splitTransactionsDtos);
		
		} else {
			throw new NullPointerException("Splitted transactions must be send...!!!");
		}
		
		
		
		
	}
	
	@Override
	public TransactionDto getTransactionDetails(User user, String fingerprint) throws Exception {
		
		CategoryTransactionMapper categoryTransactionMapper = categoryTransactionMapperRepository.getCategoryTransactionMapperByUserIdAndTxFingerPrint(user.getId(),fingerprint);
		
		if(categoryTransactionMapper != null ) {
			return getTransactionDetailsByCategoryTransactionMapper(categoryTransactionMapper);
		} else {
			throw new ResourceNotFoundException("fingerprint doesn't Match...!!!");
		}
		
	}

	
	public TransactionDto getTransactionDetailsByCategoryTransactionMapper(CategoryTransactionMapper categoryTransactionMapper) throws Exception {
		
		Transaction transaction = categoryTransactionMapper.getTransaction();
		Account account = categoryTransactionMapper.getAccount();
		Integer instCode = accountService.getFinancialInstitutionIdByAccountId(account.getId());
		Category category = categoryTransactionMapper.getCategory();
		
		List<TransactionDto> splitTransactionsDtos = new ArrayList<>();
		if(transaction.getSplitted()) {
			List<SplitTransactions> splitTransactions = splitTransactionsRepository.getSplitTransactionsByTransactionId(transaction.getId());
			splitTransactionsDtos = TransactionDtoMapper.toTransactionDtos(splitTransactions);
		}
		return TransactionDtoMapper.toTransactionDto(instCode, account, transaction, category, splitTransactionsDtos);
	
	}

	@Override
	public List<CategoryTransactionMapper> getCategoryTransactionMapperForLast3TransactionsByAccountIdAndUserIdAndMonthAndYear(Long userId, Long accountId, Integer month, Integer year) {
		return categoryTransactionMapperRepository.getCategoryTransactionMapperForLast3TransactionsByAccountIdAndUserIdAndMonthAndYear(userId, accountId, month, year,new PageRequest(0,3));
	}
	
	@Override
	public Integer getCountOfCurrentMonthTransactionsByAccountIdAndUserIdAndMonthAndYear(Long userId, Long accountId, Integer month, Integer year,String accountType,String purpose) {
		return categoryTransactionMapperRepository.getCountOfCurrentMonthTransactionsByAccountIdAndUserIdAndMonthAndYear(userId, accountId, month, year,accountType,purpose);
	}


	@Override
	public List<TransactionDto> userTransactionsByMonthYearAndCount(Long userId, Integer month, Integer year, Integer noOfTransactions) throws Exception {
		
		PageRequest pageRequest = new PageRequest(0,noOfTransactions);	
		List<CategoryTransactionMapper> categoryTransactionMappers = categoryTransactionMapperRepository.getUserTransactionsByMonthYearAndCount(userId, month, year, pageRequest);
		return TransactionDtoMapper.toDashBoardTransactionDtos(categoryTransactionMappers);
	}
	
	@Override
	public List<CategoricalExpensesDto> userSpentMainCategoryTransactionsByAccountTypeMonthYearAndCount(Long userId,Purpose purpose, Integer month, Integer year, Integer noOfTransactions) throws Exception {
		
		List<CategoricalExpensesDto> categoricalExpenses = new ArrayList<>();
		List<CategoryTransactionMapper> categoryTransactionMappers = categoryTransactionMapperRepository.getUserSpentCategoryTransactionsByAccountTypeMonthYear(userId, purpose, month, year);
		
		int size = 0;
		
		for (CategoricalExpensesDto categoricalExpensesDto : calculateCategoicalExpenses(categoryTransactionMappers)) {
 			
			size = size + 1 ;
			categoricalExpenses.add(categoricalExpensesDto);
			if(size == noOfTransactions) {
				break;
			}
		   
		}

		return categoricalExpenses;
		
	}
	
	@Override
	public Set<String> searchSuggestions(User user, TxRequestDto txRequestDto) throws Exception {
		
			Set<String> data = new LinkedHashSet<>();
			
			if(txRequestDto.getSearchStr() == null) {
				throw new NullPointerException("Search parameter should not be empty...!!!");
			} else if(txRequestDto.getSearchStr().length() < 3 ) {
				throw new NullPointerException("Search parameter should be minimum 3 characters...!!!");
			}
			
			List<CategoryTransactionMapper> categoryTransactionMappers = getCategoryTransactionMappers(user, txRequestDto, OneMoneyConstants.dataType_transactions);
		
			for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMappers) {
				Transaction transaction = categoryTransactionMapper.getTransaction();
				
				String description = transaction.getDescription();
				String[] splittedWords = description.split(" ");
				
				for(String splittedWord : splittedWords){
					if(splittedWord.toUpperCase().contains(txRequestDto.getSearchStr().toUpperCase()))
						data.add(splittedWord);
				}
			}
			return data;
	}

	@Override
	public void deleteTransactionsByUserIdAndAccountCode(Long userId,String accountCode) {
		List<Transaction> transactions = transactionRepository.getTransactionByUserIdAndAccountCode(userId, accountCode);
		if(!transactions.isEmpty()){
			transactions.forEach(transaction -> {
				transaction.setIsActive(false);
				transactionRepository.save(transaction);
			});
		}
	}
}
