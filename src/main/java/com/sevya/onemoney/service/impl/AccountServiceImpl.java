package com.sevya.onemoney.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.onemoney.dto.AccountDto;
import com.sevya.onemoney.dto.AccountGroupDto;
import com.sevya.onemoney.dto.AccountSummaryDto;
import com.sevya.onemoney.dto.CategoricalExpensesDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.dto.TransactionDto;
import com.sevya.onemoney.dto.ValuesDto;
import com.sevya.onemoney.dto.mapper.AccountDtoMapper;
import com.sevya.onemoney.dto.mapper.TransactionDtoMapper;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.AccountAccountGroupMapper;
import com.sevya.onemoney.model.AccountGroup;
import com.sevya.onemoney.model.AccountType;
import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.model.FinancialInstitution;
import com.sevya.onemoney.repository.AccountAccountGroupMapperRepository;
import com.sevya.onemoney.repository.AccountGroupRepository;
import com.sevya.onemoney.repository.AccountRepository;
import com.sevya.onemoney.repository.AccountTypeRepository;
import com.sevya.onemoney.repository.FinancialInstitutionRepository;
import com.sevya.onemoney.service.AccountService;
import com.sevya.onemoney.service.BudgetService;
import com.sevya.onemoney.service.CategoryService;
import com.sevya.onemoney.service.GoalService;
import com.sevya.onemoney.service.TransactionService;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.OneMoneyConstants;
import com.sevya.onemoney.utility.Purpose;

