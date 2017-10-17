package com.sevya.onemoney.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;
import com.sevya.launchpad.model.User;

@Entity
@Table(name = "userDetails")
public class UserDetails extends BaseModel {
	

	@OneToOne(targetEntity=User.class,cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private User user;
	
	private Float personalMonthlyIncome;
	
	private Float businessMonthlyIncome;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Float getPersonalMonthlyIncome() {
		return personalMonthlyIncome;
	}

	public void setPersonalMonthlyIncome(Float personalMonthlyIncome) {
		this.personalMonthlyIncome = personalMonthlyIncome;
	}

	public Float getBusinessMonthlyIncome() {
		return businessMonthlyIncome;
	}

	public void setBusinessMonthlyIncome(Float businessMonthlyIncome) {
		this.businessMonthlyIncome = businessMonthlyIncome;
	}
	
	
	
}
