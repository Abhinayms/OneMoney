package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "accountaccountgroupmapper")
public class AccountAccountGroupMapper extends BaseModel {
	
	@ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
	@JoinColumn( name = "accountId")
	private Account account;
	
	@ManyToOne(targetEntity = AccountGroup.class, fetch = FetchType.LAZY)
	@JoinColumn( name = "accountGroupId")
	private AccountGroup accountGroup;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public AccountGroup getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(AccountGroup accountGroup) {
		this.accountGroup = accountGroup;
	}
	
	

}
