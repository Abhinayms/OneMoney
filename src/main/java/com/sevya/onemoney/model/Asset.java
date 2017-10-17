package com.sevya.onemoney.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "asset")
public class Asset extends BaseModel {

	private String name;
	private String description;
	private Float originalValue;	
	private Float growthRate;	
	private Date originalValuationDate;
	
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
	public Float getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(Float originalValue) {
		this.originalValue = originalValue;
	}
	public Float getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(Float growthRate) {
		this.growthRate = growthRate;
	}
	public Date getOriginalValuationDate() {
		return originalValuationDate;
	}
	public void setOriginalValuationDate(Date originalValuationDate) {
		this.originalValuationDate = originalValuationDate;
	}	
}
