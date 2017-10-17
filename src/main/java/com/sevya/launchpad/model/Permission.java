package com.sevya.launchpad.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission extends BaseModel {
	
	
	private String code;
	private String name;
	private String urlPattern;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "permissionId")
	private Set<RolePermissionMapper> rolePermissionMappers;
	
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
	public String getUrlPattern() {
		return urlPattern;
	}
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	public Set<RolePermissionMapper> getRolePermissionMappers() {
		return rolePermissionMappers;
	}
	public void setRolePermissionMappers(
			Set<RolePermissionMapper> rolePermissionMappers) {
		this.rolePermissionMappers = rolePermissionMappers;
	}
	
}
