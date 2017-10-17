package com.sevya.launchpad.dto;

public class DepartmentDto {
	
	private Long departmentId;
	private Long divisionId;
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
	public Long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
}
