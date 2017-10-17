package com.sevya.onemoney.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sevya.launchpad.model.BaseModel;

@Entity
@Table(name = "category")
public class Category extends BaseModel {
	
	private String name;	
	private Long parentId;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private Boolean isUserCreated = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Boolean getUserCreated() {
		return isUserCreated;
	}
	public void setUserCreated(Boolean isUserCreated) {
		this.isUserCreated = isUserCreated;
	}
	
}
