package com.sevya.launchpad.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
public class Role extends BaseModel {

	private String code;
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	private Set<UserRoleMapper> userRoleMappers;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	private Set<RolePermissionMapper> rolePermissionMappers;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
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
	public Set<UserRoleMapper> getUserRoleMappers() {
		return userRoleMappers;
	}
	public void setUserRoleMappers(Set<UserRoleMapper> userRoleMappers) {
		this.userRoleMappers = userRoleMappers;
	}
	
	public Set<RolePermissionMapper> getRolePermissionMappers() {
		return rolePermissionMappers;
	}
	public void setRolePermissionMappers(
			Set<RolePermissionMapper> rolePermissionMappers) {
		this.rolePermissionMappers = rolePermissionMappers;
	}
	public Set<GroupRoleMapper> getGroupRoleMappers() {
		return groupRoleMappers;
	}
	public void setGroupRoleMappers(Set<GroupRoleMapper> groupRoleMappers) {
		this.groupRoleMappers = groupRoleMappers;
	}	
}
