package com.sevya.launchpad.dto;

import java.util.List;
import java.util.Map;

public class PaginationDto<T> {

	private List<T> content;
	private Map<String,String> metadata;
	
	private int offset;
	private int limit;
	private List<String> fields;
	private String sort;
	private String criteria;
	
	
	public PaginationDto(){
		super();
	}
/*	public PaginationDto(Page<T> page,List contents,PaginationDto<T> selfData) {
		this.setContent(contents);
		this.metadata = new HashMap();
		this.offset =selfData.getOffset();
		this.limit =selfData.getLimit();
		this.criteria =selfData.getCriteria();
		this.sort =selfData.getSort();
		this.fields =selfData.getFields();
		
	}*/
		
	
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}



	public Map<String, String> getMetadata() {
		return metadata;
	}



	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	
	
}
