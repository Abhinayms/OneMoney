package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Division;

@Repository
public interface DivisionRepository extends CrudRepository<Division,Long> {

	@Query("FROM Division division WHERE division.id = :divisionId "
			+ "AND division.isActive = true")
	public Division getDivisionById(@Param("divisionId") Long divisionId);
	
	@Query("FROM Division division "
			+ "LEFT JOIN FETCH division.entity as entity "
			+ "WHERE entity.id = :entityId "
			+ "AND entity.isActive = true "
			+ "AND division.isActive = true")
	public List<Division> getAllDivisionsByEntityId(@Param("entityId") Long entityId);

	@Query("FROM Division division WHERE division.uuid = :uuid "
			+ "AND division.isActive = true")
	public Division getDivisionByUUID(@Param("uuid")String uuid);

}
