package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "financialinstitution")
public class FinancialInstitution extends BaseModel {
	
	private Long instCode;
	private String name;
	
	public Long getInstCode() {
		return instCode;
	}
	public void setInstCode(Long instCode) {
		this.instCode = instCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
