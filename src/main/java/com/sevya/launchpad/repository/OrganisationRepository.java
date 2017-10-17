package com.sevya.launchpad.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Organisation;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation,Long> {
	
	@Query("FROM Organisation organisation "
			+ "WHERE organisation.id = :orgId AND organisation.isActive = true")
	public Organisation getOrgById(@Param("orgId") Long orgId);
	
	@Query("FROM Organisation organisation WHERE organisation.isActive = true")
	public List<Organisation> getAllOrganisations();

	
	@Query("FROM Organisation organisation "
			+ "WHERE organisation.uuid = :uuid  AND organisation.isActive = true")
	public Organisation getOrgByUUID(@Param("uuid")String uuid);

}