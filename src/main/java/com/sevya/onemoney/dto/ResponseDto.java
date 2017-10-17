package com.sevya.onemoney.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseDto extends AppBaseDto {
	
	private Object data;
	private String status;
	
	private String message;
	private String lastUpdatedDateForCategory;
	private List<CategoryDto> categories;
	
	private AccountSummaryDto business;
	private AccountSummaryDto personal;
	
	private Boolean businessFlag;
	
	private ResponseDto budgetSummary;
	private ResponseDto goalSummary;
	
	public Boolean getBusinessFlag() {
		return businessFlag;
	}
	public void setBusinessFlag(Boolean businessFlag) {
		this.businessFlag = businessFlag;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastUpdatedDateForCategory() {
		return lastUpdatedDateForCategory;
	}
	public void setLastUpdatedDateForCategory(String lastUpdatedDateForCategory) {
		this.lastUpdatedDateForCategory = lastUpdatedDateForCategory;
	}
	public List<CategoryDto> getCategories() {
		return categories;
	}
	public void setCategories(List<CategoryDto> categories) {
		this.categories = categories;
	}
	public AccountSummaryDto getBusiness() {
		return business;
	}
	public void setBusiness(AccountSummaryDto business) {
		this.business = business;
	}
	public AccountSummaryDto getPersonal() {
		return personal;
	}
	public void setPersonal(AccountSummaryDto personal) {
		this.personal = personal;
	}
	public ResponseDto getBudgetSummary() {
		return budgetSummary;
	}
	public void setBudgetSummary(ResponseDto budgetSummary) {
		this.budgetSummary = budgetSummary;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ResponseDto getGoalSummary() {
		return goalSummary;
	}
	public void setGoalSummary(ResponseDto goalSummary) {
		this.goalSummary = goalSummary;
	}
	
	
}
