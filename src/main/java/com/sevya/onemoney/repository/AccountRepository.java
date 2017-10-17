package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	@Query("FROM Account account WHERE account.id = :accountId AND account.isActive = true")
	public Account getAccountByAccountId(@Param("accountId") Long accountId);
	
	@Query("FROM Account account WHERE account.accountCode = :accountCode AND account.isActive = true AND account.createdBy.id = :userId")
	public Account getAccountByAccountCode(@Param("userId") Long userId,@Param("accountCode") String accountCode);
	
	@Modifying
	@Transactional
	@Query("UPDATE Account account set account.isActive = false WHERE account.createdBy.id = :userId AND account.accountCode = :accountCode")
	public void deleteAccountByUserIdAndAccountCode(@Param("userId") Long userId,@Param(value="accountCode") String accountCode);
	
	@Query(value = " select a.id,a.accountCode,a.accountName,a.purpose,a.updatedAt,at.name from account a, accounttype at,accountaccountgroupmapper aag, "
				 + " user u where a.createdBy = u.id and a.accountTypeId = at.id and u.id = :userId and aag.accountId = a.id and aag.isActive = 1 "
				 + " and month(a.createdDate) = :month and year(a.createdDate) = :year and at.name = :accountType and a.purpose = :purpose", nativeQuery = true )
	public List<Object[]> getAccountsByUserIdAndMonthAndYearAndAccountTypeAndPurpose(@Param("userId") Long userId,@Param("month") Integer month,
										 @Param("year") Integer year,@Param("accountType") String accountType,@Param("purpose") String purpose);
	
	@Query(value = " select a.accountCode,a.accountName,a.purpose,a.updatedAt,at.name from account a, accounttype at, user u, accountaccountgroupmapper aag "
				 + " where aag.accountId = a.id and aag.accountgroupId = :groupId and a.createdBy = u.id and a.accountTypeId = at.id and "
				 + " u.id = :userId and month(a.createdDate) = :month and year(a.createdDate) = :year and aag.isActive = 1", nativeQuery = true )
	public List<Object[]> getAccountsByUserIdGroupIdAndMonthAndYear(@Param("userId") Long userId,@Param("groupId") Long groupId,
												@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value = " select a.accountCode from account a, accounttype at"+
			   " WHERE at.name IN(:accountTypes) and a.purpose = :purpose and a.isActive = true"+
			   " and a.createdBy =:userId and a.accountTypeId = at.id", nativeQuery = true)
	public List<String> getAllAccountIdsByAccountTypesAndPurpose(@Param("userId")Long userId, @Param(value = "accountTypes")List<String> accountTypes, @Param(value = "purpose") String purpose);

	@Query(value = " select count(*) from account where createdBy = :userId and purpose = :purpose and isActive = 1 ", nativeQuery = true)
	public Integer checkingAccountsAvailabilityByUserAndPurpose(@Param("userId") Long userId, @Param("purpose") String purpose);

	

}
