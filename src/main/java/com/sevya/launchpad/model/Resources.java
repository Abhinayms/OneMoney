package com.sevya.launchpad.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "resources")
public class Resources extends BaseModel {
	
	private String filename;
	private String location;
	private boolean resourceType;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isResourceType() {
		return resourceType;
	}
	public void setResourceType(boolean resourceType) {
		this.resourceType = resourceType;
	}

}
