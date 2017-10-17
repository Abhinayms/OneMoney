package com.sevya.onemoney.dto;

public class ValuesDto extends AppBaseDto {
	
	private String label;
	private Float value;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}

	
}
