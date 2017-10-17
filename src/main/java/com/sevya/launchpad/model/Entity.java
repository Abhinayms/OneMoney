package com.sevya.launchpad.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "entity")
public class Entity extends BaseModel {

	private String code;
	private String name;
	private String configLabelName;
	
	@ManyToOne(targetEntity = Organisation.class)
	@JoinColumn(name = "organisationId")
	private Organisation organisation;
	
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
	public Organisation getOrganisation() {
		return organisation;
	}
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	

	
}
