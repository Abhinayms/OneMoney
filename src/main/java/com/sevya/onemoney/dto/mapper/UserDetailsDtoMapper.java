package com.sevya.onemoney.dto.mapper;

import org.apache.commons.codec.digest.DigestUtils;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.onemoney.dto.UserDetailsDto;
import com.sevya.onemoney.model.UserDetails;

public class UserDetailsDtoMapper {

	private UserDetailsDtoMapper(){}
	
	public static User toUser(UserDetailsDto userDetailsDto) {
	
		User user = new User();
		user.setFirstName(userDetailsDto.getName());
		user.setMobile(userDetailsDto.getMobile());
		user.setCountryCode(userDetailsDto.getCountryCode());
		user.setPassword(DigestUtils.md5Hex(userDetailsDto.getPassword()));
		user.setUuid(LaunchpadUtility.generateUUIDCode());
		user.setAvtar("https://s3.amazonaws.com/gamez2know/assets/images/user.png");
		user.setIsActive(false);
		return user;
	
	}
	
	
	
	public static User toUser(User user,UserDetailsDto userDetailsDto) {
	
		user.setFirstName(userDetailsDto.getName()!=null ? userDetailsDto.getName()  : user.getFirstName());
		user.setMobile(userDetailsDto.getMobile() !=null ? userDetailsDto.getMobile() : user.getMobile());
		user.setCountryCode(userDetailsDto.getCountryCode()!=null ? userDetailsDto.getCountryCode() : user.getCountryCode());
		user.setEmail(userDetailsDto.getEmail() !=null ? userDetailsDto.getEmail() : user.getEmail());
		return user;
		
	}
	
	
	public static UserDetails toUserDetails(User user, UserDetails userDetails, UserDetailsDto userDetailsDto) {
		
		userDetails.setUser(user);
		userDetails.setCreatedBy(user);
		userDetails.setUuid(userDetails.getUuid() == null ? LaunchpadUtility.generateUUIDCode() : userDetails.getUuid());
		userDetails.setBusinessMonthlyIncome(userDetailsDto.getBusinessMonthlyIncome() !=null ? userDetailsDto.getBusinessMonthlyIncome() : userDetails.getBusinessMonthlyIncome());
		userDetails.setPersonalMonthlyIncome(userDetailsDto.getPersonalMonthlyIncome() !=null ? userDetailsDto.getPersonalMonthlyIncome() : userDetails.getPersonalMonthlyIncome());
		return userDetails;
		
	}
	
	
	public static UserDetailsDto toUserDetailsDto(UserDetails userDetails) {
		
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		User user = userDetails.getUser();
		userDetailsDto.setMobile(user.getMobile());
		userDetailsDto.setEmail(user.getEmail());
		userDetailsDto.setName(user.getFirstName());
		userDetailsDto.setBusinessMonthlyIncome(userDetails.getBusinessMonthlyIncome());
		userDetailsDto.setPersonalMonthlyIncome(userDetails.getPersonalMonthlyIncome());
		userDetailsDto.setCountryCode(user.getCountryCode());
		return userDetailsDto;
	}

	public static UserDetailsDto toUserDetailsDto(UserRequests userRequests) {
		
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		User user = userRequests.getUser();
		userDetailsDto.setMobile(user.getMobile());
		userDetailsDto.setCountryCode(user.getCountryCode());
		userDetailsDto.setEmail(user.getEmail());
		userDetailsDto.setName(user.getFirstName());
		userDetailsDto.setAvtar(user.getAvtar());
		return userDetailsDto;
	
	}
	
	

	
	
}
