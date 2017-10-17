package com.sevya.onemoney.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.model.Category;

public class CategoryDtoMapper {
	
	private CategoryDtoMapper() {}
	
	public static Category toCategory(Category catagory,CategoryDto categoryDto){
		catagory.setName(categoryDto.getCategoryName()!=null ? categoryDto.getCategoryName() : catagory.getName());
		catagory.setParentId(catagory.getParentId() != null ? catagory.getParentId() : categoryDto.getParentId() == null ? null : categoryDto.getParentId());
		return catagory;
	}
	
	public static CategoryDto toCategoryDto(CategoryDto categoryDto,Category category,List<Category> subCategories) throws Exception{
		categoryDto.setCategoryCode(category.getUuid());
		categoryDto.setCategoryName(category.getName());
		categoryDto.setSubCategories(subCategories == null ? null : toSubCategoryDtos(subCategories));
		return categoryDto;
	}
	
	public static List<CategoryDto> toCategoryDtos(List<Category> catagories,List<Category> subCatagories){
		List<CategoryDto> dtos = new ArrayList<>();
		catagories.forEach(category -> {
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setId(category.getId());
			categoryDto.setCategoryCode(category.getUuid());
			categoryDto.setCategoryName(category.getName());
			categoryDto.setParentId(category.getParentId() == null ? null : category.getParentId());
			categoryDto.setSubCategories(subCatagories == null ? null : toSubCategoryDtos(subCatagories));
			dtos.add(categoryDto);
		});
		return dtos;
	}
	
	public static List<CategoryDto> toSubCategoryDtos(List<Category> catagories){
		List<CategoryDto> dtos = new ArrayList<>();
		catagories.forEach(category -> {
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setIsUserCreated(category.getUserCreated());
			categoryDto.setCategoryCode(category.getUuid());
			categoryDto.setCategoryName(category.getName());
			dtos.add(categoryDto);
		});
		return dtos;
	}
	
}
