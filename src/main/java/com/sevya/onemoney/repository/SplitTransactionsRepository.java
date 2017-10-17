package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.onemoney.model.SplitTransactions;

@Repository
public interface SplitTransactionsRepository extends CrudRepository<SplitTransactions, Long> {
	
	@Query("FROM SplitTransactions st "
			+ "LEFT JOIN FETCH st.transaction AS transaction "
			+ "LEFT JOIN FETCH st.category AS category "
			+ "WHERE st.isActive = true AND transaction.id = :transactionId ")
	public List<SplitTransactions> getSplitTransactionsByTransactionId(@Param("transactionId") Long transactionId );
	
	@Modifying
	@Transactional
	@Query("UPDATE SplitTransactions stn SET stn.isActive = false WHERE stn.transaction.id = :transactionId")
	public void deleteOldSplitTransactions(@Param("transactionId") Long transactionId);
	
}
