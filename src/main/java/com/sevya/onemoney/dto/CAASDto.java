package com.sevya.onemoney.dto;

import java.util.Map;

public class CAASDto extends AppBaseDto {

	private String transaction_id;
	private String description;
	private Integer timestamp;
	private Boolean api_categorised;
	private Boolean consumer_categorised;
	private Boolean one_time_categorised;
	private String category_id;
	private Map<String,String> metadata;
	private Map<String,String> data;
	private String parent_category_id;
	private String name;

	
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
	public Boolean getApi_categorised() {
		return api_categorised;
	}
	public void setApi_categorised(Boolean api_categorised) {
		this.api_categorised = api_categorised;
	}
	public Boolean getConsumer_categorised() {
		return consumer_categorised;
	}
	public void setConsumer_categorised(Boolean consumer_categorised) {
		this.consumer_categorised = consumer_categorised;
	}
	public Boolean getOne_time_categorised() {
		return one_time_categorised;
	}
	public void setOne_time_categorised(Boolean one_time_categorised) {
		this.one_time_categorised = one_time_categorised;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public Map<String, String> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public String getParent_category_id() {
		return parent_category_id;
	}
	public void setParent_category_id(String parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
