package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "userrolemapper")
public class UserRoleMapper extends BaseModel {

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(targetEntity = Role.class)
	@JoinColumn(name = "roleId")
	private Role role;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
