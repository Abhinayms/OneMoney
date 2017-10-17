package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.Budget;
import com.sevya.onemoney.utility.Purpose;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Long> {
	
	@Query(" FROM Budget budget"
		 + " LEFT JOIN FETCH budget.category AS category"
		 + " WHERE category.isActive = true AND budget.modifiedDate IS NULL"
		 + " AND budget.isActive = true AND budget.id = :id")
	public Budget getBudget(@Param("id") Long id);
	
	@Query(" FROM Budget budget"
		 + " LEFT JOIN FETCH budget.category AS category"
		 + " WHERE category.isActive = true AND category.id = :categoryId"
		 + " AND budget.isActive = true AND budget.modifiedDate IS NULL"
		 + " AND budget.createdBy.id = :userId AND budget.purpose = :purpose")
	public Budget getBudgetByCategoryId(@Param("userId") Long userId,@Param("categoryId") Long categoryId,@Param("purpose") Purpose purpose);
	
	@Query(value = " select * from budget b,category c, user u where b.createdBy = u.id "
			 	 + " and b.categoryId = c.id and b.categoryId = :categoryId and b.createdBy = :userId "
			 	 + " and b.modifiedDate is null and b.isActive = true and b.purpose = :purpose", nativeQuery = true )
	public Budget getActiveBudgetByCategoryId(@Param("userId") Long userId,@Param("categoryId") Long categoryId,@Param("purpose") String purpose);
	
	@Query(" FROM Budget budget"
		 + " LEFT JOIN FETCH budget.category AS category"
		 + " WHERE category.isActive = true AND category.uuid = :categoryCode"
		 + " AND budget.createdBy.id = :userId AND budget.isActive = true AND budget.modifiedDate IS NULL")
	public Budget getBudgetByCategoryCode(@Param("userId") Long userId,@Param("categoryCode") String categoryCode);
	
	@Query(" FROM Budget budget"
			 + " LEFT JOIN FETCH budget.category AS category"
			 + " WHERE category.isActive = true AND category.uuid = :categoryCode"
			 + " AND budget.createdBy.id = :userId AND budget.isActive = true AND budget.budgetedAmount IS NULL")
	public Budget getParentBudgetByCategoryCode(@Param("userId") Long userId,@Param("categoryCode") String categoryCode);
	
	@Modifying
	@Transactional
	@Query("UPDATE Budget budget set budget.modifiedDate = now() WHERE budget.id = :id")
	public void deleteBudget(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Budget budget set budget.modifiedDate = now() WHERE budget.id IN (:budgetIds)")
	public void deleteListOfBudgets(@Param("budgetIds") List<Long> budgetIds);
	
	@Modifying
	@Transactional
	@Query("UPDATE Budget budget set budget.modifiedDate = now() WHERE budget.id = :id")
	public void updateAndDeleteBudgetByBudgetId(@Param("id") Long id);
	
	@Query(value = " select b.* from budget b, category c where c.id = b.categoryId "
				+  " and c.parentId IS NOT NULL and b.createdBy = :userId "
				+  " and (b.modifiedDate > :lastDateOfMonth || b.modifiedDate is null) "
				+  " and b.createdDate < :nextMonthFirst and b.categoryId = :categoryId and b.isActive = 1 ", nativeQuery = true )
	public Budget getBudgetForSubCategoryByCatagoryIdAndDates(@Param("userId") Long userId,
			@Param("categoryId") Long categoryId,@Param("lastDateOfMonth") String lastDateOfMonth,
			@Param("nextMonthFirst") String nextMonthFirst);
	
	@Query(value = " select b.*,c.* from budget b, category c where c.id = b.categoryId "
				 + " and c.parentId IS NULL and b.createdBy = :userId "
				 + " and (b.modifiedDate > :lastDateOfMonth || b.modifiedDate is null) "
				 + " and b.createdDate < :nextMonthFirst and b.isActive = 1 ", nativeQuery = true )
	public List<Budget> getBudgets(@Param("userId") Long userId,@Param("lastDateOfMonth") String lastDateOfMonth,
								   @Param("nextMonthFirst") String nextMonthFirst);
	
	@Query(value = " select b.*,c.* from budget b, category c where c.id = b.categoryId "
			 + " and c.parentId IS NULL and b.createdBy = :userId "
			 + " and (b.modifiedDate > :lastDateOfMonth || b.modifiedDate is null) "
			 + " and b.createdDate < :nextMonthFirst and b.purpose = :purpose and b.isActive = 1 ", nativeQuery = true )
	public List<Budget> getBudgetsForSummary(@Param("userId") Long userId,@Param("lastDateOfMonth") String lastDateOfMonth,
							   @Param("nextMonthFirst") String nextMonthFirst,@Param("purpose") String purpose);
	
	@Query(value = " select b.* from budget b,user u where b.categoryId = (select c.parentId from category c "
				 + " where c.id = :subCategoryId) and b.modifiedDate is null and b.createdBy = u.id "
				 + " and b.createdBy = :userId order by b.id limit 1", nativeQuery = true)
	public Budget getParentBudgetBySubCategoryId(@Param("userId") Long userId,@Param("subCategoryId") Long subCategoryId);
	
	@Query(value = " select b.* from budget b,user u where b.categoryId = :categoryId "
			 	 + " and b.modifiedDate is null and b.createdBy = u.id "
			 	 + " and b.createdBy = :userId order by b.id limit 1", nativeQuery = true)
	public Budget getParentBudgetByCategoryId(@Param("userId") Long userId,@Param("categoryId") Long categoryId);
	
	@Query(value = " select SUM(budgetedAmount) from budget b"
			 + " where b.createdBy = :userId and b.isActive =true"
			 + " and MONTH(b.createdDate) =:month and YEAR(b.createdDate) =:year", nativeQuery = true )
	public Float getAllBudgetsByUserMonthAndYear(@Param("userId") Long userId,@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = "select b.* from budget b,user u where b.categoryId IN "
			+ " (select id from category where parentId = :categoryId) and "
			+ " b.modifiedDate is null and b.createdBy = u.id and "
			+ " b.createdBy = :userId and b.purpose = :purpose order by b.id", nativeQuery = true )
	public List<Budget> getSubBudgetsByParentCategoryId(@Param("userId") Long userId,@Param("categoryId") Long categoryId,@Param("purpose") String purpose);
	
	@Query(value = "select b.* from budget b,user u where b.categoryId IN "
			+ " (select id from category where parentId = :categoryId) and "
			+ " b.modifiedDate is null and b.createdBy = u.id  and "
			+ " b.createdBy = u.id and b.createdBy = :userId and b.id not in (:budgetsIds) order by b.id", nativeQuery = true )
	public List<Budget> getSubBudgetsByParentCategoryIdAndNotInBudgetIds(@Param("userId") Long userId,
			@Param("categoryId") Long categoryId,@Param("budgetsIds") List<Long> budgetsIds);
	
	@Query(value = "select b.*,c.* from budget b, user u, category c where b.categoryId = "
			+ " (select parentId from category where  id = (select categoryId from budget "
			+ " where id = :budgetsId)) and b.modifiedDate is null  and b.createdBy = :userId "
			+ " and b.createdBy = u.id and b.categoryId = c.id and b.isActive = true", nativeQuery = true)
	public Budget getParentBudgetbyChildBudgetId(@Param("userId") Long userId,@Param("budgetsId") Long budgetsId);
	
	@Query(value = "select * from budget b,user u,category c where b.categoryId = c.id and b.createdBy = u.id "
			+ " and b.categoryId in (select id from category where parentId = :parentCategoryId) and  b.createdBy = :userId "
			+ " and b.modifiedDate is null and b.isActive = true", nativeQuery = true)
	public List<Budget> getChildBudgetsbyParentCategoryId(@Param("userId") Long userId,@Param("parentCategoryId") Long parentCategoryId);
	
	@Query(value = "  select b.*,c.* from budget b, category c where c.id = b.categoryId "
				 + " and c.parentId IS NULL and b.createdBy = :userId "
				 + " and (b.modifiedDate > :lastDateOfMonth || b.modifiedDate is null) "
				 + " and b.createdDate < :nextMonthFirst and c.name = :categoryName "
				 + " and b.isActive = true", nativeQuery = true)
	public Budget getTotalIncomeBudgeted(@Param("userId") Long userId,@Param("categoryName") String categoryName,
			@Param("lastDateOfMonth") String lastDateOfMonth, @Param("nextMonthFirst") String nextMonthFirst);
	
	@Query(value = "select sum(budgetedAmount) from budget b,category c,user u where b.createdBy = u.id "
			+ " and b.categoryId = c.id and b.createdBy = :userId and b.modifiedDate is null and b.categoryId "
			+ " not in (select id from category where parentId = (select id from category where name = :categoryName) "
			+ " or name = :categoryName ) and month(b.createdDate) = :month and year(b.createdDate) = :year and b.isActive = true ", nativeQuery = true)
	public Float getTotalBudgetedAmountExcludingIncomeCategory(@Param("userId") Long userId,@Param("categoryName") String categoryName,  
			@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = "select * from budget b,user u,category c where b.categoryId = c.id and b.createdBy = u.id "
			+ " and b.categoryId in (select id from category where parentId = :parentCategoryId) and  b.createdBy = :userId"
			+ " and b.purpose=:purpose and b.modifiedDate is null and b.isActive = true", nativeQuery = true)
	public List<Budget> getChildBudgetsbyPurposeAndParentCategoryId(@Param("userId") Long userId, @Param("purpose") String purpose, 
			@Param("parentCategoryId") Long parentCategoryId);
	

}
