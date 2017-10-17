package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sgintegration")
public class SgIntegration extends BaseModel {
	
	private String endPoint;
	private String email;
	private String password;
	private String appId;
	private String appSecret;
	
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
}
