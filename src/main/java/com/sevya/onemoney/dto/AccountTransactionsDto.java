package com.sevya.onemoney.dto;

import java.util.List;

public class AccountTransactionsDto extends AppBaseDto {
	
	private String accountId;
	private String updatedAt;
    private List<TransactionDto> transactions;
	
    public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<TransactionDto> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionDto> transactions) {
		this.transactions = transactions;
	}
    
	
}
