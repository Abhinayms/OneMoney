package com.sevya.onemoney.service;

import java.util.List;
import java.util.Map;

import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.GoalBudgetSummaryDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.model.GoalAccountMapper;
import com.sevya.onemoney.model.GoalAssetMapper;
import com.sevya.onemoney.utility.Purpose;

public interface GoalService {
	
	/**
	 * Get All Goals
	 * @param Null 
	 * @return List<GoalDto>
	 * */
	public Goal getGoal(Long goalId);
	
	public Goal getGoalByIdAndPurpose(Long goalId,Purpose purpose);
	
	public List<GoalDto> getGoals(User user,Integer month,Integer year) throws Exception;
	
	public GoalDto saveGoal(User user,GoalDto goalDto) throws Exception;
	
	public GoalDto editGoal(GoalDto goalDto,User user) throws Exception;
	
	public void deleteGoal(Long goalId,User user);
	
	public GoalBudgetSummaryDto getGoalsAndBudgetSummary(User user,Integer month,Integer year);
	
	public Map<String,Float> getGoalsInfoByUser(User user);
	
	public List<GoalDto> getUserGoalsAndAccounts(User user);
	
	public List<GoalAssetMapper> getGoalAssetMappersByGoalIdAndUserId(Long userId,Long goalId);
	
	public List<GoalAccountMapper> getGoalAccountMappersByGoalIdAndUserId(Long userId,Long goalId);
	
	public GoalAccountMapper getGoalAccountMappersByGoalIdAndAccountCodeAndUserId(Long userId,Long goalId,String accountCode);

	public Double getSuggestedAmountWhileGetGoals(Goal goal, User user) throws Exception;
	
	public Long getSuggetedMonthlyContribution(GoalDto goalDto,User user) throws Exception;
	
	public List<Goal> getGoalsByUserId(Long userId);

	public Map<String, Long> getGoalsData(User user, List<GoalDto> goalDtos, String purpose) throws Exception;
	
}
