package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "grouprolemapper")
public class GroupRoleMapper extends BaseModel {

	@ManyToOne(targetEntity = Group.class)
	@JoinColumn(name = "groupId")
	private Group group;

	@ManyToOne(targetEntity = Role.class)
	@JoinColumn(name = "roleId")
	private Role role;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
