package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "goalType")
public class GoalType extends BaseModel {
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
