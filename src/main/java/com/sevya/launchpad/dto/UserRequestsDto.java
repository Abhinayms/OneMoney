package com.sevya.launchpad.dto;

public class UserRequestsDto {
	
	private String requestKey;	
	private Integer userId;
	private boolean isComplete;
	private String requestingIPAddress;
	private String lifeOfRequest;
	private boolean requestType;
	
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public boolean isComplete() {
		return isComplete;
	}
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	public String getRequestingIPAddress() {
		return requestingIPAddress;
	}
	public void setRequestingIPAddress(String requestingIPAddress) {
		this.requestingIPAddress = requestingIPAddress;
	}
	public String getLifeOfRequest() {
		return lifeOfRequest;
	}
	public void setLifeOfRequest(String lifeOfRequest) {
		this.lifeOfRequest = lifeOfRequest;
	}
	public boolean isRequestType() {
		return requestType;
	}
	public void setRequestType(boolean requestType) {
		this.requestType = requestType;
	}
	
	
	
	
}
