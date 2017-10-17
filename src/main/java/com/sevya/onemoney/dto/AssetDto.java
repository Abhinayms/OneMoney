package com.sevya.onemoney.dto;

import java.util.Date;

public class AssetDto extends AppBaseDto{
	
	private Long assetId;
	private String name;
	private String description;
	private float originalValue;	
	private float growthRate;	
	private Date originalValuationDate;
	
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(float originalValue) {
		this.originalValue = originalValue;
	}
	public float getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(float growthRate) {
		this.growthRate = growthRate;
	}
	public Date getOriginalValuationDate() {
		return originalValuationDate;
	}
	public void setOriginalValuationDate(Date originalValuationDate) {
		this.originalValuationDate = originalValuationDate;
	}

	
}
