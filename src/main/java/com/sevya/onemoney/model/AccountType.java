package com.sevya.onemoney.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "accounttype")
public class AccountType extends BaseModel {
	
	private String name;
	
	@OneToMany(targetEntity = Account.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "accountTypeId")
	private Set<Account> accounts;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	
}
