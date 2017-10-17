package com.sevya.onemoney.dto.mapper;

import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.onemoney.dto.AccountDto;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.Purpose;

public class AccountDtoMapper {
	
	private AccountDtoMapper() { }
	
	public static Account toAccount(Account account,AccountDto accountDto) throws Exception {
		
		account.setId(account.getId());
		account.setAccountCode(accountDto.getAccountId() != null ? accountDto.getAccountId() : account.getAccountCode());
		account.setHideFromBudget(accountDto.getHideFromBudget() != null ? accountDto.getHideFromBudget() : account.getHideFromBudget());
		account.setMinBalance(accountDto.getMinBalance() != null ? accountDto.getMinBalance() : account.getMinBalance());
		account.setUuid(account.getId() == null ? LaunchpadUtility.generateUUIDCode() : account.getUuid());
		
		String nickName = accountDto.getAccountName() != null ? accountDto.getAccountName() : account.getAccountName();
		if(nickName == null ? true :false){
			throw new NullPointerException("Account name mandatory...!!!");
		}else{
			account.setAccountName(nickName);
		}
		
		if(accountDto.getPurpose() != null){
			switch(accountDto.getPurpose()){
				case "B" :  account.setPurpose(Purpose.B);
					break;
				case "P" :  account.setPurpose(Purpose.P);
					break;
				default : throw new NullPointerException("Account type doesn't match...!!!");
			}
		}else{
			if(account.getPurpose() == null){
				throw new NullPointerException("Account type is mandatory...!!!");
			}else{
				account.setPurpose(account.getPurpose());
			}
		}
		return account;
	}
	
	public static AccountDto toAccountDto(AccountDto accountDto,Object[] account){
		accountDto.setAccountId(account[1].toString());
		accountDto.setAccountName(account[2].toString());
		accountDto.setPurpose(account[3].toString());
		accountDto.setUpdatedAt(account[4] == null ? "" : account[4].toString());
		accountDto.setAccountType(account[5].toString());
		accountDto.setStatus(0);
		return accountDto;
	}
	
	public static AccountDto toAccountDto(AccountDto accountDto,Account account){
		accountDto.setAccountId(account.getAccountCode());
		accountDto.setAccountName(account.getAccountName());
		accountDto.setPurpose(account.getPurpose().toString());
		accountDto.setUpdatedAt(account.getUpdatedAt() == null ? "" : DateUtility.DATE_FORMATTER_FOR_SQL_FORMAT.format(account.getUpdatedAt()));
		accountDto.setAccountType(account.getAccountType().getName());
		accountDto.setStatus(0);
		return accountDto;
	}
	

}
