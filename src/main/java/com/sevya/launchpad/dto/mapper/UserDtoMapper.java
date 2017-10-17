package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.sevya.launchpad.dto.UserDto;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.util.LaunchpadUtility;

public class UserDtoMapper {	
	
	public static User toUser(UserDto userDto) {
		
		if (userDto != null) {
			
			User user = new User();
			
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setMiddleName(userDto.getMiddleName());
			user.setGender(userDto.getGender());
			user.setIsActive(true);
			user.setAvtar(userDto.getAvtar());
			user.setEmail(userDto.getEmail());
			user.setPassword(DigestUtils.md5Hex(userDto.getPassword()));
			user.setMobile(userDto.getMobile());
			user.setCreatedDate(new Date());
			user.setModifiedDate(new Date());
			user.setLogInFailureAttempts(0);
			user.setUuid(LaunchpadUtility.generateUUIDCode());
			
			return user;
		}
		return null;
	}

	/**
	 * Returns edited user
	 * 
	 * */
	
	public static User toUser(UserDto userDto,User user){
		user.setAvtar(userDto.getAvtar()!=null?userDto.getAvtar():user.getAvtar());
		user.setFirstName(userDto.getFirstName()!=null?userDto.getFirstName():user.getFirstName());
		user.setLastName(userDto.getLastName()!=null?userDto.getLastName():user.getLastName());
		user.setMiddleName(userDto.getMiddleName()!=null?userDto.getMiddleName():user.getMiddleName());
		user.setGender(userDto.getGender()!=null?userDto.getGender():user.getGender());
		user.setMobile(userDto.getMobile()!=null?userDto.getMobile():user.getMobile());
		
		user.setModifiedDate(new Date());
		
		return user;
	}
	
	
	public static UserDto toUserDto(User user) {
		
		if (user != null) {
			
			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setFirstName(user.getFirstName());
			userDto.setLastName(user.getLastName());
			userDto.setMiddleName(user.getMiddleName());
			userDto.setGender(user.getGender());
			userDto.setIsActive(user.getIsActive());
			userDto.setAvtar(user.getAvtar());
			userDto.setEmail(user.getEmail());
			userDto.setMobile(user.getMobile());
			userDto.setUuid(user.getUuid());
			
			return userDto;
		}
		return null;
	}
	
	
	public static List<UserDto> toUserDtos(Iterable<User> users) {
		
		List<UserDto> dtos =new ArrayList<UserDto>();
		for(User user : users){

			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setFirstName(user.getFirstName());
			userDto.setLastName(user.getLastName());
			userDto.setMiddleName(user.getMiddleName());
			userDto.setGender(user.getGender());
			userDto.setIsActive(user.getIsActive());
			userDto.setAvtar(user.getAvtar());
			userDto.setEmail(user.getEmail());
			userDto.setMobile(user.getMobile());
			userDto.setUuid(user.getUuid());
			
			dtos.add(userDto);
		}
		return dtos;
	}
	
	

}
