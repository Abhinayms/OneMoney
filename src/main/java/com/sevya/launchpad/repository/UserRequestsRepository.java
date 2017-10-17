package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.model.UserRequests.RequestType;

@Repository
public interface UserRequestsRepository extends CrudRepository<UserRequests, Long> {

	@Query("FROM UserRequests as userRequests LEFT JOIN FETCH userRequests.user as user "
			+ "WHERE userRequests.requestKey =:requestKey and userRequests.isComplete = false "
			+ "and userRequests.isActive =true" )
	public UserRequests getUserRequestsByActivateToken(@Param("requestKey")String requestKey);
	
	@Query("FROM UserRequests as userRequests LEFT JOIN FETCH userRequests.user as user "
			+ "WHERE userRequests.requestKey =:requestKey AND user.email=:email AND userRequests.isComplete = false "
			+ "AND userRequests.isActive =true" )
	public UserRequests getUserByEmailAndActivationKey(@Param("email")String email, @Param("requestKey")String requestKey);
	
	
	@Query("FROM UserRequests as userRequests LEFT JOIN FETCH userRequests.user as user "
			+ "WHERE userRequests.requestKey =:requestKey AND user.mobile=:mobile AND userRequests.isComplete = false "
			+ "AND user.countryCode = :countryCode "
			+ "AND userRequests.isActive =true" )
	public UserRequests getUserRequestsByMobileAndCountryCodeAndActivationKey(@Param("mobile")String mobile, @Param("countryCode")String countryCode, @Param("requestKey")String requestKey);

	
	@Query("FROM UserRequests as userRequests LEFT JOIN FETCH userRequests.user as user "
			+ "WHERE userRequests.requestType =:requestType AND user.mobile=:mobile AND userRequests.isComplete = false "
			+ "AND user.countryCode = :countryCode "
			+ "AND userRequests.isActive =true" )
	public List<UserRequests> getUserRequestsByMobileAndCountryCodeAndRequestType(@Param("mobile")String mobile, @Param("countryCode") String countryCode, @Param("requestType")RequestType requestType);
	
	
}
