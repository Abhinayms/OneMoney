package com.sevya.onemoney.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.FinancialInstitution;

@Repository
public interface FinancialInstitutionRepository extends CrudRepository<FinancialInstitution, Long> {

	@Query("FROM FinancialInstitution f WHERE f.instCode = :instCode AND f.isActive = true")
	public FinancialInstitution getFinancialInstitutionByCode(@Param(value="instCode") Long instCode);
	
	@Query(value = " select f.instCode from financialinstitution f, accountgroup ag, "
				 + " accountaccountgroupmapper aag where aag.accountGroupId = ag.id and "
				 + " ag.financialInstitutionId = f.id and aag.accountId = :accountId limit 1", nativeQuery = true)
	public Integer getFinancialInstitutionCodeByAccountId(@Param(value="accountId") Long accountId);
}
