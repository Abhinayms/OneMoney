package com.sevya.onemoney.dto.mapper;

import com.sevya.onemoney.dto.GoalAssetDto;
import com.sevya.onemoney.model.Asset;

public class AssetDtoMapper {
	
	private AssetDtoMapper() { }
	
	public static Asset toAsset(GoalAssetDto goalAssetDto) {
		
		Asset asset = new Asset();
		asset.setName(goalAssetDto.getName());
		asset.setGrowthRate(goalAssetDto.getGrowthRate());
		asset.setOriginalValue(goalAssetDto.getCurrentValue());
		return asset;
	
	}
	
	
	public static Asset toAsset(Asset asset,GoalAssetDto goalAssetDto) {
		
		asset.setName(goalAssetDto.getName() == null ? asset.getName()  : goalAssetDto.getName());
		asset.setGrowthRate(goalAssetDto.getGrowthRate() == null ? asset.getGrowthRate() : goalAssetDto.getGrowthRate() );
		asset.setOriginalValue(goalAssetDto.getCurrentValue() == null ? asset.getOriginalValue() : goalAssetDto.getCurrentValue());
		return asset;

	}

}
