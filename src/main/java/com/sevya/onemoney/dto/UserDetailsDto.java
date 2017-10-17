package com.sevya.onemoney.dto;

import com.sevya.launchpad.dto.UserDto;

public class UserDetailsDto extends UserDto {

	private String name;
    private Float personalMonthlyIncome;
    private Float businessMonthlyIncome;
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
