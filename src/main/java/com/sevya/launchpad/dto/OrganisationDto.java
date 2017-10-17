package com.sevya.launchpad.dto;

public class OrganisationDto {

	private Long orgId;
	private String code;
	private String name;
	private String configLabelName;
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getConfigLabelName() {
		return configLabelName;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
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
}
