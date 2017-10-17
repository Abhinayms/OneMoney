package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.AccountAccountGroupMapper;
import com.sevya.onemoney.utility.Purpose;

@Repository
public interface AccountAccountGroupMapperRepository extends CrudRepository<AccountAccountGroupMapper, Long> {
	
	
	@Query(value = "FROM AccountAccountGroupMapper mapper "
		 	 + "LEFT JOIN FETCH mapper.account AS account "
			 + "LEFT JOIN FETCH account.accountType AS accountType "
			 + "WHERE accountType.name IN (:accountTypes) AND accountType.isActive = true "
			 + "AND account.createdBy.id = :userId AND account.purpose = :purpose AND mapper.isActive = true AND account.isActive = true")
	public List<AccountAccountGroupMapper> getAccountAccountGroupMappersByAccountTypesAndPurpose(@Param(value = "userId") Long userId,
								  @Param(value = "accountTypes") List<String> accountTypes,@Param(value = "purpose") Purpose person);
	
	@Query(value = "FROM AccountAccountGroupMapper mapper "
			 + "LEFT JOIN FETCH mapper.accountGroup As group "
			 + "LEFT JOIN FETCH mapper.account As account "
			 + "WHERE group.isActive = true AND group.id = :groupId "
			 + "AND account.isActive = true AND mapper.createdBy.id = :userId AND mapper.isActive = true")
	public List<AccountAccountGroupMapper> getAccountAccountGroupMappersByGroupIdAndUserId(@Param(value="groupId") Long groupId,@Param(value="userId") Long userId);
	
	@Query(value = "FROM AccountAccountGroupMapper mapper "
				 + "LEFT JOIN FETCH mapper.accountGroup As group "
				 + "LEFT JOIN FETCH mapper.account As account "
				 + "WHERE group.isActive = true AND group.id = :groupId "
				 + "AND account.isActive = true ANd account.accountCode = :accountCode AND mapper.isActive = true")
	public AccountAccountGroupMapper getAccountAccountGroupMapperByGroupIdAndAccountCode(@Param(value="groupId") Long groupId,@Param(value="accountCode") String accountCode);
	
	@Query(value = "FROM AccountAccountGroupMapper mapper "
			 	 + "LEFT JOIN FETCH mapper.accountGroup As group "
			 	 + "LEFT JOIN FETCH mapper.account As account "
			 	 + "WHERE group.isActive = true AND group.id = :groupId "
			 	 + "AND account.isActive = true AND account.id = :accountId AND mapper.isActive = false")
	public AccountAccountGroupMapper checkAccountAccountGroupMapperForPreviouslyMapperAccount(@Param(value="groupId") Long groupId,@Param(value="accountId") Long accountId);
	
	
	@Query(value = "FROM AccountAccountGroupMapper mapper "
			 	 + "LEFT JOIN FETCH mapper.account As account "
			 	 + "LEFT JOIN FETCH account.accountType As type "
			 	 + "WHERE account.purpose = :purpose AND account.isActive = true "
			 	 + "AND (type.name = :accountType1 OR type.name = :accountType2 OR "
			 	 + "type.name = :accountType3) AND mapper.createdBy.id = :userId AND mapper.isActive = true")
	public List<AccountAccountGroupMapper> getAccountAccountGroupMappersByUserIdAndMonthAndYearAndAccountTypeAndPurpose(@Param("userId") Long userId,@Param("accountType1") String accountType1,
					@Param("accountType2") String accountType2,@Param("accountType3") String accountType3,@Param("purpose") Purpose purpose);

	@Modifying
	@Transactional
	@Query("UPDATE AccountAccountGroupMapper mapper set mapper.isActive = false "
		 + "WHERE mapper.accountGroup.id = :groupId and mapper.account.accountCode = :accountId")
	public void deleteAccountAccountGroupMapperByGroupIdAndAccountCode(@Param(value="groupId") Long groupId,@Param(value="accountId") String accountId);
	
	@Modifying
	@Transactional
	@Query("UPDATE AccountAccountGroupMapper mapper set mapper.isActive = false WHERE mapper.accountGroup.id = :groupId")
	public void deleteAccountAccountGroupMapperByGroupId(@Param(value="groupId") Long groupId);
	
	@Query(value = "FROM AccountAccountGroupMapper mapper "
				 + "LEFT JOIN FETCH mapper.accountGroup As group "
				 + "LEFT JOIN FETCH mapper.account As account "
				 + "WHERE group.isActive = true AND group.id = :accountGroupId AND mapper.isActive = true AND account.isActive = true and mapper.createdBy.id = :userId")
	public List<AccountAccountGroupMapper> getAccountAccountGroupMappersByUserIdAndGroupId(@Param("userId") Long userId,@Param("accountGroupId") Long accountGroupId);
	
	
}
