package com.sevya.launchpad.dto;

public class EntityDto {
	
	private Long entityId;
	private Long orgId;
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
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	
}
