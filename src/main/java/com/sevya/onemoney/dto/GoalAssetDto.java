package com.sevya.onemoney.dto;

public class GoalAssetDto extends AppBaseDto {
	
	private Long assetId;
    private String name;
    private Float currentValue;
    private Float growthRate;
    private Float proportion;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(Float currentValue) {
		this.currentValue = currentValue;
	}
	public Float getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(Float growthRate) {
		this.growthRate = growthRate;
	}
	public Float getProportion() {
		return proportion;
	}
	public void setProportion(Float proportion) {
		this.proportion = proportion;
	}
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	
} 
