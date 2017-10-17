package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.AccountGroup;

@Repository
public interface AccountGroupRepository extends CrudRepository<AccountGroup, Long> {
	
	@Query("FROM AccountGroup group WHERE group.id = :groupId AND group.isActive = true AND group.createdBy.id = :userId")
	public AccountGroup getAccountGroup(@Param(value = "userId") Long userId,@Param(value = "groupId") Long groupId);
	
	@Query("FROM AccountGroup accountGroup "
			 + "LEFT JOIN FETCH accountGroup.financialInstitution AS financialInstitution "
			 + "WHERE accountGroup.createdBy.id = :userId AND accountGroup.isActive = true "
			 + "AND financialInstitution.instCode = :instCode")
	public AccountGroup getAccountGroupsByUserIdAndInstCode(@Param(value = "userId") Long userId,@Param("instCode") Long instCode);
	
	@Query("FROM AccountGroup accountGroup "
		 + "LEFT JOIN FETCH accountGroup.financialInstitution AS financialInstitution "
		 + "WHERE accountGroup.createdBy.id = :userId AND accountGroup.isActive = true ")
	public List<AccountGroup> getAccountGroupsByUserIdAndMonthAndYear(@Param(value = "userId") Long userId);
	
	@Modifying
	@Transactional
	@Query("UPDATE AccountGroup g set g.isActive = false WHERE g.id = :groupId")
	public void deleteAccountGroupByGroupId(@Param(value="groupId") Long groupId);
	
	
	
	@Query("FROM AccountGroup group WHERE group.id = :groupId AND group.isActive = true")
	public AccountGroup getAccountGroupByGroupId(@Param(value="groupId") Long groupId);
	

}
