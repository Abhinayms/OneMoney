package com.sevya.onemoney.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.utility.Purpose;

@Repository
public interface CategoryTransactionMapperRepository extends CrudRepository<CategoryTransactionMapper,Long> ,JpaSpecificationExecutor<CategoryTransactionMapper> {
	
	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.createdBy AS user"
			 + " LEFT JOIN FETCH mapper.transaction AS transaction"
			 + " WHERE mapper.account.id = :accountId AND user.id = :userId"
			 + " AND mapper.isActive = true AND user.isActive = true AND transaction.isActive=true")
	public List<CategoryTransactionMapper> getCategoryTransactionMapperByUserIdAndAccountId(
			@Param("userId") Long userId,@Param("accountId") Long accountId);
	
	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.category AS category"
			 + " WHERE category.id = :categoryId AND mapper.createdBy.id = :userId"
			 + " AND category.isActive = true")
	public List<CategoryTransactionMapper> getCategoryTransactionMapperByCategoryIdAndUserId(
			@Param("categoryId") Long categoryId,@Param("userId") Long userId);
	
	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.category AS category"
			 + " LEFT JOIN FETCH mapper.transaction AS transaction"
			 + " WHERE category.id = :categoryId AND mapper.createdBy.id = :userId"
			 + " AND MONTH(transaction.transactionDate) = :presentMonth "
			 + " AND YEAR(transaction.transactionDate) = :presentYear"
			 + " AND category.isActive = true AND mapper.isActive = true ")
	public List<CategoryTransactionMapper> getCategoryTransactionMapperByCategoryIdUserIdMonthAndYear(
			@Param("categoryId") Long categoryId,@Param("userId") Long userId,
			@Param("presentMonth") Integer presentMonth,@Param("presentYear") Integer presentYear);
	
	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.category AS category"
			 + " LEFT JOIN FETCH mapper.transaction AS transaction"
			 + " WHERE mapper.isActive = true AND transaction.fingerprint =:fingerprint "
			 + " AND transaction.isActive=true AND transaction.createdBy.id =:userId" )
	public CategoryTransactionMapper getCategoryTransactionMapperByUserIdAndTxFingerPrint(
								@Param("userId")Long userId,@Param("fingerprint")String fingerprint);
	
	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.account AS account"
			 + " LEFT JOIN FETCH mapper.category AS category"
			 + " LEFT JOIN FETCH mapper.transaction AS transaction"
			 + " WHERE mapper.isActive = true AND mapper.createdBy.id = :userId"
			 + " AND MONTH(transaction.transactionDate) = :month AND YEAR(transaction.transactionDate) = :year"
			 + " AND account.id = :accountId AND account.isActive = true AND mapper.isActive = true ORDER BY transaction.id DESC ")
	public List<CategoryTransactionMapper> getCategoryTransactionMapperForLast3TransactionsByAccountIdAndUserIdAndMonthAndYear(
														@Param("userId") Long userId,@Param("accountId") Long accountId,
														@Param("month") Integer month,@Param("year") Integer year,Pageable pageable);
	
	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.account AS account"
			 + " LEFT JOIN FETCH mapper.category AS category"
			 + " LEFT JOIN FETCH mapper.transaction AS transaction"
			 + " WHERE mapper.isActive = true AND mapper.createdBy.id = :userId"
			 + " AND MONTH(mapper.createdDate) = :month AND YEAR(mapper.createdDate) = :year ORDER BY mapper.id DESC")
	public List<CategoryTransactionMapper> getUserTransactionsByMonthYearAndCount(@Param("userId") Long userId, 
			@Param("month") Integer month,@Param("year") Integer year,Pageable pageable);

	@Query(" FROM CategoryTransactionMapper mapper"
			 + " LEFT JOIN FETCH mapper.account AS account"
			 + " LEFT JOIN FETCH mapper.category AS category"
			 + " LEFT JOIN FETCH mapper.transaction AS transaction"
			 + " WHERE mapper.isActive = true AND mapper.category IS NOT NULL AND transaction.isActive = true "
			 + " AND account.purpose=:purpose AND transaction.amount < 0 AND mapper.createdBy.id = :userId AND account.isActive = true "
			 + " AND MONTH(transaction.transactionDate) = :month AND YEAR(transaction.transactionDate) = :year ORDER BY transaction.transactionDate DESC")
	public List<CategoryTransactionMapper> getUserSpentCategoryTransactionsByAccountTypeMonthYear(@Param("userId") Long userId,
			    @Param("purpose") Purpose purpose,@Param("month") Integer month,@Param("year") Integer year);

	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ctm,transaction t,user u, category c "
			 	 + " where ctm.createdBy = u.id and t.id = ctm.transactionId and ctm.createdBy = :userId "
			 	 + " and ctm.categoryId = c.id and ctm.categoryId not in (select id from category where "
			 	 + " parentId in (select id from category where c.name = :categoryName) or name = :categoryName) "
			 	 + " and ctm.isActive = true and month(t.transactionDate) = :month and year(t.transactionDate) = :year and ctm.categoryId = :categoryId ",nativeQuery=true)
	public Float getTotalSpentForCurrentMonthByUserIdAndCategoryIdAndMonthAndYear(@Param("userId") Long userId,@Param("categoryId") Long categoryId,@Param("categoryName") String categoryName, 
														  @Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ctm, "
				 + " transaction t, category c where ctm.createdBy = :userId and t.id = ctm.transactionId and "
				 + " ctm.categoryId = c.id and ctm.categoryId not in (select id from category where parentId in "
				 + " (select id from category where name = :categoryName) or name = :categoryName) and "
				 + " ctm.isActive = true and t.transactionDate between :pastMonth and :presentMonth and ctm.categoryId = :categoryId ",nativeQuery=true)
	public Float getAverageSpentForLastThreeMonthsByUserIdAndCategoryIdAndMonthAndYear(@Param("userId") Long userId,@Param("categoryId") Long categoryId,
			@Param("categoryName") String categoryName,@Param("pastMonth") String pastMonth,@Param("presentMonth") String presentMonth);
	
	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ctm,transaction t,user u, category c "
		 	 	 + " where ctm.createdBy = u.id and t.id = ctm.transactionId and ctm.createdBy = :userId "
		 	 	 + " and ctm.categoryId = c.id and ctm.categoryId not in (select id from category where "
		 	 	 + " parentId in (select id from category where c.name = :categoryName) or name = :categoryName) "
		 	 	 + " and ctm.isActive = true and month(t.transactionDate) = :month and year(t.transactionDate) = :year ",nativeQuery=true)
	public Float getTotalSpentForUserByMonthByUserIdAndCategoryNameAndMonthAndYear(@Param("userId") Long userId,@Param("categoryName") String categoryName, 
														  @Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ctm, "
				 + " transaction t, category c where ctm.createdBy = :userId and t.id = ctm.transactionId and "
				 + " ctm.categoryId = c.id and ctm.categoryId not in (select id from category where parentId in "
				 + " (select id from category where name = :categoryName) or name = :categoryName) and "
				 + " ctm.isActive = true and t.transactionDate between :pastMonth and :presentMonth  ",nativeQuery=true)
	public Float getAverageSpentByUserLastThreeMonthsByUserIdAndCategoryNameAndMonthAndYear(@Param("userId") Long userId,
			@Param("categoryName") String categoryName,@Param("pastMonth") String pastMonth,@Param("presentMonth") String presentMonth);
	
	@Query(value = " select sum(case when t.amount > 0 then t.amount else 0 end) from categorytransactionmapper ct, "
				 + " transaction t, user u, category c where ct.categoryId in (select id from category where "
				 + " parentId = (select id from category where name= :categoryName) or c.name = :categoryName) "
				 + " and ct.createdBy = :userId and month(t.transactionDate) = :month and year(t.transactionDate) = :year "
				 + " and ct.createdBy = u.id and t.id = ct.transactionId and ct.categoryId = c.id and ct.isActive = true", nativeQuery = true)
	public Float getToatalAmountForCategory(@Param("userId") Long userId,@Param("categoryName") String categoryName, 
			@Param("month") Integer month,@Param("year") Integer year);

	@Query(value = " select count(*) from categorytransactionmapper ctm, transaction t, account a,accounttype at,user u, category c "
				 + " where a.id = ctm.accountId and t.id = ctm.transactionId and c.id = ctm.categoryId and u.id = ctm.createdBy "
				 + " and a.accountTypeId = at.id and at.name = :accountType and a.purpose = :purpose and a.id = :accountId and u.id = :userId "
				 + " and month(ctm.createdDate) = :month and year(ctm.createdDate) = :year and ctm.isActive = true", nativeQuery = true)
	public Integer getCountOfCurrentMonthTransactionsByAccountIdAndUserIdAndMonthAndYear(@Param("userId") Long userId,
						 @Param("accountId") Long accountId,@Param("month") Integer month,@Param("year") Integer year,
						 @Param("accountType") String accountType,@Param("purpose") String purpose);

	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ct, "
				 + " transaction t, user u, category c, account a where ct.categoryId in (select id from category where "
				 + " parentId = (select id from category where name= :categoryName) or c.name = :categoryName ) and "
				 + " ct.createdBy = :userId and a.purpose=:purpose and month(t.transactionDate) = :month and "
				 + " year(t.transactionDate) = :year and ct.createdBy = u.id and t.id = ct.transactionId and ct.categoryId = c.id "
				 + " and ct.accountId = a.id and ct.isActive = true", nativeQuery = true)
	public Float getTotalSpentsForUserByPurposeCategoryMonthAndYear(@Param("userId") Long userId,@Param("purpose") String purpose,
							@Param("categoryName") String categoryName,@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select count(ctm.id) transactions from categorytransactionmapper ctm, transaction t , user u where ctm.accountId in "
				 + " (select id from account where createdBy = :userId and isActive = true and purpose = :purpose) and categoryId is null "
				 + " and ctm.transactionId = t.id and ctm.createdBy = u.id and ctm.isActive = true and month(t.transactionDate) = :month "
				 + " and year(t.transactionDate) = :year and t.isActive = true ", nativeQuery = true)
	public Integer getUnCategorizedTransactionsCount(@Param("userId") Long userId,@Param("purpose") String purpose,
														@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select sum(case when t.amount >= 0 then t.amount else 0 end) as credits, "
				 + " sum(case when t.amount < 0 then t.amount else 0 end) as debits "
				 + " from categorytransactionmapper ctm, account a, user u, transaction t "
				 + " where ctm.createdBy = u.id and ctm.transactionId = t.id and ctm.accountId = a.id "
				 + " and t.accountCode = :accountId and u.id = :userId and month(t.transactionDate) = :month "
				 + " and year(t.transactionDate) = :year and ctm.isActive = 1 and a.isActive = 1 ORDER BY t.transactionDate DESC ", nativeQuery = true)
	public List<Object[]> getTotalIncomingAndTotalOutgoingByUserIdAndAccountCodeAndMonthAndYear(@Param("userId") Long userId,
							 @Param("accountId") String accountId,@Param("month") Integer month,@Param("year") Integer year);

	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ct, "
			 	 + " transaction t, user u, category c, account a where ct.transactionId=t.id and ct.categoryId=c.id and "
			 	 + " ct.categoryId in (select id from category where parentId =:categoryId or c.id =:categoryId) "
			 	 + " and ct.accountId=a.id and a.purpose=:purpose and ct.createdBy=:userId and ct.isActive = true", nativeQuery = true)
	public Float getTotalExpensesOfUserByCategoryIdandPurpose(@Param("userId") Long userId,@Param("categoryId") Long categoryId, @Param("purpose") String purpose);
	
	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) from categorytransactionmapper ct, "
				 + " transaction t, user u, category c, account a where ct.transactionId=t.id and ct.categoryId=c.id and "
				 + " ct.categoryId in (select id from category where parentId =:categoryId or c.id =:categoryId) "
				 + " and ct.accountId=a.id and a.purpose=:purpose and ct.createdBy=:userId and ct.isActive = true "
				 + " and month(ct.createdDate) = :month and year(ct.createdDate) = :year", nativeQuery = true)
	public Float getTotalExpensesOfUserByCategoryIdPurposeMonthAndYear(@Param("userId") Long userId,@Param("categoryId") Long categoryId, 
											 @Param("purpose") String purpose,@Param("month") Integer month,@Param("year") Integer year);

	@Query(value = " select sum(case when t.amount < 0 then t.amount else 0 end) amount from categorytransactionmapper ctm,category c,"
				 + " account a, transaction t where ctm.transactionId = t.id and ctm.createdBy = :userId and ctm.isActive = 1 and ctm.categoryId in "
				 + " (select id from category where parentId = :categoryId or c.id = :categoryId) and t.isActive = 1 and month(t.transactionDate) = :month "
				 + " and year(t.transactionDate) = :year and ctm.categoryId = c.id and ctm.accountId = a.id and a.purpose = :purpose and a.isActive = 1", nativeQuery = true)
	public BigInteger getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose(@Param("userId") Long userId,@Param("categoryId") Long categoryId,
			 											@Param("purpose") String purpose,@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select c.name as name,c.uuid as code,c.id as catagoryId,c.parentId as parentId from categorytransactionmapper ctm,account a, "
				 + " transaction t,category c where a.purpose = :purpose and a.id = ctm.accountId and t.id = ctm.transactionId and ctm.categoryId = c.id "
				 + " and ctm.createdBy = :userId and ctm.categoryId not in (select b.categoryId from budget b, category c where b.categoryId = c.id and "
				 + " b.createdBy = :userId and b.purpose = :purpose and b.modifiedDate is null) and ctm.isActive = true and t.isActive = true and month(ctm.createdDate) = :month "
				 + " and  ctm.categoryId not in (select id from category where parentId = (select id from category where name = :categoryName) or c.name = :categoryName)"
				 + " and year(ctm.createdDate) = :year GROUP BY c.id ORDER BY ctm.id DESC ", nativeQuery = true)
	public List<Object[]> getUnBudgedCategoriesByUserIdAndMonthAndYear(@Param("userId") Long userId,@Param("purpose") String purpose,
							   @Param("categoryName") String categoryName,@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select c.name as name,c.uuid as code,c.id as catagoryId from categorytransactionmapper ctm,account a,transaction t,"
				 + " category c where a.purpose = :purpose and a.id = ctm.accountId and t.id = ctm.transactionId and ctm.categoryId = c.id "
			 	 + " and ctm.createdBy = :userId and c.parentId is not null and ctm.categoryId in (:subCategoryIds) and ctm.isActive = true "
			 	 + " and t.isActive = true and month(ctm.createdDate) = :month and year(ctm.createdDate) = :year GROUP BY c.id ORDER BY ctm.id DESC", nativeQuery = true)
	public List<Object[]> getUnBudgedSubCategoriesByUserIdAndMonthAndYearAndSubCatagoryIds(@Param("userId") Long userId,@Param("purpose") String purpose,
										  @Param("subCategoryIds") List<Long> subCategoryIds, @Param("month") Integer month,@Param("year") Integer year);
	
	@Modifying
	@Transactional
	@Query("UPDATE CategoryTransactionMapper mapper set mapper.isActive = false WHERE mapper.account.id = :accountId AND mapper.createdBy.id = :userId")
	public void deleteCategoryTransactionMappersByUserIdAndAccountId(@Param(value="userId") Long userId,@Param(value="accountId") Long accountId);
}
