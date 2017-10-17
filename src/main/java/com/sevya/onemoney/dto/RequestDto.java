package com.sevya.onemoney.dto;

import java.util.List;

public class RequestDto {
	
	private AccountDto account;
	private Long accountGroupId;
	private String fingerPrint;
	private String categoryCode;
	private List<BudgetDto> budgets;
	
	private List<AccountTransactionsDto> accountTransactions;
	
	public List<AccountTransactionsDto> getAccountTransactions() {
		return accountTransactions;
	}

	public void setAccountTransactions(List<AccountTransactionsDto> accountTransactions) {
		this.accountTransactions = accountTransactions;
	}

	public List<BudgetDto> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<BudgetDto> budgets) {
		this.budgets = budgets;
	}

	public AccountDto getAccountDto() {
		return account;
	}

	public void setAccountDto(AccountDto accountDto) {
		this.account = accountDto;
	}

	public Long getAccountGroupId() {
		return accountGroupId;
	}

	public void setAccountGroupId(Long accountGroupId) {
		this.accountGroupId = accountGroupId;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	
}
