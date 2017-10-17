package com.sevya.onemoney.dto;

import java.util.List;

public class CAASResponseDto extends AppBaseDto {

	private List<CAASDto> payload;
	private Integer status;
	private String message;

	public List<CAASDto> getPayload() {
		return payload;
	}
	public void setPayload(List<CAASDto> payload) {
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
