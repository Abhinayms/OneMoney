package com.sevya.onemoney.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.client.RestClientException;

import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.CategoricalExpensesDto;
import com.sevya.onemoney.dto.PaginationDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.TransactionDto;
import com.sevya.onemoney.dto.TxRequestDto;
import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.utility.Purpose;

public interface TransactionService {

	
	public void uploadAccountTransactions(User user, RequestDto requestDto) throws Exception;

	public TransactionDto recategoriseTransaction(User user, TransactionDto transactionDto)throws Exception;

	public void deleteTransactionsFromCAASServer(User user, String accountCode) throws RestClientException;
	
	public PaginationDto getAllTransactions(User user, TxRequestDto txRequestDto)throws Exception;

	public TransactionDto getTransactionDetails(User user, String fingerprint)throws Exception;
	
	public TransactionDto splitTransactions(User user, TransactionDto transactionDto) throws Exception;
	
	public Integer getCountOfCurrentMonthTransactionsByAccountIdAndUserIdAndMonthAndYear(Long userId, Long accountId,Integer month, Integer year,String accountType,String purpose);

	public List<TransactionDto> userTransactionsByMonthYearAndCount(Long userId, Integer month, Integer year, Integer noOfTransactions) throws Exception;
	
	public List<CategoricalExpensesDto> userSpentMainCategoryTransactionsByAccountTypeMonthYearAndCount(Long userId, Purpose purpose, Integer month, Integer year, Integer noOfTransactions) throws Exception;

	public Set<String> searchSuggestions(User user, TxRequestDto txRequestDto) throws Exception;

	public void deleteTransactionsByUserIdAndAccountCode(Long userId,String accountCode);

	public List<CategoryTransactionMapper> getCategoryTransactionMapperForLast3TransactionsByAccountIdAndUserIdAndMonthAndYear(Long userId, Long accountId, Integer month, Integer year);


}
