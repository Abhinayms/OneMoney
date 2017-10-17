package com.sevya.onemoney.service;

import java.util.List;
import java.util.Map;

import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.AccountDto;
import com.sevya.onemoney.dto.AccountGroupDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.utility.Purpose;

public interface AccountService {
	
	
	public Account getAccountByCode(User user,String accountCode);
	
	public void saveAccounts(List<Account> accounts);
	
	public AccountGroupDto addAccounts(User user,AccountGroupDto accountGroupDto) throws Exception;
	
	public void updateAccounts(RequestDto requestDto) throws Exception;
	
	public void unlickAccounts(User user,Long groupId,String accountCode) throws Exception;
	
	public ResponseDto getAccountSummary(User user) throws Exception;
	
	public List<AccountGroupDto> getAccountsList(User user) throws Exception;

	public List<AccountDto> getAccountsByGroupId(User user,Long groupId) throws Exception;
	
	public Map<String,Object> dashBoard(User user,List<GoalDto> goalDtos) throws Exception;
	
	public Integer getFinancialInstitutionIdByAccountId(Long accountId);
	
	public AccountDto getAccountsByAccountTypeAndPurpose(User user,String accountTypeGroup,String purpose) throws Exception;
	
	public void removeInstitutionByGroupId(User user,Long accountGroupId) throws Exception;
	
	public Integer checkingAccountsAvailabilityByUserAndPurpose(Long userId, Purpose purpose);
	
}
