package com.sevya.launchpad.dto;

import java.util.Map;

public class MailDto {
	
	private Long userId;			//Mandatory*
	private String email;
	private String baseUrl;			//Mandatory*
	private String accessKey;		//Mandatory*
	private String replyTo;
	private String ccMails;
	private Integer templateId;		//Mandatory*
	Map <String,String> variables;	//Mandatory*
	private String code;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public String getCcMails() {
		return ccMails;
	}
	public void setCcMails(String ccMails) {
		this.ccMails = ccMails;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public Map<String, String> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	

	
}
