package com.sevya.onemoney.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.UserDetails;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
	
	@Query("FROM UserDetails AS userDetails "
		+ "LEFT JOIN FETCH userDetails.user AS user  "
		+ "WHERE user.id =:userId AND user.isActive=true AND userDetails.isActive = true")
	public UserDetails getUserDetailsByUserId(@Param("userId")Long userId);
	
}
