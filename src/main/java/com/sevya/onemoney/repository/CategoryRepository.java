package com.sevya.onemoney.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sevya.onemoney.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

	@Query("FROM Category category WHERE category.isActive = true AND category.id = :id ")
	public Category getCategory(@Param("id") Long id);
	
	@Query("FROM Category category WHERE category.isActive = true AND category.uuid = :uuid ")
	public Category getCategoryByUuid(@Param("uuid") String uuid);
	
	@Query("FROM Category category WHERE category.isActive = true AND category.name = :name ")
	public Category getCategoryByName(@Param("name") String name);
	
	@Query("FROM Category category WHERE category.isActive = true and category.parentId IS NULL "
		 + "AND (category.createdBy.id = :userId OR category.createdBy.id = :superAdminId)")
	public List<Category> getCategories(@Param("userId") Long userId,@Param("superAdminId") Long superAdminId);
	
	@Query("FROM Category category "
		 + "WHERE category.parentId = :parentId AND category.isActive = true "
		 + "AND (category.createdBy.id = :userId OR category.createdBy.id = :superAdminId)")
	public List<Category> getSubCategoriesByParentId(@Param("userId") Long userId,@Param("parentId") Long parentId,
																		 @Param("superAdminId") Long superAdminId);
	
	@Modifying
	@Transactional
	@Query("UPDATE Category category set category.isActive = false "
	  	 + "WHERE category.id = :id OR category.parentId = :id")
	public void deleteCategory(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Category category set category.isActive = false "
		 + "WHERE category.parentId IN (:parentIds) ")
	public void deleteSubCategoriesByParentId(@Param("parentIds") List<Long> parentIds);
	
	@Query(value = "select DATE_FORMAT(max(c.modifiedDate),'%d %b %Y %T') as modifiedDate,"
			+ " DATE_FORMAT(max(c.createdDate),'%d %b %Y %T') as createdDate, c.id "
			+ " from category c, user u where (c.modifiedBy = :userId || c.modifiedBy = :superAdminId) ", nativeQuery = true)
	public Category getLastUpdatedDateForCategory(@Param("userId") Long userId,@Param("superAdminId") Long superAdminId);

	@Query(value = "FROM Category category WHERE category.isActive = true")
	public List<Category> getAllCategoryList();
	
	@Query(value = "select id as categoryId from category c where c.isActive = true and (c.createdBy = :userId or "
				 + "c.createdBy = :superAdminId) and c.parentId = (select id from category where uuid = :parentCategoryCode) ",nativeQuery = true)
	public List<BigInteger> getSubCategoryIdsByParentCategoryId(@Param("userId") Long userId,@Param("parentCategoryCode") String parentCategoryCode,
			 															@Param("superAdminId") Long superAdminId);
	
}