package com.sevya.onemoney.dto;

import java.util.Set;

public class BudgetDto extends AppBaseDto {
	
	private Long budgetId;
	private Long amount;
	private String message;
	private String purpose;
	private Long parentBudgetId;
	private CategoryDto category;
	//private Integer avgSpend;
	//private Integer currentMonthSpend;
	private String subCategoryName;
	private String categoryCode;
	private Float budgetedAmount;
	private Long previousBudgetId;
	
	private Boolean limit;
	private Long spentAmount;
	private Long avgSpentAmount;
	private Float monthlyIncome;
	private Long unBudgetedAmount;
	
	private Set<BudgetDto> subCategories;
	private Set<BudgetDto> unbudgetedCategories;
	
	public Float getBudgetedAmount() {
		return budgetedAmount;
	}
	public void setBudgetedAmount(Float budgetedAmount) {
		this.budgetedAmount = budgetedAmount;
	}
	public Set<BudgetDto> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(Set<BudgetDto> subCategories) {
		this.subCategories = subCategories;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	
	/*public Integer getAvgSpend() {
		return avgSpend;
	}
	public void setAvgSpend(Integer avgSpend) {
		this.avgSpend = avgSpend;
	}
	public Integer getCurrentMonthSpend() {
		return currentMonthSpend;
	}
	public void setCurrentMonthSpend(Integer currentMonthSpend) {
		this.currentMonthSpend = currentMonthSpend;
	}*/
	public CategoryDto getCategory() {
		return category;
	}
	public void setCategory(CategoryDto category) {
		this.category = category;
	}
	public Long getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(Long budgetId) {
		this.budgetId = budgetId;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public Long getAmount() {
		return amount;
	}
	public Long getParentBudgetId() {
		return parentBudgetId;
	}
	public void setParentBudgetId(Long parentBudgetId) {
		this.parentBudgetId = parentBudgetId;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Long getPreviousBudgetId() {
		return previousBudgetId;
	}
	public void setPreviousBudgetId(Long previousBudgetId) {
		this.previousBudgetId = previousBudgetId;
	}
	public Boolean getLimit() {
		return limit;
	}
	public Float getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setLimit(Boolean limit) {
		this.limit = limit;
	}
	public void setMonthlyIncome(Float monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public Long getSpentAmount() {
		return spentAmount;
	}
	public Long getAvgSpentAmount() {
		return avgSpentAmount;
	}
	public Set<BudgetDto> getUnbudgetedCategories() {
		return unbudgetedCategories;
	}
	public void setSpentAmount(Long spentAmount) {
		this.spentAmount = spentAmount;
	}
	public void setAvgSpentAmount(Long avgSpentAmount) {
		this.avgSpentAmount = avgSpentAmount;
	}
	public void setUnbudgetedCategories(Set<BudgetDto> unbudgetedCategories) {
		this.unbudgetedCategories = unbudgetedCategories;
	}
	public Long getUnBudgetedAmount() {
		return unBudgetedAmount;
	}
	public void setUnBudgetedAmount(Long unBudgetedAmount) {
		this.unBudgetedAmount = unBudgetedAmount;
	}
	

}
