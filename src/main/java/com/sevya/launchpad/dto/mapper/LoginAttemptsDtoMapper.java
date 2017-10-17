package com.sevya.launchpad.dto.mapper;

import java.util.Date;

import com.sevya.launchpad.model.LoginAttempts;
import com.sevya.launchpad.model.User;

public class LoginAttemptsDtoMapper {
	
	
	public static LoginAttempts toLoginAttempts(User user,String ipAddress,boolean status) {
		
		LoginAttempts loginAttempts = new LoginAttempts();
		if(user != null)
			loginAttempts.setEmail(user.getEmail());
		loginAttempts.setStatus(status);
		loginAttempts.setUser(user);
		loginAttempts.setIpAddress(ipAddress);
		loginAttempts.setCreatedDate(new Date());
		loginAttempts.setModifiedDate(new Date());
		return loginAttempts;
		
	}

}
