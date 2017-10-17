package com.sevya.launchpad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.context.annotation.Scope;


@Entity
@Table(name = "apicredentials")
@Scope("session")
public class ApiCredentials extends BaseModel {
	
	
	@Column(name = "apiId",unique = true)
	private String apiId;
	
	@Column(name = "apiSecret",unique = true)
	private String apiSecret;
	
	@Column(name = "isEnable")
	private boolean isEnable;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}
