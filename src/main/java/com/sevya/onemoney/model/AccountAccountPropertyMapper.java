package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "accountAccountPropertyMapper")
public class AccountAccountPropertyMapper extends BaseModel {
	
	private String value;
	
	@ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
	@JoinColumn(name="accountId")
	private Account account;
	
	@ManyToOne(targetEntity = AccountProperty.class, fetch = FetchType.LAZY)
	@JoinColumn(name="accountPropertyId")
	private AccountProperty accountProperty;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public AccountProperty getAccountProperty() {
		return accountProperty;
	}

	public void setAccountProperty(AccountProperty accountProperty) {
		this.accountProperty = accountProperty;
	}
	
	
	
	

}
