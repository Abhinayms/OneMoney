package com.sevya.onemoney.dto;

public class GoalBudgetSummaryDto extends AppBaseDto {

	private Integer totalBudgeted;
	private Integer totalAvailable;
	private Integer totalSaving;
	private Integer totalSpent;
	private Integer avgSpent;
	private Integer totalIncomeBudgted;
	private Integer totalIncomeCredited;
	
	
	public Integer getTotalIncomeBudgted() {
		return totalIncomeBudgted;
	}

	public void setTotalIncomeBudgted(Integer totalIncomeBudgted) {
		this.totalIncomeBudgted = totalIncomeBudgted;
	}

	public Integer getTotalIncomeCredited() {
		return totalIncomeCredited;
	}

	public void setTotalIncomeCredited(Integer totalIncomeCredited) {
		this.totalIncomeCredited = totalIncomeCredited;
	}

	public Integer getTotalBudgeted() {
		return totalBudgeted;
	}

	public void setTotalBudgeted(Integer totalBudgeted) {
		this.totalBudgeted = totalBudgeted;
	}

	public Integer getTotalAvailable() {
		return totalAvailable;
	}

	public void setTotalAvailable(Integer totalAvailable) {
		this.totalAvailable = totalAvailable;
	}

	public Integer getTotalSaving() {
		return totalSaving;
	}

	public void setTotalSaving(Integer totalSaving) {
		this.totalSaving = totalSaving;
	}

	public Integer getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Integer totalSpent) {
		this.totalSpent = totalSpent;
	}

	public Integer getAvgSpent() {
		return avgSpent;
	}

	public void setAvgSpent(Integer avgSpent) {
		this.avgSpent = avgSpent;
	}

}
