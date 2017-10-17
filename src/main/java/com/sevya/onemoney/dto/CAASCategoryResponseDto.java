package com.sevya.onemoney.dto;

public class CAASCategoryResponseDto {

	private CAASDto payload;
	private Integer status;
	private String message;

	public CAASDto getPayload() {
		return payload;
	}
	public void setPayload(CAASDto payload) {
		this.payload = payload;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
