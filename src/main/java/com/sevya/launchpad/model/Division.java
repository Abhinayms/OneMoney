package com.sevya.launchpad.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "division")
public class Division extends BaseModel {

	private String code;
	private String name;
	private String configLabelName;
	
	@ManyToOne(targetEntity = Entity.class)
	@JoinColumn(name = "entityId")
	private Entity entity;

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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	
}
