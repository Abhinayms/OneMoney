package com.sevya.onemoney.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TransactionDto extends AppBaseDto {

	private String description;
	private String fingerprint;
	private String date;
	private String currency;
	private Float amount;
	private CategoryDto category;
	private AccountDto account;
	private String categoryCode;
	private List<TransactionDto> splittedTransactions;
	
	private Boolean splitted;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	public CategoryDto getCategory() {
		return category;
	}
	public void setCategory(CategoryDto category) {
		this.category = category;
	}
	public AccountDto getAccount() {
		return account;
	}
	public void setAccount(AccountDto account) {
		this.account = account;
	}
	public Boolean getSplitted() {
		return splitted;
	}
	public void setSplitted(Boolean splitted) {
		this.splitted = splitted;
	}
	public List<TransactionDto> getSplittedTransactions() {
		return splittedTransactions;
	}
	public void setSplittedTransactions(List<TransactionDto> splittedTransactions) {
		this.splittedTransactions = splittedTransactions;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	
}
