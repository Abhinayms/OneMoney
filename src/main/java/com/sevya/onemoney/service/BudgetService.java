package com.sevya.onemoney.service;

import java.util.List;
import java.util.Map;

import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.BudgetDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.model.Budget;
import com.sevya.onemoney.utility.Purpose;

public interface BudgetService {

	/**
	 * Create new Budget
	 * @param BudgetDto 
	 * @return BudgetDto
	 * */
	public BudgetDto addBudget(RequestDto requestDto,User user) throws Exception;
	
	/**
	 * Edit Budget
	 * @param BudgetDto 
	 * @return BudgetDto
	 * */
	public BudgetDto updateBudget(RequestDto requestDto,User user) throws Exception;

	/**
	 * Delete Budget
	 * @param BudgetId 
	 * @return String true if deleted else exception message
	 * */
	public void deleteBudget(User user,Long budgetId);
	
	/**
	 * Get All Budgets
	 * @param Null 
	 * @return List<BudgetDto>
	 * */
	public List<BudgetDto> getAllBudgets(User user,Object month,Object year) throws Exception;
	
	/**
	 * Get Budget
	 * @param categoryId 
	 * @return Budget
	 * */
	public Budget getBudgetByCatagoryId(User user,Long categoryId,Purpose purpose);

	//public Budget getBudgetByCatagoryIdAndDates(Long userId,Long categoryId, String date1, String date2);

	public Float getAllBudgetsOfUserPerMonth(Long userId, Integer month, Integer year);
	
	public Budget getTotalAmountBudgetedForIncome(Long userId,String categoryName, Object month, Object year);
	
	public Float getTotalAmountBudgeted(Long userId,String categoryName, Object month, Object year);

	public Map<String, Float> getUserBudgetInfoByMonthAndYear(User user, String purpose, Integer month, Integer year);

	public ResponseDto getBudgetsAndGoalsSummary(User user,List<GoalDto> goalDtos) throws Exception;

}
