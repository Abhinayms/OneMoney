package com.sevya.launchpad.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group,Long> {

	
	@Query("SELECT DISTINCT g FROM Group AS g "
			+ "LEFT JOIN FETCH g.groupRoleMappers AS grm "
			+ "LEFT JOIN FETCH grm.role r "
			+ "WHERE r.id = :roleId AND grm.isActive=true")
	public Group getGroupByRoleId(@Param("roleId")Long roleId);
	
	@Query("SELECT DISTINCT g FROM Group AS g "
			+ "LEFT JOIN FETCH g.groupRoleMappers AS grm "
			+ "WHERE grm.group.id =:groupId AND grm.role.id =:roleId AND grm.isActive=true")
	public Group getGroupByGroupIdAndRoleId(@Param("groupId")Long groupId,@Param("roleId")Long roleId);

	@Query("FROM Group group where group.uuid =:uuid AND group.isActive=true")
	public Group getGroupByUUID(@Param("uuid")String uuid);

	@Query("FROM Group group where group.id =:groupId AND group.isActive=true")
	public Group getGroupById(@Param("groupId")Long groupId);

	
	
}