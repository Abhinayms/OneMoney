package com.sevya.onemoney.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;
import com.sevya.onemoney.utility.Purpose;

@Entity
@Table(name = "budget")
public class Budget extends BaseModel {
	
	private Float budgetedAmount;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private Boolean maxLimit = false;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum ('P','B')", nullable = true)
	private Purpose purpose;
	
	@ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
	@JoinColumn(name="categoryId")
	private Category category;
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Float getBudgetedAmount() {
		return budgetedAmount;
	}
	public void setBudgetedAmount(Float budgetedAmount) {
		this.budgetedAmount = budgetedAmount;
	}
	public Boolean getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(Boolean maxLimit) {
		this.maxLimit = maxLimit;
	}
	public Purpose getPurpose() {
		return purpose;
	}
	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}
	
	
}
