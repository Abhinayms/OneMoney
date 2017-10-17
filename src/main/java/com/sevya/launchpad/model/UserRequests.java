package com.sevya.launchpad.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "userrequests")
public class UserRequests extends BaseModel {
	
	private String requestKey;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "userId")
	private User user;
	
	@Column(columnDefinition="tinyint(1) default 1")
	private Boolean isComplete = false;
	
	private String requestingIPAddress;
	
	private Date lifeOfRequest;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum ('activation','forgotPassword')", nullable = true)
	private RequestType requestType;
	
	public enum RequestType{
		activation,forgotPassword
	}
	

	public RequestType getRequestType() {
		return requestType;
	}
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	public String getRequestingIPAddress() {
		return requestingIPAddress;
	}
	public void setRequestingIPAddress(String requestingIPAddress) {
		this.requestingIPAddress = requestingIPAddress;
	}
	public Date getLifeOfRequest() {
		return lifeOfRequest;
	}
	public void setLifeOfRequest(Date lifeOfRequest) {
		this.lifeOfRequest = lifeOfRequest;
	}
	

	
}