@Service
@PropertySource(value = { "classpath:constants.properties" })
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountTypeRepository accountTypeRepository;
	
	@Autowired
	private AccountGroupRepository accountGroupRepository;
	
	@Autowired
	private FinancialInstitutionRepository financialInstitutionRepository;
	
	@Autowired
	private AccountAccountGroupMapperRepository accountAccountGroupMapperRepository;

	@Autowired
	private BudgetService budgetService;
	
	@Autowired
	private Environment env;
	
	@Override
	public void saveAccounts(List<Account> accounts){
		accountRepository.save(accounts);
	}
	
	@Override
	public Account getAccountByCode(User user,String accountCode) {
		return accountRepository.getAccountByAccountCode(user.getId(),accountCode);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountGroupDto addAccounts(User user,AccountGroupDto accountGroupDto) throws Exception {
		
		AccountGroup accountGroup = new AccountGroup();
		if(accountGroupDto.getAccountGroupId() == null){
			
			if(accountGroupDto.getInstCode() == null){
				throw new NullPointerException("FinancialInstitution code is mandatory...!!!");
			}
			
			FinancialInstitution financialInstitution = financialInstitutionRepository.getFinancialInstitutionByCode(accountGroupDto.getInstCode());
			
			if(financialInstitution == null){
				throw new NullPointerException("FinancialInstitution doesn't exists...!!!");
			}
			
			if(accountGroupDto.getInstId() == null){
					throw new NullPointerException("FinancialInstitution id is mandatory...!!!");
			}
			
			accountGroup.setName(financialInstitution.getName());
			accountGroup.setInstId(accountGroupDto.getInstId());
			accountGroup.setFinancialInstitution(financialInstitution);
			accountGroup.setUuid(LaunchpadUtility.generateUUIDCode());
			accountGroup = accountGroupRepository.save(accountGroup);
		}else{
			accountGroup = accountGroupRepository.getAccountGroup(user.getId(),accountGroupDto.getAccountGroupId());
			if(accountGroup == null){
				throw new NullPointerException("Group doesn't exits...!!!");
			}
		}
		
		if(accountGroupDto.getAccounts() != null){
			addAccountsToGroup(user,accountGroupDto,accountGroup);
		}else{
			throw new NullPointerException("Accounts list need to be send...!!!");
		}
		
		return new AccountGroupDto(accountGroup.getId());
	}
	
	public void addAccountsToGroup(User user,AccountGroupDto accountGroupDto,AccountGroup accountGroup) throws Exception {
		
		for(AccountDto accountDto : accountGroupDto.getAccounts()){
			
			if(accountDto.getAccountId() == null){
				throw new NullPointerException("AccountId is madatory...!!!");
			}
			
			Account existedAccount = accountRepository.getAccountByAccountCode(user.getId(),accountDto.getAccountId());
			if(existedAccount != null){
				throw new NullPointerException("Your account is already added ...!!!"); 
			}
			
			AccountAccountGroupMapper accountsMapper = accountAccountGroupMapperRepository.getAccountAccountGroupMapperByGroupIdAndAccountCode
																			  (accountGroupDto.getAccountGroupId(),accountDto.getAccountId());
			if(accountsMapper == null){
				Account account = AccountDtoMapper.toAccount(new Account(), accountDto);
				AccountType accountType = accountTypeRepository.getAccountTypeByAccountType(accountDto.getAccountType());
				if(accountType == null){
					throw new NullPointerException("Account Type doesn't exits...!!!");
				}
				account.setAccountType(accountType);
				account = accountRepository.save(account);
				AccountAccountGroupMapper mapper = new AccountAccountGroupMapper();
				mapper.setAccount(account);
				mapper.setAccountGroup(accountGroup);
				mapper.setUuid(LaunchpadUtility.generateUUIDCode());
				accountAccountGroupMapperRepository.save(mapper);
			}
		}
	}
	
	public boolean checkAccountStatus(AccountGroup accountGroup,AccountDto accountDto,Account existedAccount) throws Exception{
		
		boolean flag = false;
		AccountAccountGroupMapper exitedAccountMapper = accountAccountGroupMapperRepository.checkAccountAccountGroupMapperForPreviouslyMapperAccount(accountGroup.getId(),existedAccount.getId());
		
		if(exitedAccountMapper != null){
			exitedAccountMapper.setIsActive(true);
			accountAccountGroupMapperRepository.save(exitedAccountMapper);
			updateExistedAccount(exitedAccountMapper.getAccount(),accountDto);
			flag = true;
		}
		return flag;
	}

	@Override
	public void updateAccounts(RequestDto requestDto) throws Exception {
		
		AccountAccountGroupMapper mapper = accountAccountGroupMapperRepository.getAccountAccountGroupMapperByGroupIdAndAccountCode
				(requestDto.getAccountGroupId(),requestDto.getAccountDto().getAccountId());
		if(mapper != null){
			updateExistedAccount(mapper.getAccount(),requestDto.getAccountDto());
		}else{
			throw new NullPointerException("Your account is not available in this group...!!!");
		}
	}

	public void updateExistedAccount(Account account,AccountDto accountDto) throws Exception{
		Account oldAccount = AccountDtoMapper.toAccount(account,accountDto);
		if(accountDto.getAccountType() != null){
			AccountType accountType = accountTypeRepository.getAccountTypeByAccountType(accountDto.getAccountType());
			if(accountType == null){
				throw new NullPointerException("Account Type doesn't exits...!!!");
			}
			oldAccount.setAccountType(accountType);
		}
		accountRepository.save(oldAccount);
	}
	
	@Override
	public void unlickAccounts(User user,Long groupId,String accountCode) throws Exception {
		
		Long accountId = null;
		List<AccountAccountGroupMapper> modifiedList = new ArrayList<>();
		List<AccountAccountGroupMapper> accountAccountGroupMappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByUserIdAndGroupId(user.getId(),groupId);
		
		for(AccountAccountGroupMapper accountAccountGroupMapper : accountAccountGroupMappers) {
				
			if(accountAccountGroupMapper.getAccount().getAccountCode().equals(accountCode)) {
				accountId = accountAccountGroupMapper.getAccount().getId();
				accountAccountGroupMapper.getAccount().setIsActive(false);
				accountAccountGroupMapper.setIsActive(false);
				if(accountAccountGroupMappers.size() == 1)
					accountAccountGroupMapper.getAccountGroup().setIsActive(false);
				modifiedList.add(accountAccountGroupMapper);
			}
		}
		
		if(accountId != null) {
			accountAccountGroupMapperRepository.save(modifiedList);
			boolean isDeleted = categoryService.deleteCategoryTransactionMappersByUserIdAndAccountId(user.getId(),accountId);
			if(isDeleted)
				transactionService.deleteTransactionsFromCAASServer(user,accountCode);
		} else {
			throw new NullPointerException("Account is not available...!!!");	
		}	
		
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public AccountDto getAccountsByAccountTypeAndPurpose(User user, String accountTypeGroup,String purpose) throws Exception {
		
		List<String> accountTypes = new ArrayList<>();
		
		if(accountTypeGroup.equals(OneMoneyConstants.ACCOUNT_TYPE_GROUP_BANK_ACCOUNTS)){
			accountTypes.add(OneMoneyConstants.ACCOUNT_TYPE_CASH);
			accountTypes.add(OneMoneyConstants.ACCOUNT_TYPE_OVERDRAFT);
		}else if(accountTypeGroup.equals(OneMoneyConstants.ACCOUNT_TYPE_GROUP_CREDIT_CARDS)){
			accountTypes.add(OneMoneyConstants.ACCOUNT_TYPE_CREDIT); 
		}else{
			throw new NullPointerException("Group type doesn't match...!!!");
		}
		
		List<AccountAccountGroupMapper> mappers;
		switch(purpose.toUpperCase()){
			case "P":
				mappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByAccountTypesAndPurpose(user.getId(),accountTypes,Purpose.P);
			break;
			case "B":
				mappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByAccountTypesAndPurpose(user.getId(),accountTypes,Purpose.B);
				break;
			default :
				throw new NullPointerException("AccountType doesn't match...!!!");
		}
		
		Double credit = 0d;
		Double debit = 0d;
		Double totalCredit = 0d;
		Double totalDebit = 0d;
		AccountDto accountDto = new AccountDto();
		List<AccountDto> accountDtos = new ArrayList<>();
		if(!mappers.isEmpty()){
			for(AccountAccountGroupMapper account : mappers) {
				AccountDto accountData = new AccountDto();
				accountData.setAccountId(account.getAccount().getAccountCode());
				accountData.setAccountName(account.getAccount().getAccountName());
				accountData.setPurpose(account.getAccount().getPurpose().toString());
				accountData.setAccountType(account.getAccount().getAccountType().getName());
				accountData.setUpdatedAt(account.getAccount().getUpdatedAt() == null ? "" : DateUtility.DATE_FORMATTER_FOR_SQL_FORMAT.format(account.getAccount().getUpdatedAt()));
				accountData.setInstCode(financialInstitutionRepository.getFinancialInstitutionCodeByAccountId(account.getId()));
				accountData.setStatus(0);
				
				List<CategoryTransactionMapper> categoryTransactionMappers = transactionService.getCategoryTransactionMapperForLast3TransactionsByAccountIdAndUserIdAndMonthAndYear
																						(user.getId(), account.getId(),DateUtility.getCurrentMonth(),DateUtility.getCurrentYear());
				
				Map<String,Object> map = getTransactionsList(categoryTransactionMappers);
				
				List<Object[]> transactionAmounts = categoryService.getTotalIncomingAndTotalOutgoingAmountsForAnAccountOfUser(user.getId(),account.getAccount().getAccountCode(),DateUtility.getCurrentMonth(),DateUtility.getCurrentYear());
			
				for(Object[] object: transactionAmounts){
					credit =  object[0] == null ? 0 : Math.abs((Double) object[0]);
					debit = object[1] == null ? 0 : Math.abs((Double) object[1]);
				}
				
				accountData.setTotalOutgoing(Math.abs(Math.rint(debit)));
				accountData.setTotalIncoming(Math.rint(credit));
				accountData.setTransactions((List<TransactionDto>)map.get(OneMoneyConstants.TRANSACTIONS));
				accountData.setTotalTransactionsCount(transactionService.getCountOfCurrentMonthTransactionsByAccountIdAndUserIdAndMonthAndYear
							  (user.getId(), account.getId(),DateUtility.getCurrentMonth(),DateUtility.getCurrentYear(),account.getAccount().getAccountType().getName(),purpose));
				accountDtos.add(accountData);
				totalCredit = totalCredit + credit;
				totalDebit = totalDebit + debit;
			}
			accountDto.setTotalIncoming(Math.rint(totalCredit));
			accountDto.setTotalOutgoing(Math.abs(Math.rint(totalDebit)));
			accountDto.setAccounts(accountDtos);
		}
		return accountDto;
	}
	
	public Map<String,Object> getTransactionsList(List<CategoryTransactionMapper> categoryTransactionMappers) throws Exception {
		
		Map<String,Object> map = new HashMap<>();
		List<TransactionDto> transactionDtos = new ArrayList<>();
		for(CategoryTransactionMapper mapper : categoryTransactionMappers){
			TransactionDto dto = TransactionDtoMapper.toTransactionDto(mapper.getTransaction());
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setCategoryCode(mapper.getCategory() == null ? null : mapper.getCategory().getUuid());
			categoryDto.setCategoryName(mapper.getCategory() == null ? null : mapper.getCategory().getName());
			dto.setCategory(categoryDto);
			transactionDtos.add(dto);
		}
		map.put(OneMoneyConstants.TRANSACTIONS,transactionDtos);
		return map;
	}
	
	@Override
	public ResponseDto getAccountSummary(User user) throws Exception {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setBusiness(getBusinessAndPersonalAccounts(user,OneMoneyConstants.BUSINESS));
		responseDto.setPersonal(getBusinessAndPersonalAccounts(user,OneMoneyConstants.PERSONAL));
		return responseDto;
	}

	public AccountSummaryDto getBusinessAndPersonalAccounts(User user,String purpose){
		
		Double totalDebit     = 0d;
		Double totalCredit 	  = 0d;
		AccountSummaryDto dto = new AccountSummaryDto();
		
		List<AccountAccountGroupMapper> accountAccountGroupMappers = new ArrayList<>();
		
		if(purpose.equals(OneMoneyConstants.PERSONAL)){
			accountAccountGroupMappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByUserIdAndMonthAndYearAndAccountTypeAndPurpose(
											 user.getId(),OneMoneyConstants.ACCOUNT_TYPE_CASH,
											 OneMoneyConstants.ACCOUNT_TYPE_CREDIT,OneMoneyConstants.ACCOUNT_TYPE_OVERDRAFT, Purpose.P);
		}else if(purpose.equals(OneMoneyConstants.BUSINESS)){
			accountAccountGroupMappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByUserIdAndMonthAndYearAndAccountTypeAndPurpose(
											 user.getId(),OneMoneyConstants.ACCOUNT_TYPE_CASH,
											 OneMoneyConstants.ACCOUNT_TYPE_CREDIT, OneMoneyConstants.ACCOUNT_TYPE_OVERDRAFT, Purpose.B);
		}
		
		List<AccountDto> accounts = new ArrayList<>();
		List<AccountDto> creditCards = new ArrayList<>();
		for(AccountAccountGroupMapper accountAccountGroupMapper : accountAccountGroupMappers) {
			
			if(OneMoneyConstants.ACCOUNT_TYPE_CREDIT.equals(accountAccountGroupMapper.getAccount().getAccountType().getName())){
				AccountDto creditCard = AccountDtoMapper.toAccountDto(new AccountDto(),accountAccountGroupMapper.getAccount());
				creditCard.setInstCode(financialInstitutionRepository.getFinancialInstitutionCodeByAccountId(accountAccountGroupMapper.getAccount().getId()));
				creditCards.add(creditCard);
			}else{
				AccountDto accountData = AccountDtoMapper.toAccountDto(new AccountDto(),accountAccountGroupMapper.getAccount());
			
				if(OneMoneyConstants.ACCOUNT_TYPE_CASH.equals(accountAccountGroupMapper.getAccount().getAccountType().getName())){
					accountData.setMinBalance(accountAccountGroupMapper.getAccount().getMinBalance() == null ? 0 : accountAccountGroupMapper.getAccount().getMinBalance());
					accountData.setHideFromBudget(accountAccountGroupMapper.getAccount().getHideFromBudget());
				}
				
				Map<String,Object> map = getTotalCreditAndDedit(accountData,accountAccountGroupMapper.getAccount(),user);
				totalDebit  = totalDebit  + (Double) map.get(OneMoneyConstants.DEBIT);
				totalCredit = totalCredit + (Double) map.get(OneMoneyConstants.CREDIT);
				accountData = (AccountDto) map.get("accountData");
				accounts.add(accountData);
			}
		}
		
		AccountDto accountData = new AccountDto();
		accountData.setTotalIncoming(Math.rint(totalCredit));
		accountData.setTotalOutgoing(Math.rint(Math.abs(totalDebit)));
		accountData.setAccounts(accounts);
		dto.setAccountData(accountData);
		dto.setCreditCards(creditCards);
		dto.setUncategorizedTransactionsCount(categoryService.getUnCategorizedTransactionsCount(user.getId(),purpose,DateUtility.getCurrentMonth(),DateUtility.getCurrentYear()));
		return dto;
	}

	public Map<String,Object> getTotalCreditAndDedit(AccountDto accountData,Account account,User user){
		
		Map<String,Object> map = new HashMap<>();
		Double totalCredit = 0d;
		Double totalDebit = 0d;
		
		Integer month = DateUtility.getCurrentMonth();
		Integer year  = DateUtility.getCurrentYear();
		
		accountData.setInstCode(financialInstitutionRepository.getFinancialInstitutionCodeByAccountId(account.getId()));
		
		List<Object[]> transactionAmounts = categoryService.getTotalIncomingAndTotalOutgoingAmountsForAnAccountOfUser(
					user.getId(),account.getAccountCode(),month,year);
		
		for(Object[] object: transactionAmounts){
			
			Double totalIncoming = (Double) object[0];
			
			if(totalIncoming != null){
				accountData.setTotalIncoming(Math.rint(totalIncoming));
				totalCredit = totalCredit + totalIncoming;
			}else{
				accountData.setTotalIncoming(0d);
			}
			
			Double totalOutgoing = (Double)object[1];
			
			if(totalOutgoing != null){
				accountData.setTotalOutgoing(Math.abs(totalOutgoing));
				totalDebit = totalDebit + totalOutgoing;
			}else{
				accountData.setTotalOutgoing(0d);
			}
		}
		map.put(OneMoneyConstants.DEBIT,totalDebit);
		map.put(OneMoneyConstants.CREDIT,totalCredit);
		map.put("accountData",accountData);
		return map;
	}
	
	@Override
	public List<AccountGroupDto> getAccountsList(User user) throws Exception {
		
		List<AccountGroupDto> list = new ArrayList<>();
		List<AccountGroup> accountGroups = accountGroupRepository.getAccountGroupsByUserIdAndMonthAndYear(user.getId());
		
		for(AccountGroup group : accountGroups){
			AccountGroupDto dto = new AccountGroupDto();
			dto.setInstId(group.getInstId());
			dto.setAccountGroupId(group.getId());
			dto.setInstCode(group.getFinancialInstitution().getInstCode());
			dto.setAccounts(getAccountsByGroupId(user,group.getId()));
			list.add(dto);
		}
		return list;
	}
	
	@Override
	public List<AccountDto> getAccountsByGroupId(User user,Long groupId) throws Exception {
		
		Double totalIncoming = 0d;
		Double totalOutgoing = 0d;
		List<AccountDto> accountDtos = new ArrayList<>();
		
		List<AccountAccountGroupMapper> accountAccountGroupMappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByGroupIdAndUserId(groupId, user.getId());
		
		for(AccountAccountGroupMapper mapper : accountAccountGroupMappers){
			AccountDto dto = new AccountDto();
			dto.setStatus(0);
			dto.setAccountId(mapper.getAccount().getAccountCode());
			dto.setAccountName(mapper.getAccount().getAccountName());
			dto.setPurpose(mapper.getAccount().getPurpose().toString());
			dto.setUpdatedAt(mapper.getAccount().getUpdatedAt() != null ? DateUtility.DATE_FORMATTER_FOR_SQL_FORMAT.format(mapper.getAccount().getUpdatedAt()) : "");
			dto.setAccountType(mapper.getAccount().getAccountType().getName());
			dto.setMinBalance(mapper.getAccount().getMinBalance() == null ? 0 : mapper.getAccount().getMinBalance());
			dto.setHideFromBudget(mapper.getAccount().getHideFromBudget());
			
			List<Object[]> transactionAmounts = categoryService.getTotalIncomingAndTotalOutgoingAmountsForAnAccountOfUser(
					user.getId(),mapper.getAccount().getAccountCode(),DateUtility.getCurrentMonth(),DateUtility.getCurrentYear());
			for(Object[] object: transactionAmounts) {
				totalIncoming = (Double) object[0] == null ? 0d : (Double) object[0];
				totalOutgoing = (Double) object[1] == null ? 0d : (Double) object[1];
			}
			
			dto.setTotalIncoming(Math.rint(totalIncoming));
			dto.setTotalOutgoing(Math.rint(Math.abs(totalOutgoing)));
			accountDtos.add(dto);
		}
		
		return accountDtos;
	}

	@Override
	public Map<String, Object> dashBoard(User user,List<GoalDto> goalDtos) throws Exception {
		
		Integer month = DateUtility.getCurrentMonth();
		Integer year  = DateUtility.getCurrentYear();
		
		List<String> creditCardTypes = new ArrayList<>();
		creditCardTypes.add(env.getProperty("constans.account_type_credit"));
		
		List<String> bankAccountTypes = new ArrayList<>();
		bankAccountTypes.add(env.getProperty("constans.account_type_cash"));
		bankAccountTypes.add(env.getProperty("constans.account_type_overdraft"));
		
		List<String> investmentAccountTypes = new ArrayList<>();
		investmentAccountTypes.add(env.getProperty("constans.account_type_investment"));
		
		List<String> loanAccountTypes = new ArrayList<>();
		loanAccountTypes.add(env.getProperty("constans.account_type_loan"));
		loanAccountTypes.add(env.getProperty("constans.account_type_mortgage"));
		loanAccountTypes.add(env.getProperty("constans.account_type_investloan"));
		loanAccountTypes.add(env.getProperty("constans.account_type_liability"));
		
		List<String> otherinvestmentAccountTypes = new ArrayList<>();
		otherinvestmentAccountTypes.add(env.getProperty("constans.account_type_asset"));
		otherinvestmentAccountTypes.add(env.getProperty("constans.account_type_superannuation"));
		otherinvestmentAccountTypes.add(env.getProperty("constans.account_type_insurance"));
		otherinvestmentAccountTypes.add(env.getProperty("constans.account_type_effects"));
		otherinvestmentAccountTypes.add(env.getProperty("constans.account_type_property"));
		
		Map<String, Object> dashboardObj = new LinkedHashMap<>();
		
		dashboardObj.put("business",getDashboardDateForBusinessAndPersonal(user,creditCardTypes,bankAccountTypes,investmentAccountTypes,loanAccountTypes,otherinvestmentAccountTypes,OneMoneyConstants.BUSINESS,goalDtos));
		dashboardObj.put("personal",getDashboardDateForBusinessAndPersonal(user,creditCardTypes,bankAccountTypes,investmentAccountTypes,loanAccountTypes,otherinvestmentAccountTypes,OneMoneyConstants.PERSONAL,goalDtos));
		
		List<TransactionDto> transactionDtos = transactionService.userTransactionsByMonthYearAndCount(user.getId(),month,year,3);
		dashboardObj.put("transactions",transactionDtos);
		return dashboardObj;
	}
	
	public List<AccountDto> getDashboardDateForBusinessAndPersonal(User user,List<String> creditCardTypes,List<String> bankAccountTypes,
			List<String> investmentAccountTypes,List<String> loanAccountTypes,List<String> otherinvestmentAccountTypes,String purpose,List<GoalDto> goalDtos) throws Exception {
		
		Purpose purposeType;
		Integer month = DateUtility.getCurrentMonth();
		Integer year  = DateUtility.getCurrentYear();
		
		if(OneMoneyConstants.BUSINESS.equals(purpose)) {
			purposeType = Purpose.B;
		}else {
			purposeType = Purpose.P;
		}
		
		List<AccountDto> dashBoardDate = new ArrayList<>();
		
		List<String> businessBankaccountCodes = accountRepository.getAllAccountIdsByAccountTypesAndPurpose(user.getId(),bankAccountTypes,purpose);
		AccountDto businessBankAccountDto = new AccountDto();
		businessBankAccountDto.setGroupType(env.getProperty("constans.group_type_bankaccounts"));
		businessBankAccountDto.setAccountIds(businessBankaccountCodes == null ? new ArrayList<>() : businessBankaccountCodes);
		businessBankAccountDto.setCount(businessBankaccountCodes == null ? 0 : businessBankaccountCodes.size());
		businessBankAccountDto.setStatus(0);
		dashBoardDate.add(businessBankAccountDto);
		
		List<String> businessCreditCardAccountCodes = accountRepository.getAllAccountIdsByAccountTypesAndPurpose(user.getId(),creditCardTypes,purpose);
		AccountDto businessCreditCardAccountDto = new AccountDto();
		businessCreditCardAccountDto.setGroupType(env.getProperty("constans.group_type_creditcards"));
		businessCreditCardAccountDto.setAccountIds(businessCreditCardAccountCodes == null ? new ArrayList<>() : businessCreditCardAccountCodes);
		businessCreditCardAccountDto.setCount(businessCreditCardAccountCodes == null ? 0 : businessCreditCardAccountCodes.size());
		businessCreditCardAccountDto.setStatus(0);
		dashBoardDate.add(businessCreditCardAccountDto);
		
		List<String> businessInvestmentAccountCodes = accountRepository.getAllAccountIdsByAccountTypesAndPurpose(user.getId(),investmentAccountTypes,purpose);
		AccountDto businessInvestmentAccountDto = new AccountDto();
		businessInvestmentAccountDto.setGroupType(env.getProperty("constans.group_type_investments"));
		businessInvestmentAccountDto.setAccountIds(businessInvestmentAccountCodes == null ? new ArrayList<>() : businessInvestmentAccountCodes);
		businessInvestmentAccountDto.setCount(businessInvestmentAccountCodes == null ? 0 : businessInvestmentAccountCodes.size());
		businessInvestmentAccountDto.setStatus(0);
		dashBoardDate.add(businessInvestmentAccountDto);
		
		List<String> businessLoanAccountCodes = accountRepository.getAllAccountIdsByAccountTypesAndPurpose(user.getId(),loanAccountTypes,purpose);
		AccountDto businessLoanAccountDto = new AccountDto();
		businessLoanAccountDto.setGroupType(env.getProperty("constans.group_type_loans"));
		businessLoanAccountDto.setAccountIds(businessLoanAccountCodes == null ? new ArrayList<>() : businessLoanAccountCodes);
		businessLoanAccountDto.setCount(businessLoanAccountCodes == null ? 0 : businessLoanAccountCodes.size());
		businessLoanAccountDto.setStatus(0);
		dashBoardDate.add(businessLoanAccountDto);
		
		List<String> businessOtherinvestmentAccountCodes = accountRepository.getAllAccountIdsByAccountTypesAndPurpose(user.getId(),otherinvestmentAccountTypes,purpose);
		AccountDto businessOtherinvestmentAccountDto = new AccountDto();
		businessOtherinvestmentAccountDto.setGroupType(env.getProperty("constans.group_type_otherinvestments"));
		businessOtherinvestmentAccountDto.setAccountIds(businessOtherinvestmentAccountCodes == null ? new ArrayList<>() : businessOtherinvestmentAccountCodes);
		businessOtherinvestmentAccountDto.setCount(businessOtherinvestmentAccountCodes == null ? 0 : businessOtherinvestmentAccountCodes.size());
		businessOtherinvestmentAccountDto.setStatus(0);
		dashBoardDate.add(businessOtherinvestmentAccountDto);
		
		List<CategoricalExpensesDto> businessCategoricalExpensesDtos = transactionService.userSpentMainCategoryTransactionsByAccountTypeMonthYearAndCount(user.getId(),purposeType,month,year,3);
		AccountDto businessExpenseAccountDto = new AccountDto();
		businessExpenseAccountDto.setGroupType(env.getProperty("constans.group_type_expense"));
		businessExpenseAccountDto.setStatus(0);
		businessExpenseAccountDto.setCategoricalExpenses(businessCategoricalExpensesDtos);
		dashBoardDate.add(businessExpenseAccountDto);
		
		AccountDto businessBudegetAccountDto = new AccountDto();
		Map<String,Float> businessBudgetInfo = budgetService.getUserBudgetInfoByMonthAndYear(user,purpose,month,year);
		businessBudegetAccountDto.setGroupType(env.getProperty("constans.group_type_budgets"));
		businessBudegetAccountDto.setStatus(0);
		businessBudegetAccountDto.setCount(Math.round(businessBudgetInfo.get("totalBudgets")));
		
		List<ValuesDto> businessBudgetValues = new ArrayList<>();
		
		ValuesDto budgetedAmount = new ValuesDto();
		budgetedAmount.setLabel("Budgeted Amount");
		budgetedAmount.setValue(businessBudgetInfo.get("budgetedAmount"));
		businessBudgetValues.add(budgetedAmount);
		
		ValuesDto budgetSpents = new ValuesDto();
		budgetSpents.setLabel("Spent this month");
		budgetSpents.setValue(businessBudgetInfo.get("budgetSpents"));
		businessBudgetValues.add(budgetSpents);
		businessBudegetAccountDto.setValues(businessBudgetValues);
		dashBoardDate.add(businessBudegetAccountDto);
		
		AccountDto goalAccountDto = new AccountDto();
		goalAccountDto.setGroupType(env.getProperty("constans.group_type_goals"));
		Map<String,Long> goalInfo = goalService.getGoalsData(user, goalDtos, purpose);
		goalAccountDto.setStatus(0);
		goalAccountDto.setCount((int) (goalInfo.get("totalGoals") == null ? 0 : goalInfo.get("totalGoals")));
				
		List<ValuesDto> goalValues = new ArrayList<>();
		ValuesDto savedGoalValuedto = new ValuesDto();
		savedGoalValuedto.setLabel("Total Saved Amount");
		savedGoalValuedto.setValue(goalInfo.get("savedAmount") == null ? 0f : goalInfo.get("savedAmount"));
		
		ValuesDto targetGoalValuedto = new ValuesDto();
		targetGoalValuedto.setLabel("Total Target Amount");
		targetGoalValuedto.setValue(goalInfo.get("targetAmount") == null ? 0f : goalInfo.get("targetAmount"));
		
		goalValues.add(savedGoalValuedto);
		goalValues.add(targetGoalValuedto);
		
		goalAccountDto.setValues(goalValues);
		dashBoardDate.add(goalAccountDto);
		
		return dashBoardDate;
	}
	
	
	@Override
	public Integer getFinancialInstitutionIdByAccountId(Long accountId) {
		return financialInstitutionRepository.getFinancialInstitutionCodeByAccountId(accountId);
	}

	@Override
	public void removeInstitutionByGroupId(User user, Long accountGroupId) throws Exception {
		
		/*List<AccountAccountGroupMapper> accountAccountGroupMappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByUserIdAndGroupId(user.getId(),accountGroupId);
		if(accountAccountGroupMappers.isEmpty()){
			throw new NullPointerException("Institution not available...!!!");
		}
		
		accountAccountGroupMappers.forEach(mapper -> {
			Account account = accountRepository.getAccountByAccountId(mapper.getAccount().getId());
			account.setIsActive(false);
			accountRepository.save(account);
			transactionService.deleteTransactionsByUserIdAndAccountCode(user.getId(),account.getAccountCode());
			categoryService.deleteCategoryTransactionMappersByUserIdAndAccountId(user.getId(),account.getId());
		});
		
		AccountGroup accountGroup = accountGroupRepository.getAccountGroup(user.getId(), accountGroupId);
		if(accountGroup != null){
			accountGroup.setIsActive(false);
			accountGroupRepository.save(accountGroup);
		}
		
		List<AccountAccountGroupMapper> accountAccountGroupMappers2 = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByGroupIdAndUserId(accountGroupId, user.getId());
		if(!accountAccountGroupMappers2.isEmpty()){
			accountAccountGroupMappers2.forEach(mapper -> {
				mapper.setIsActive(false);
				accountAccountGroupMapperRepository.save(mapper);
			});
		}*/
		
		
		List<Account> accounts = new ArrayList<>();
		List<AccountAccountGroupMapper> modifiedList = new ArrayList<>();
		List<AccountAccountGroupMapper> accountAccountGroupMappers = accountAccountGroupMapperRepository.getAccountAccountGroupMappersByUserIdAndGroupId(user.getId(), accountGroupId);

		if (accountAccountGroupMappers.isEmpty()) {
			
			AccountGroup accountGroup = accountGroupRepository.getAccountGroup(user.getId(), accountGroupId);
			if (accountGroup != null) {
				accountGroup.setIsActive(false);
				accountGroupRepository.save(accountGroup);
			}
	
		} else {
			
			for (AccountAccountGroupMapper accountAccountGroupMapper : accountAccountGroupMappers) {

				accounts.add(accountAccountGroupMapper.getAccount());
				accountAccountGroupMapper.getAccount().setIsActive(false);
				accountAccountGroupMapper.setIsActive(false);
				accountAccountGroupMapper.getAccountGroup().setIsActive(false);
				modifiedList.add(accountAccountGroupMapper);

			}
		}
		
		for(Account account : accounts) {
			
			boolean isDeleted = categoryService.deleteCategoryTransactionMappersByUserIdAndAccountId(user.getId(),account.getId());
			if(isDeleted)
				transactionService.deleteTransactionsFromCAASServer(user,account.getAccountCode());
			
		}
	
	}

	@Override
	public Integer checkingAccountsAvailabilityByUserAndPurpose(Long userId, Purpose purpose) {
		return accountRepository.checkingAccountsAvailabilityByUserAndPurpose(userId, purpose.toString());
	}
	
}
