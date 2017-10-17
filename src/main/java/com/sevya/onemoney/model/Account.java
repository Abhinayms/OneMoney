package com.sevya.onemoney.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;
import com.sevya.onemoney.utility.Purpose;

@Entity
@Table(name = "account")
public class Account extends BaseModel {
	
	private Date updatedAt;
	private Float minBalance;
	private String accountCode;
	private String accountName;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private Boolean hideFromBudget;
	
	@ManyToOne(targetEntity = AccountType.class, fetch = FetchType.LAZY)
	@JoinColumn(name="accountTypeId")
	private AccountType  accountType;
		
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum ('P','B')", nullable = true)
	private Purpose purpose;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
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

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Purpose getPurpose() {
		return purpose;
	}

	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}

	
		
}
