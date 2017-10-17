package com.sevya.launchpad.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class Group extends BaseModel {
	
	private String code;
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId")
	private Set<GroupRoleMapper> groupRoleMappers;
	
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
	public Set<GroupRoleMapper> getGroupRoleMappers() {
		return groupRoleMappers;
	}
	public void setGroupRoleMappers(Set<GroupRoleMapper> groupRoleMappers) {
		this.groupRoleMappers = groupRoleMappers;
	}
	

}
