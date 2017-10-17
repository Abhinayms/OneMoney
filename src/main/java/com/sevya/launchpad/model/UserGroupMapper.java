package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usergroupmapper")
public class UserGroupMapper extends BaseModel {

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(targetEntity = Group.class)
	@JoinColumn(name = "groupId")
	private Group group;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	
	
}
