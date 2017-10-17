package com.sevya.onemoney.dto.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.sevya.onemoney.dto.AccountDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.TransactionDto;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.model.SplitTransactions;
import com.sevya.onemoney.model.Transaction;
import com.sevya.onemoney.utility.DateUtility;

public class TransactionDtoMapper {

	private TransactionDtoMapper() {}
	
	
	public static Transaction toTransaction(String accountId, TransactionDto transactionDto) throws ParseException {
	
		Transaction transaction = new Transaction();
		transaction.setAccountCode(accountId);
		transaction.setFingerprint(transactionDto.getFingerprint());
		transaction.setDescription(transactionDto.getDescription());
		transaction.setCurrency(transactionDto.getCurrency());
		transaction.setAmount(transactionDto.getAmount());
		transaction.setTransactionDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(transactionDto.getDate()));
		return transaction;
	}
	
	public static TransactionDto toTransactionDto(Transaction transaction) throws ParseException {
		
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setFingerprint(transaction.getFingerprint());
		transactionDto.setDescription(transaction.getDescription());
		transactionDto.setCurrency(transaction.getCurrency());
		transactionDto.setAmount(transaction.getAmount() == null ? 0 : transaction.getAmount());
		transactionDto.setSplitted(transaction.getSplitted());
		transactionDto.setDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(transaction.getTransactionDate()));
		return transactionDto;
	}
	
	
	public static TransactionDto toTransactionDto(Integer instCode, Account account, Transaction transaction, Category category, List<TransactionDto> splittedTransactions) throws ParseException {
		
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setFingerprint(transaction.getFingerprint());
		transactionDto.setDescription(transaction.getDescription());
		transactionDto.setCurrency(transaction.getCurrency());
		transactionDto.setAmount(transaction.getAmount() == null ? 0 : transaction.getAmount());
		transactionDto.setDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(transaction.getTransactionDate()));
		transactionDto.setSplitted(transaction.getSplitted());
		
		AccountDto accountDto = new AccountDto();
		accountDto.setInstCode(instCode);
		accountDto.setAccountName(account.getAccountName());
		accountDto.setAccountId(account.getAccountCode());
		transactionDto.setAccount(accountDto);
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryCode(category == null ? null : category.getUuid());
		categoryDto.setCategoryName(category == null ? null : category.getName());
		transactionDto.setCategory(categoryDto);
		
		if(transaction.getSplitted())
			transactionDto.setSplittedTransactions(splittedTransactions);
		
		return transactionDto;
	}

	
	public static List<TransactionDto> toDashBoardTransactionDtos(List<CategoryTransactionMapper> categoryTransactionMappers) throws ParseException {
		
		List<TransactionDto> transactionDtos = new ArrayList<>();
		
		for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMappers) {
			Transaction transaction = categoryTransactionMapper.getTransaction();
			TransactionDto transactionDto = toTransactionDto(transaction);
			Account account = categoryTransactionMapper.getAccount();
			AccountDto accountDto = new AccountDto();
			accountDto.setAccountId(account.getAccountCode());
			accountDto.setAccountName(account.getAccountName());
			accountDto.setPurpose(account.getPurpose() == null ? null : account.getPurpose().toString());
			transactionDto.setAccount(accountDto);
			transactionDtos.add(transactionDto);
		
		}
		return transactionDtos;
	}
	
	
	public static List<TransactionDto> toTransactionDtos(List<SplitTransactions> splitTransactions) {
		
		List<TransactionDto> transactionDtos = new ArrayList<>();
		
		for(SplitTransactions splitTransaction : splitTransactions) {
			
			TransactionDto transactionDto = new TransactionDto();
			transactionDto.setAmount(splitTransaction.getAmount());
			
			Category category = splitTransaction.getCategory();
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setCategoryCode(category.getUuid());
			categoryDto.setCategoryName(category.getName());
			transactionDto.setCategory(categoryDto);
			transactionDtos.add(transactionDto);
		}
		return transactionDtos;
	}
	
}
