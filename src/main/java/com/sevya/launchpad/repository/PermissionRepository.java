package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Group;
import com.sevya.launchpad.model.Permission;
import com.sevya.launchpad.model.User;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
	
	@Query("FROM Permission AS p WHERE p.isActive=true AND p.id=:id")
	public Permission getPermission	(@Param("id") Long id);
		
	
	@Query("FROM Permission AS p WHERE p.isActive=true AND p.uuid=:uuidCode")
	public Permission getPermissionByUUIDCode(@Param("uuidCode")String uuidCode);
	
	
	@Query("FROM Permission AS p WHERE p.isActive=true")
	public List<Permission> getAllPermissions();
	
	@Query("FROM Permission AS p WHERE p.isActive=true")
	public Page<Permission> getAllPermissions(Pageable pageable);
	
	
	
	
	 @Query("FROM Permission p WHERE " +
	            "LOWER(p.name) LIKE LOWER(CONCAT('%',:criteria, '%')) OR " +
	            "LOWER(p.code) LIKE LOWER(CONCAT('%',:criteria, '%')) OR " +
	            "LOWER(p.urlPattern) LIKE LOWER(CONCAT('%',:criteria, '%')) AND p.isActive=true")	
	public Page<Permission> getAllPermissionsByCriteria(@Param("criteria")String criteria, Pageable pageable);
	
	

	@Query("SELECT DISTINCT usr FROM User AS usr "
			+ "LEFT JOIN FETCH usr.userRoleMappers AS urm "
			+ "LEFT JOIN FETCH urm.role r "
			+ "LEFT JOIN FETCH r.rolePermissionMappers rpm "
			+ "LEFT JOIN FETCH rpm.permission p "
			+ "WHERE usr.id = :userId AND p.id = :permissionId "
			+ "AND p.isActive=true AND r.isActive=true")
	public User getUserByPermission(@Param("userId") long userId, @Param("permissionId")long permissionId);
	
	
	
	@Query("SELECT DISTINCT g FROM Group AS g "
			+ "LEFT JOIN FETCH g.groupRoleMappers AS grm "
			+ "LEFT JOIN FETCH grm.role r "
			+ "LEFT JOIN FETCH r.rolePermissionMappers rpm "
			+ "LEFT JOIN FETCH rpm.permission p "
			+ "WHERE g.id = :groupId AND p.id = :permissionId "
			+ "AND p.isActive=true AND r.isActive=true")
	public Group getGroupByPermission(@Param("groupId") long groupId, @Param("permissionId")long permissionId);

	
	
	@Query("FROM Permission AS p "
			+ "LEFT JOIN FETCH p.rolePermissionMappers AS rpm  "
			+ "LEFT JOIN FETCH rpm.role r "
			+ "LEFT JOIN FETCH r.userRoleMappers urm  "
			+ "LEFT JOIN FETCH urm.user AS u  "
			+ "WHERE u.id = :userId "
			+ "AND p.isActive=true  ")
	public Iterable<Permission> getPermissinosByUser(@Param("userId")Long userId);

	
	@Query("FROM Permission AS p "
			+ "LEFT JOIN FETCH p.rolePermissionMappers AS rpm  "
			+ "LEFT JOIN FETCH rpm.role r "
			+ "LEFT JOIN FETCH r.groupRoleMappers grm  "
			+ "LEFT JOIN FETCH grm.group AS g  "
			+ "WHERE g.id = :groupId "
			+ "AND p.isActive=true  ")	
	public Iterable<Permission> getPermissinosByGroup(@Param("groupId")Long groupId);

	

	

	
}
