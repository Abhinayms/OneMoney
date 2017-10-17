package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "frequencyType")
public class FrequencyType extends BaseModel {

	private String name;
	private Integer noOfMonths;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNoOfMonths() {
		return noOfMonths;
	}
	public void setNoOfMonths(Integer noOfMonths) {
		this.noOfMonths = noOfMonths;
	}
}
