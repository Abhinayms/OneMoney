package com.sevya.onemoney.dto.mapper;

import java.util.Date;
import java.util.List;

import com.sevya.onemoney.dto.GoalAccountDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.model.GoalAccountMapper;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.OneMoneyConstants;
import com.sevya.onemoney.utility.Purpose;

public class GoalDtoMapper {

	private GoalDtoMapper() {
	}

	public static GoalDto toGoalDto(GoalDto goalDto, Goal goal) {
		goalDto.setGoalId(goal.getId());
		goalDto.setName(goal.getName());
		goalDto.setMonthlyContrib(goal.getInstallmentAmount());
		goalDto.setTargetAmount(goal.getTargetAmount());
		goalDto.setTargetDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(goal.getTargetDate()));
		goalDto.setPurpose(goal.getPurpose() == null ? null : goal.getPurpose().toString());
		goalDto.setGoalAchieved(goal.getGoalAchieved());
		return goalDto;
	}

	public static Goal toGoal(Goal goal, GoalDto goalDto) throws Exception {

		goal.setId(goalDto.getGoalId());
		goal.setName(goalDto.getName() != null ? goalDto.getName() : goal.getName());
		goal.setInstallmentAmount(goalDto.getMonthlyContrib() != null ? goalDto.getMonthlyContrib() : goal.getInstallmentAmount());
		goal.setTargetAmount(goalDto.getTargetAmount() != null ? goalDto.getTargetAmount() : goal.getTargetAmount());
		goal.setGoalAchieved(goalDto.getGoalAchieved() != null ? goalDto.getGoalAchieved() : false);
		
		
		if (goalDto.getPurpose() != null) {

			if (goalDto.getPurpose().equalsIgnoreCase(OneMoneyConstants.BUSINESS)) {
				goal.setPurpose(Purpose.B);
			} else if (goalDto.getPurpose().equalsIgnoreCase(OneMoneyConstants.PERSONAL)) {
				goal.setPurpose(Purpose.P);
			} else {
				throw new NullPointerException("AccountType doesn't match...!!!");
			}
		}

		if (goalDto.getTargetDate() != null) {
			goal.setTargetDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(goalDto.getTargetDate()));
		} else {
			goal.setTargetDate(goal.getTargetDate());
		}

		return goal;
	}

	public static Double calculateTimeTotarget(Date targetedDate) throws Exception {
		int months = DateUtility.getMonthsDifference(new Date(),targetedDate);
		return months/12.0;

	}

	public static Double calculatePropertyTimeToTarget(Date targetedDate, Date propertyDate) throws Exception {
		int months = DateUtility.getMonthsDifference(propertyDate,targetedDate);
		return months/12.0;
	}
	
	public static Double calculateTimeTillGoalCreation(Date goalCreatedDate) throws Exception {
		int months = DateUtility.getMonthsDifference(goalCreatedDate,new Date());
		return months/12.0;

	}
	

	public static Long calculateMonthlyContributionByAcountMappers(List<GoalAccountMapper> goalAccountMappers, GoalDto goalDto) {
		
		double contribution = 0;
		Double period = goalDto.getTimeToTarget();
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers) {
			double rateOfintrest = goalAccountMapper.getRateOfInterest()/100.0;
			double temp  = Math.pow(1+rateOfintrest,period)-1;
			contribution = contribution + (temp/rateOfintrest);
		}
		
		Long monthlyContribution;
		if(contribution > 0)
			monthlyContribution = (long) (goalDto.getSavingsTarget()/contribution)/12;
		else
			monthlyContribution = (long) (goalDto.getSavingsTarget()/12);
		
		return monthlyContribution; 
	}
	
	public static Long calculateContribFV(List<GoalAccountMapper> goalAccountMappers, GoalDto goalDto) {
		
		double contributeFV = 0l;
		Double period = goalDto.getTimeToTarget();
		float monthlyContribution = goalDto.getMonthlyContrib();
		
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers) {
			
			double accountGrowthRate = goalAccountMapper.getRateOfInterest()/100.0;
			double formula  = Math.pow(1+accountGrowthRate,period)-1;
			contributeFV = contributeFV + (formula/accountGrowthRate);
			
		}
		
		return Math.round(monthlyContribution * 12 * contributeFV);
		
	}

	public static Long calculateAccountBalanceFV(List<GoalAccountMapper> goalAccountMappers, GoalDto goalDto) {
		
		double accountBalanceFV = 0f;
		Double period = goalDto.getTimeToTarget();
	
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers) {
			
			Account account = goalAccountMapper.getAccount();
			double accountGrowthRate = goalAccountMapper.getRateOfInterest()/100.0;
			double formula  =  Math.pow(1+accountGrowthRate,period);
			accountBalanceFV = accountBalanceFV + (account.getMinBalance()*formula);
			
		}
		return Math.round(accountBalanceFV);
	}

	public static Long calculateExpectedAdditionalSavings(List<GoalAccountMapper> goalAccountMappers, GoalDto goalDto) {
		
		double expectedAdditionalSavings = 0f;
		Double period = goalDto.getTimeToTarget();
	
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers) {
			
			double accountGrowthRate = goalAccountMapper.getRateOfInterest()/100.0;
			double formula  =  Math.pow(1+accountGrowthRate,period);
			expectedAdditionalSavings = expectedAdditionalSavings+formula;
			
		}
		
		if(expectedAdditionalSavings > 0.0) {
			return Math.round(goalDto.getShortfall()/expectedAdditionalSavings);
		}
		return Math.round(expectedAdditionalSavings); 
		
	}

	public static Long calculateExpectedAccountBalance(List<GoalAccountMapper> goalAccountMappers, GoalDto dto) {
		
		double expectedAccountBal = 0f;
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers) {
			Account account = goalAccountMapper.getAccount();
			expectedAccountBal = expectedAccountBal + account.getMinBalance();
		}
		return Math.round(dto.getExpectedAdditionalSavings() + expectedAccountBal);
		
	}

	public static Long calculateMonthlyAdditionalContrib(List<GoalAccountMapper> goalAccountMappers, GoalDto goalDto) {
		
		double monthlyContribution = 0f;
		Double period = goalDto.getTimeToTarget();
		
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers) {
			double accountGrowthRate = goalAccountMapper.getRateOfInterest()/100.0;
			double formula  = Math.pow(1+accountGrowthRate,period)-1;
			monthlyContribution = monthlyContribution + (formula/accountGrowthRate);
		}
		
		if(monthlyContribution > 0.0) {
			monthlyContribution = (goalDto.getShortfall()/monthlyContribution)/12;
		}
		return Math.round(monthlyContribution); 
	}

	public static Long calculateSavedAmount(List<GoalAccountDto> goalAccountDtos, GoalDto dto) {
		
		double accountBalance = 0;
		for(GoalAccountDto goalAccountDto : goalAccountDtos) {
			double bal =  goalAccountDto.getBalance() != null ? goalAccountDto.getBalance() : 0 ;
			accountBalance = accountBalance + bal;
		}
		return (long)accountBalance + dto.getPropertyCV();
		
	}
	

}
