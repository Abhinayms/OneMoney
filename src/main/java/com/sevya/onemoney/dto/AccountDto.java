package com.sevya.onemoney.dto;

import java.util.List;

public class AccountDto extends AppBaseDto {
	
	private String accountName;
	private String accountId;
	private Boolean hideFromBudget;
	private Float minBalance;
	private String purpose;
	private String accountType;
	private String groupType;
	private Integer status;
	private Integer instCode;
	private Double totalIncoming;
	private Double totalOutgoing;
	private String updatedAt;
	private Integer count;
	private List<String> accountIds;
	private List<AccountDto> accounts;
	private List<CategoricalExpensesDto> categoricalExpenses;
	private List<ValuesDto> values;
	private List<TransactionDto> transactions;
	private Integer totalTransactionsCount;

	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Boolean getHideFromBudget() {
		return hideFromBudget;
	}
	public void setHideFromBudget(Boolean hideFromBudget) {
		this.hideFromBudget = hideFromBudget;
	}
	public Float getMinBalance() {
		return minBalance;
	}
	public void setMinBalance(Float minBalance) {
		this.minBalance = minBalance;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getInstCode() {
		return instCode;
	}
	public void setInstCode(Integer instCode) {
		this.instCode = instCode;
	}
	public Double getTotalIncoming() {
		return totalIncoming;
	}
	public void setTotalIncoming(Double totalIncoming) {
		this.totalIncoming = totalIncoming;
	}
	public Double getTotalOutgoing() {
		return totalOutgoing;
	}
	public void setTotalOutgoing(Double totalOutgoing) {
		this.totalOutgoing = totalOutgoing;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<AccountDto> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountDto> accounts) {
		this.accounts = accounts;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<String> getAccountIds() {
		return accountIds;
	}
	public void setAccountIds(List<String> accountIds) {
		this.accountIds = accountIds;
	}

	public List<CategoricalExpensesDto> getCategoricalExpenses() {
		return categoricalExpenses;
	}
	public void setCategoricalExpenses(List<CategoricalExpensesDto> categoricalExpenses) {
		this.categoricalExpenses = categoricalExpenses;
	}
	public List<ValuesDto> getValues() {
		return values;
	}
	public void setValues(List<ValuesDto> values) {
		this.values = values;
	}
	public List<TransactionDto> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionDto> transactions) {
		this.transactions = transactions;
	}
	public Integer getTotalTransactionsCount() {
		return totalTransactionsCount;
	}
	public void setTotalTransactionsCount(Integer totalTransactionsCount) {
		this.totalTransactionsCount = totalTransactionsCount;
	}
	
}
