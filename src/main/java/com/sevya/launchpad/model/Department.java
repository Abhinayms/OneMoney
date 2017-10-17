package com.sevya.launchpad.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;


@Entity
@Table(name = "department")
public class Department extends BaseModel {

	private String code;
	private String name;
	private String configLabelName;
	
	@ManyToOne(targetEntity = Division.class)
	@JoinColumn(name = "divisionId")
	private Division division;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfigLabelName() {
		return configLabelName;
	}

	public void setConfigLabelName(String configLabelName) {
		this.configLabelName = configLabelName;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}
	
	
	
}
