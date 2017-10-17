package com.sevya.launchpad.dto;

public class DivisionDto {
	
	private Long divisionId;
	private Long entityId;
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
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	
	

}
