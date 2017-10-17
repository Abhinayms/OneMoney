package com.sevya.onemoney.dto;

import java.util.List;

public class GoalDto extends AppBaseDto {

	private Long goalId;
	private String name;
	private String message;
	private Float targetAmount;
	private Float monthlyContrib;
	private String targetDate;
	private Long savedAmount;
	private Boolean goalAchieved;
	private String purpose;
	
	private Double timeToTarget;
	private Double propertyTimeToTarget;
	private Long propertyFV;
	private Long propertyCV;
	private Long savingsTarget;
	private Long suggestedMonthlyContrib;
	private Long contribFV;
	private Long accountBalanceFV;
	private Long totalFV;
	private Long shortfall;
	private Long expectedAdditionalSavings;
	private Long expectedAccountBalance;
	private Long monthlyAdditionalContrib;
	private Long expectedSavings;
	private String createdAt;
	
	private List<GoalAccountDto> accounts;	
	private List<GoalAssetDto> assets;
	
	public Long getGoalId() {
		return goalId;
	}
	public void setGoalId(Long goalId) {
		this.goalId = goalId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Float getTargetAmount() {
		return targetAmount;
	}
	public void setTargetAmount(Float targetAmount) {
		this.targetAmount = targetAmount;
	}
	public Float getMonthlyContrib() {
		return monthlyContrib;
	}
	public void setMonthlyContrib(Float monthlyContrib) {
		this.monthlyContrib = monthlyContrib;
	}
	
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public Long getSavedAmount() {
		return savedAmount;
	}
	public void setSavedAmount(Long savedAmount) {
		this.savedAmount = savedAmount;
	}
	
	public Boolean getGoalAchieved() {
		return goalAchieved;
	}
	public void setGoalAchieved(Boolean goalAchieved) {
		this.goalAchieved = goalAchieved;
	}
	public List<GoalAccountDto> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<GoalAccountDto> accounts) {
		this.accounts = accounts;
	}
	public List<GoalAssetDto> getAssets() {
		return assets;
	}
	public void setAssets(List<GoalAssetDto> assets) {
		this.assets = assets;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public Double getTimeToTarget() {
		return timeToTarget;
	}
	public void setTimeToTarget(Double timeToTarget) {
		this.timeToTarget = timeToTarget;
	}
	public Double getPropertyTimeToTarget() {
		return propertyTimeToTarget;
	}
	public void setPropertyTimeToTarget(Double propertyTimeToTarget) {
		this.propertyTimeToTarget = propertyTimeToTarget;
	}
	public Long getPropertyFV() {
		return propertyFV;
	}
	public void setPropertyFV(Long propertyFV) {
		this.propertyFV = propertyFV;
	}
	public Long getPropertyCV() {
		return propertyCV;
	}
	public void setPropertyCV(Long propertyCV) {
		this.propertyCV = propertyCV;
	}
	public Long getSavingsTarget() {
		return savingsTarget;
	}
	public void setSavingsTarget(Long savingsTarget) {
		this.savingsTarget = savingsTarget;
	}
	public Long getSuggestedMonthlyContrib() {
		return suggestedMonthlyContrib;
	}
	public void setSuggestedMonthlyContrib(Long suggestedMonthlyContrib) {
		this.suggestedMonthlyContrib = suggestedMonthlyContrib;
	}
	public Long getContribFV() {
		return contribFV;
	}
	public void setContribFV(Long contribFV) {
		this.contribFV = contribFV;
	}
	public Long getAccountBalanceFV() {
		return accountBalanceFV;
	}
	public void setAccountBalanceFV(Long accountBalanceFV) {
		this.accountBalanceFV = accountBalanceFV;
	}
	public Long getTotalFV() {
		return totalFV;
	}
	public void setTotalFV(Long totalFV) {
		this.totalFV = totalFV;
	}
	public Long getShortfall() {
		return shortfall;
	}
	public void setShortfall(Long shortfall) {
		this.shortfall = shortfall;
	}
	public Long getExpectedAdditionalSavings() {
		return expectedAdditionalSavings;
	}
	public void setExpectedAdditionalSavings(Long expectedAdditionalSavings) {
		this.expectedAdditionalSavings = expectedAdditionalSavings;
	}
	public Long getExpectedAccountBalance() {
		return expectedAccountBalance;
	}
	public void setExpectedAccountBalance(Long expectedAccountBalance) {
		this.expectedAccountBalance = expectedAccountBalance;
	}
	public Long getMonthlyAdditionalContrib() {
		return monthlyAdditionalContrib;
	}
	public void setMonthlyAdditionalContrib(Long monthlyAdditionalContrib) {
		this.monthlyAdditionalContrib = monthlyAdditionalContrib;
	}
	public Long getExpectedSavings() {
		return expectedSavings;
	}
	public void setExpectedSavings(Long expectedSavings) {
		this.expectedSavings = expectedSavings;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
}

