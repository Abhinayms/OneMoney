package com.sevya.onemoney.dto.mapper;

import java.util.Date;
import java.util.List;

import com.sevya.onemoney.dto.GoalAssetDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.model.Asset;
import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.model.GoalAssetMapper;

public class GoalAssetDtoMapper {

	private GoalAssetDtoMapper() {
		
	}
	
	public static GoalAssetMapper toGoalAssetMapper(GoalAssetDto goalAssetDto, Goal goal, Asset asset) {

		GoalAssetMapper goalAssetMapper = new GoalAssetMapper();
		goalAssetMapper.setAsset(asset);
		goalAssetMapper.setGoal(goal);
		goalAssetMapper.setProportion(goalAssetDto.getProportion());
		return goalAssetMapper;
		
	}

	public static GoalAssetMapper toGoalAssetMapper(GoalAssetMapper goalAssetMapper,GoalAssetDto goalAssetDto){
		
		goalAssetMapper.setProportion(goalAssetDto.getProportion() != null ? goalAssetDto.getProportion() : goalAssetMapper.getProportion());
		return goalAssetMapper;
	
	}

	public static Long calculateCurrentValueByGoalAssetMappers(List<GoalAssetMapper> goalAssetMappers) {
		
		double currentValue = 0f;
		for(GoalAssetMapper goalAssetMapper : goalAssetMappers){
 			Asset asset = goalAssetMapper.getAsset();
 			if(asset.getOriginalValue() != null && goalAssetMapper.getProportion() != null){
 	 			currentValue = currentValue + asset.getOriginalValue() * goalAssetMapper.getProportion();
 	 		}
 	 	}
		return Math.round(currentValue);
	
	}
		


	public static Long calculatePropertyFV(List<GoalAssetMapper> goalAssetMappers,GoalDto goalDto) {
	
		double finalValue = 0d;
		for(GoalAssetMapper goalAssetMapper : goalAssetMappers) {
 			Asset asset = goalAssetMapper.getAsset();
 			double accountGrowthRate =asset.getGrowthRate()/100.0;
 			double formula = Math.pow(1+accountGrowthRate,goalDto.getPropertyTimeToTarget());
 			finalValue = finalValue + (asset.getOriginalValue() * formula);
 	 	}
		return Math.round(finalValue);
		
	}
	
	
	public static Long calculatePropertyCV(List<GoalAssetMapper> goalAssetMappers, Date goalCreatedDate) throws Exception {
		
		Double time = GoalDtoMapper.calculateTimeTillGoalCreation(goalCreatedDate);
		
		double finalValue = 0d;
		for(GoalAssetMapper goalAssetMapper : goalAssetMappers) {
 			Asset asset = goalAssetMapper.getAsset();
 			double accountGrowthRate =asset.getGrowthRate()/100.0;
 			double formula = Math.pow(1+accountGrowthRate,time);
 			finalValue = finalValue + (asset.getOriginalValue() * formula);
 	 	}
		return Math.round(finalValue);
		
	}
	
	
	
	
}
