package com.sevya.onemoney.dto;

import java.util.List;
import java.util.Set;

public class AccountSummaryDto extends AppBaseDto {
	
	private AccountDto accountData;
	private List<AccountDto> creditCards;
	private Integer uncategorizedTransactionsCount;
	
	/** Parameters for Budget Summary  **/
	private Long spentAmount;
	private Long avgSpentAmount;
	private Long monthlyIncome;
	private Float budgetedAmount;
	private Long unBudgetedAmount;
	private List<BudgetDto> budgets;
	private Set<BudgetDto> unbudgetedCategories;
	
	/** Parameters for Goal Summary  **/
	private Float targetSavings;
	private List<GoalDto> goals;
	
	private Long totalTargetAmount;
	private Long totalSavedAmount;
	
	public AccountDto getAccountData() {
		return accountData;
	}
	public void setAccountData(AccountDto accountData) {
		this.accountData = accountData;
	}
	
	public List<AccountDto> getCreditCards() {
		return creditCards;
	}
	public void setCreditCards(List<AccountDto> creditCards) {
		this.creditCards = creditCards;
	}
	public Integer getUncategorizedTransactionsCount() {
		return uncategorizedTransactionsCount;
	}
	public void setUncategorizedTransactionsCount(Integer uncategorizedTransactionsCount) {
		this.uncategorizedTransactionsCount = uncategorizedTransactionsCount;
	}
	public Long getUnBudgetedAmount() {
		return unBudgetedAmount;
	}
	public Float getBudgetedAmount() {
		return budgetedAmount;
	}
	public Long getSpentAmount() {
		return spentAmount;
	}
	public Long getAvgSpentAmount() {
		return avgSpentAmount;
	}
	public Long getMonthlyIncome() {
		return monthlyIncome;
	}
	public List<BudgetDto> getBudgets() {
		return budgets;
	}
	public void setUnBudgetedAmount(Long unBudgetedAmount) {
		this.unBudgetedAmount = unBudgetedAmount;
	}
	public void setBudgetedAmount(Float budgetedAmount) {
		this.budgetedAmount = budgetedAmount;
	}
	public void setSpentAmount(Long spentAmount) {
		this.spentAmount = spentAmount;
	}
	public void setAvgSpentAmount(Long avgSpentAmount) {
		this.avgSpentAmount = avgSpentAmount;
	}
	public void setMonthlyIncome(Long monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public void setBudgets(List<BudgetDto> budgets) {
		this.budgets = budgets;
	}
	public Set<BudgetDto> getUnbudgetedCategories() {
		return unbudgetedCategories;
	}
	public void setUnbudgetedCategories(Set<BudgetDto> unbudgetedCategories) {
		this.unbudgetedCategories = unbudgetedCategories;
	}
	public Float getTargetSavings() {
		return targetSavings;
	}
	public List<GoalDto> getGoals() {
		return goals;
	}
	public void setTargetSavings(Float targetSavings) {
		this.targetSavings = targetSavings;
	}
	public void setGoals(List<GoalDto> goals) {
		this.goals = goals;
	}
	public Long getTotalTargetAmount() {
		return totalTargetAmount;
	}
	public void setTotalTargetAmount(Long totalTargetAmount) {
		this.totalTargetAmount = totalTargetAmount;
	}
	public Long getTotalSavedAmount() {
		return totalSavedAmount;
	}
	public void setTotalSavedAmount(Long totalSavedAmount) {
		this.totalSavedAmount = totalSavedAmount;
	}
	
}
