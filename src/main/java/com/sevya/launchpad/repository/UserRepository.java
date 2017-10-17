package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	@Query("FROM User user where user.id =:userId AND user.isActive=true")
	public User getUserById(@Param("userId")Long userId);
	
	@Query("FROM User user where user.uuid =:uuidCode AND user.isActive=true")
	public User getUserByUUIDCode(@Param("uuidCode")String uuidCode);
	
	@Query("UPDATE User user set user.isActive = false where user.id =:userId")
	public void deleteUserById(@Param("userId")Long userId);
	
	@Query("SELECT DISTINCT u FROM User AS u "
			+ "LEFT JOIN FETCH u.userRoleMappers AS urm  "
			+ "LEFT JOIN FETCH urm.role r "
			+ "WHERE u.id = :userId AND r.id = :roleId "
			+ "AND u.isActive=true AND urm.isActive=true")
	public User getUserByUserIdAndRoleId(@Param("userId")Long userId, @Param("roleId")Long roleId);
	
	@Query("FROM User AS u "
			+ "LEFT JOIN FETCH u.userRoleMappers AS urm  "
			+ "LEFT JOIN FETCH urm.role r "
			+ "WHERE r.id =:roleId AND u.isActive=true AND urm.isActive=true")
	public Iterable<User> getUsersByRoleId(@Param("roleId")Long roleId);
	
	
	@Query("SELECT DISTINCT u FROM User AS u "
			+ "LEFT JOIN FETCH u.userGroupMappers AS ugm "
			+ "LEFT JOIN FETCH ugm.group g "
			+ "WHERE u.id = :userId AND g.id = :groupId "
			+ "AND u.isActive=true AND g.isActive=true")
	public User getUserByUserIdGroupId(@Param("userId") Long userId, @Param("groupId")Long groupId);

	@Query("FROM User AS u "
			+ "LEFT JOIN FETCH u.userGroupMappers AS ugm  "
			+ "LEFT JOIN FETCH ugm.group g "
			+ "WHERE g.id = :groupId AND ugm.isActive=true ")
	public Iterable<User> getUsersByGroupId(@Param("groupId")Long groupId);
	
	
	@Query("FROM User AS u "
			+ "LEFT JOIN FETCH u.userGroupMappers AS ugm  "
			+ "LEFT JOIN FETCH ugm.group g "
			+ "WHERE g.uuid = :groupUuidCode AND ugm.isActive=true ")
	public Iterable<User> getUsersByGroupUUID(@Param("groupUuidCode")String groupUuidCode);
	

	@Query("FROM User AS u "
			+ "WHERE u.email = :email AND u.isActive=true ")
	public User getUserByEmail(@Param("email")String email);
	
	
	@Query("FROM User AS u "
			+ "WHERE u.mobile = :mobile AND u.isActive=true ")
	public User getUserByMobile(@Param("mobile")String mobile);
	
	@Query("FROM User AS u "
			+ "WHERE u.mobile = :mobile AND u.isActive=true "
			+ "AND u.countryCode = :countryCode")
	public User getUserByMobileAndCountryCode(@Param("mobile") String mobile, @Param("countryCode") String countryCode);
	
	@Query("FROM User AS user "
			+ "WHERE user.mobile = :mobile "
			+ "AND user.countryCode = :countryCode")
	public User getExistedUserByMobileAndCountryCode(@Param("mobile")String mobile,
													@Param("countryCode")String countryCode);

	@Query("FROM User AS u WHERE u.isActive=true")
	public List<User> getAllUsers();
	
	@Query("FROM User AS u WHERE u.isActive=true" )
	public Page<User> getAllUsersByPagination(Pageable pageRequest);
	
	@Query("FROM User u WHERE " +
	            "LOWER(u.firstName) LIKE LOWER(CONCAT('%',:criteria, '%')) OR " +
	            "LOWER(u.email) LIKE LOWER(CONCAT('%',:criteria, '%')) OR " +
	            "LOWER(u.mobile) LIKE LOWER(CONCAT('%',:criteria, '%')) AND u.isActive=true")	
	public Page<User> getAllUsersBySearchTerm(@Param("criteria")String criteria,Pageable pageable);

	
}
