package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "accountgroup")
public class AccountGroup extends BaseModel {

	private String name;
	private String instId;
	
	@OneToOne(targetEntity = FinancialInstitution.class, fetch = FetchType.LAZY)
	@JoinColumn( name = "financialInstitutionId")
	private FinancialInstitution financialInstitution;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}

	public void setFinancialInstitution(FinancialInstitution financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	

	
}
