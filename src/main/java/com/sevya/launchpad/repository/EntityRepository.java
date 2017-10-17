package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Entity;

@Repository
public interface EntityRepository extends CrudRepository<Entity,Long> {
	
	@Query("FROM Entity entity WHERE entity.id = :entityId AND entity.isActive = true")
	public Entity getEntityById(@Param("entityId") Long entityId);
	
	@Query("FROM Entity entity "
			+ "LEFT JOIN FETCH entity.organisation AS organisation "
			+ "WHERE organisation.id = :orgId AND organisation.isActive = true "
			+ "AND entity.isActive = true")
	public List<Entity> getAllEntitiesByOrgId(@Param("orgId") Long orgId);

	
	@Query("FROM Entity entity WHERE entity.uuid = :uuid  AND entity.isActive = true")
	public Entity getEntityByUUID(@Param("uuid")String uuid);
	
}
