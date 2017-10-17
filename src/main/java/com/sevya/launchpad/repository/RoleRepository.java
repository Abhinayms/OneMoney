package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("FROM Role AS r "
			+ "LEFT JOIN r.userRoleMappers AS urm  "
			+ "LEFT JOIN urm.user u "
			+ "WHERE u.email =:email AND urm.isActive=true ")
	public List<String> getRoleByEmail(@Param("email") String email);
	
	@Query("FROM Role AS role WHERE role.isActive=true AND role.id=:roleId")	
	public Role getRoleByRoleId	(@Param("roleId") Long id);

	@Query("FROM Role AS role WHERE role.isActive=true")
	public Iterable<Role> getAllRoles();
	
	@Query("FROM Role AS r "
			+ "LEFT JOIN FETCH r.userRoleMappers AS urm  "
			+ "LEFT JOIN FETCH urm.user u "
			+ "WHERE u.id =:userId AND urm.isActive=true ")
	public Iterable<Role> getRolesByUserId(Long userId);
	
	
	@Query("FROM Role AS r "
			+ "LEFT JOIN FETCH r.userRoleMappers AS urm  "
			+ "LEFT JOIN FETCH urm.user u "
			+ "WHERE u.uuid =:userUuid AND urm.isActive=true ")
	public Iterable<Role> getRolesByUserUUIDCode(@Param("userUuid")String userUuid);
	
	
	
	@Query("FROM Role AS r "
			+ "LEFT JOIN FETCH r.groupRoleMappers AS grm "
			+ "LEFT JOIN FETCH grm.group g "
			+ "WHERE g.id =:groupId AND grm.isActive =true ")
	public Iterable<Role> getRolesByGroupId(Long groupId);
	
	@Query("FROM Role AS r "
			+ "LEFT JOIN FETCH r.groupRoleMappers AS grm "
			+ "LEFT JOIN FETCH grm.role r "
			+ "LEFT JOIN FETCH grm.group g "
			+ "WHERE r.id =:roleId AND g.id =groupId AND grm.isActive=true ")
	public Role getRoleByRoleIdAndGroupId(Long roleId,Long groupId);

	
	
}
