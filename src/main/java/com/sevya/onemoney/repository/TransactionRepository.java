package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {


	@Query("FROM Transaction transaction"
			+ " WHERE transaction.fingerprint =:fingerprint AND transaction.createdBy.id=:userId AND transaction.isActive = true")
	public Transaction getTransactionByFingerprint(@Param("userId")Long userId, @Param("fingerprint")String fingerprint);
	
	
	@Query("FROM Transaction transaction"
			+ " WHERE transaction.isCategorized=:isCategorized AND transaction.createdBy.id=:userId AND transaction.isActive = true")
	public List<Transaction> getTransactionsByCategorized(@Param("userId")Long userId, @Param("isCategorized")Boolean isCategorized);
	
	@Modifying
	@Transactional
	@Query("UPDATE Transaction transaction set transaction.isActive = false WHERE transaction.accountCode = :accountCode AND transaction.createdBy.id = :userId")
	public void deleteTransactionsByUserIdAndAccountCode(@Param(value="userId") Long userId,@Param(value="accountCode") String accountCode);
	
	@Query("FROM Transaction transaction WHERE transaction.accountCode = :accountCode AND transaction.createdBy.id = :userId AND transaction.isActive = true")
	public List<Transaction> getTransactionByUserIdAndAccountCode(@Param(value="userId") Long userId,@Param(value="accountCode") String accountCode);
	
}
