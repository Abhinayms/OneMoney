package com.sevya.onemoney.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CategoryDto extends AppBaseDto {
	
	private String img;
	private String name;
	private Long parentId;
	private Long categoryId;
	private String categoryCode;
 	private String categoryName;
 	private Boolean isUserCreated;
 	private String parentCategoryCode;
	private List<CategoryDto> subCategories;
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<CategoryDto> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<CategoryDto> subCategories) {
		this.subCategories = subCategories;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getParentCategoryCode() {
		return parentCategoryCode;
	}
	public void setParentCategoryCode(String parentCategoryCode) {
		this.parentCategoryCode = parentCategoryCode;
	}
	public Boolean getIsUserCreated() {
		return isUserCreated;
	}
	public void setIsUserCreated(Boolean isUserCreated) {
		this.isUserCreated = isUserCreated;
	}
	
	
}
