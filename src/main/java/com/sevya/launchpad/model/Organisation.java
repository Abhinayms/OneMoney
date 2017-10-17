package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organisation")
public class Organisation extends BaseModel {

	private String code;
	private String name;
	private String configLabelName;
	
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
	@Override
	public String toString() {
		return "Organisation [code=" + code + ", name=" + name + ", configLabelName=" + configLabelName + "]";
	}
	
	
	
}
