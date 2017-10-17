package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.AccountType;
import com.sevya.onemoney.utility.Purpose;

@Repository
public interface AccountTypeRepository extends CrudRepository<AccountType, Long> {
	
	@Query("FROM AccountType type WHERE type.name = :accountType AND type.isActive = true")
	public AccountType getAccountTypeByAccountType(@Param(value = "accountType") String accountType);
	
	@Query("FROM AccountType type "
		 + "LEFT JOIN FETCH type.accounts AS accounts "
		 + "WHERE type.name IN (:accountTypes) AND type.isActive = true "
		 + "AND accounts.createdBy.id = :userId AND accounts.purpose = :purpose" )
	public List<AccountType> getAccountsByAccountType(@Param(value = "userId") Long userId, @Param(value = "accountTypes") List<String> accountTypes, @Param(value = "purpose") Purpose person);
	
	@Query(" FROM AccountType accountType"+
		   " LEFT JOIN FETCH accountType.accounts AS accounts"+
		   " WHERE accounts.purpose = :purpose AND accounts.isActive = true"+
		   " AND accounts.createdBy.id = :userId AND accountType.isActive = true")
	public List<AccountType> getAllAccountTypesByPurpose(@Param(value = "userId") Long userId, @Param(value = "purpose") Purpose purpose);


}
