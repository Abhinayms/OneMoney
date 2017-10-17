package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rolepermissionmapper")
public class RolePermissionMapper extends BaseModel {

	@ManyToOne(targetEntity = Role.class)
	@JoinColumn(name = "roleId")
	private Role role;

	@ManyToOne(targetEntity = Permission.class)
	@JoinColumn(name = "permissionId")
	private Permission permission;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

}
