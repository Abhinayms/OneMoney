package com.sevya.onemoney.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;
import com.sevya.onemoney.utility.Purpose;


@Entity
@Table(name = "goal")
public class Goal extends BaseModel {

	private String name;
	private String description;
	private float targetAmount;
	private float installmentAmount;
	private Date targetDate;
	
	@ManyToOne(targetEntity = GoalType.class, fetch = FetchType.LAZY)
	@JoinColumn(name="goalTypeId")
	private GoalType goalType;
	
	@ManyToOne(targetEntity = FrequencyType.class, fetch = FetchType.LAZY)
	@JoinColumn(name="frequencyTypeId")
	private FrequencyType frequencyType;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum ('P','B')", nullable = true)
	private Purpose purpose;
	
	
	@Column(columnDefinition="tinyint(1) default 0")
	private Boolean goalAchieved = false;
	
		
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

	public float getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(float targetAmount) {
		this.targetAmount = targetAmount;
	}

	public float getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(float installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public GoalType getGoalType() {
		return goalType;
	}

	public void setGoalType(GoalType goalType) {
		this.goalType = goalType;
	}

	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(FrequencyType frequencyType) {
		this.frequencyType = frequencyType;
	}

	public Purpose getPurpose() {
		return purpose;
	}

	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}

	public Boolean getGoalAchieved() {
		return goalAchieved;
	}

	public void setGoalAchieved(Boolean goalAchieved) {
		this.goalAchieved = goalAchieved;
	}
	
	
}
