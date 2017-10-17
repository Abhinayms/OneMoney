package com.sevya.onemoney.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "transaction")
public class Transaction extends BaseModel {
	
	private Float amount;
	private String currency;
	private String fingerprint;
	private String description;
	private String accountCode;
	private Date transactionDate;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private Boolean isSplitted = false;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private Boolean isCategorized = false;
	
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(String fingerPrint) {
		this.fingerprint = fingerPrint;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsCategorized() {
		return isCategorized;
	}
	public void setIsCategorized(Boolean isCategorized) {
		this.isCategorized = isCategorized;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public Boolean getSplitted() {
		return isSplitted;
	}
	public void setSplitted(Boolean isSplitted) {
		this.isSplitted = isSplitted;
	}
	
	
}
