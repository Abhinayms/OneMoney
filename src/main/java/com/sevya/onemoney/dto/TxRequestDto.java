package com.sevya.onemoney.dto;

import java.util.List;

public class TxRequestDto extends PaginationDto {

	private List<String> accounts;
	private String transactionType;
	private Integer dataType;
	private Float minAmount;
	private Float maxAmount;
	private List<String> categories;
	
	public List<String> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public Float getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Float minAmount) {
		this.minAmount = minAmount;
	}
	public Float getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Float maxAmount) {
		this.maxAmount = maxAmount;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	
}
