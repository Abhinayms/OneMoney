package com.sevya.onemoney.dto;

import java.util.List;

public class AccountGroupDto extends AppBaseDto {
	
	private String instId;
	private Long instCode;
	private Long accountGroupId;
	private List<AccountDto> accounts;
	
	public AccountGroupDto() { }
	
	public AccountGroupDto(Long accountGroupId){
		this.accountGroupId = accountGroupId;
	}
	
	public Long getInstCode() {
		return instCode;
	}
	public void setInstCode(Long instCode) {
		this.instCode = instCode;
	}
	public Long getAccountGroupId() {
		return accountGroupId;
	}
	public void setAccountGroupId(Long accountGroupId) {
		this.accountGroupId = accountGroupId;
	}
	public List<AccountDto> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountDto> accounts) {
		this.accounts = accounts;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	
}
