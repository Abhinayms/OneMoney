package com.sevya.launchpad.dto;

public class AuthenticationDto {

	private String email;
	private String password;
	private String ipAddress;
	
	private UserRequestsDto userRequests;
	private Integer loginAttempts;
	
	
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
	public UserRequestsDto getUserRequests() {
		return userRequests;
	}
	public void setUserRequests(UserRequestsDto userRequests) {
		this.userRequests = userRequests;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Integer getLoginAttempts() {
		return loginAttempts;
	}
	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	
	
}
