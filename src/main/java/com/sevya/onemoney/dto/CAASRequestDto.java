package com.sevya.onemoney.dto;

import java.util.List;

public class CAASRequestDto extends AppBaseDto {

	private List<CAASDto> data;

	public List<CAASDto> getData() {
		return data;
	}

	public void setData(List<CAASDto> data) {
		this.data = data;
	}
	
}
