package com.sevya.onemoney.service;

import java.math.BigInteger;
import java.util.List;

import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.model.Category;

public interface CategoryService {

	/**
	 * Get Category
	 * @param Id 
	 * @return Catagory
	 * */
	public Category getCategoryById(Long id);
	
	/**
	 * Get Category
	 * @param code
	 * @return Catagory
	 * */
	public Category getCategoryByUuid(String code);
	
	/**
	 * Get All Categories
	 * @param null 
	 * @return List<CatagoryDto>
	 * */
	public ResponseDto getCategories(User user) throws Exception;
	
	/**
	 * Create new Category
	 * @param CatagoryDto 
	 * @return CatagoryDto
	 * */
	public CategoryDto addCategory(CategoryDto dto);
	
	/**
	 * Edit Category
	 * @param CatagoryDto 
	 * @return CatagoryDto
	 * */
	public void updateCategory(CategoryDto dto);

	/**
	 * Delete Category
	 * @param uuid 
	 * @return String true if deleted else exception message
	 * */
	public void deleteCategoryByUuid(String uuid);
	
	/**
	 * Get Sub Category
	 * @param categoryId 
	 * @return Catagory
	 * */
	public List<Category> getSubCategoriesByCategoryId(User user,Long categoryId);
	
	/**
	 * Create new sub Category
	 * @param CatagoryDto 
	 * @return CatagoryDto
	 * */
	public CategoryDto addSubCategory(User user, CategoryDto dto) throws Exception;
	
	/**
	 * Edit sub Category
	 * @param CatagoryDto 
	 * @return CatagoryDto
	 * */
	public CategoryDto updateSubCategory(CategoryDto dto) throws Exception;
	
	public Category getCategoryByName(String name);
	
	public List<BigInteger> getSubCategoryIdsByParentCategoryId(User user,String categoryId);
	
	public Float getTotalAmountForCategory(Long userId, String categoryName, Integer month,Integer year);

	public Float getTotalSpentForCurrentMonthByCategoryName(Long userId,String categoryName,Integer month,Integer year);
	
	public Float getAverageSpentForLastThreeMonthsByCategoryName(Long userId,String categoryName,String pastDate,String currentDate);
	
	public Float getTotalSpentForCurrentMonthByCategoryId(Long userId ,Long categoryId,String categoryName,Integer month,Integer year);
	
	public Float getAverageSpentForLastThreeMonthsByCategoryId(Long userId,Long categoryId,String categoryName,String pastDate,String currentDate);
	
	public List<Object[]> getTotalIncomingAndTotalOutgoingAmountsForAnAccountOfUser(Long userId,String accountId,Integer month,Integer year);

	public List<Category> getAllCategoryList();

	public Float totalSpentsForUserByPurposeCategoryMonthAndYear(Long userId, String purpose, String categoryName, Integer month, Integer year);

	public Integer getUnCategorizedTransactionsCount(Long userId, String purpose, Integer month, Integer year);
	
	public boolean deleteCategoryTransactionMappersByUserIdAndAccountId(Long UserId,Long AccountId);
	
	public List<Object[]> getUnBudgedCategoriesByUserIdAndMonthAndYear(Long userId,String purpose,Integer month, Integer year);
	
	public BigInteger getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose(Long userId,Long categoryId,String purpose,Integer month, Integer year);
	
	public List<Object[]> getUnBudgedSubCategoriesByUserIdAndMonthAndYearAndSubCatagoryIds(Long userId,String purpose,List<Long> subCategoryIds,Integer month, Integer year);
	
}
