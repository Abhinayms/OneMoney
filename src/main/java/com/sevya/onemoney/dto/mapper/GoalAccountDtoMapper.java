package com.sevya.onemoney.dto.mapper;

import com.sevya.onemoney.dto.GoalAccountDto;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.model.GoalAccountMapper;

public class GoalAccountDtoMapper {

	private GoalAccountDtoMapper() {
		
	}
	
	public static GoalAccountMapper toGoalAccountMapper(GoalAccountDto goalAccountDto, Goal goal, Account account) {

		GoalAccountMapper goalAccountMapper = new GoalAccountMapper();
		goalAccountMapper.setProportion(goalAccountDto.getProportion());
		goalAccountMapper.setRateOfInterest(goalAccountDto.getRateOfInterest());
		goalAccountMapper.setAccount(account);
		goalAccountMapper.setGoal(goal);
		return goalAccountMapper;
	
	}

	public static GoalAccountMapper toGoalAccountMapper(GoalAccountMapper goalAccountMapper,GoalAccountDto goalDto){

		goalAccountMapper.setProportion(goalDto.getProportion() != null ? goalDto.getProportion() : goalAccountMapper.getProportion());
		goalAccountMapper.setRateOfInterest(goalDto.getRateOfInterest() != null ? goalDto.getRateOfInterest() : goalAccountMapper.getRateOfInterest());
		return goalAccountMapper;
	
	}
	
	
	
}
