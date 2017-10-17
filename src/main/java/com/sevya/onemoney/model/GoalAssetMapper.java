package com.sevya.onemoney.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "goalAssetMapper")
public class GoalAssetMapper extends BaseModel {
	
	@ManyToOne(targetEntity = Goal.class, fetch = FetchType.LAZY)
	@JoinColumn(name="goalId")
	private Goal goal;
	
	@ManyToOne(targetEntity = Asset.class, fetch = FetchType.LAZY)
	@JoinColumn(name="assetId")
	private Asset asset;
	
	private Float proportion;

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Float getProportion() {
		return proportion;
	}

	public void setProportion(Float proportion) {
		this.proportion = proportion;
	}
	
	
	

}
