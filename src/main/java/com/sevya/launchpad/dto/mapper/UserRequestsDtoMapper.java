package com.sevya.launchpad.dto.mapper;

import java.util.Date;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.model.UserRequests.RequestType;
import com.sevya.launchpad.util.DateUtility;
import com.sevya.launchpad.util.LaunchpadUtility;

public class UserRequestsDtoMapper {
	
	
	private UserRequestsDtoMapper(){}
	
	
	public static UserRequests toUserRequestsForToken(User user,String ipAddress,RequestType requestType) {
		
			UserRequests userRequests = new UserRequests();
			userRequests.setUser(user);
			userRequests.setRequestingIPAddress(ipAddress);
			userRequests.setRequestKey(LaunchpadUtility.md5UniqueCode());
			userRequests.setLifeOfRequest(DateUtility.addDaysToDate(new Date(),1));
			userRequests.setUuid(LaunchpadUtility.generateUUIDCode());
			userRequests.setRequestType(requestType);
			return userRequests;
		
	}
	
	
	public static UserRequests toUserRequestsForOTP(User user,String ipAddress,RequestType requestType) {
		
			UserRequests userRequests = new UserRequests();
			userRequests.setUser(user);
			userRequests.setRequestingIPAddress(ipAddress);
			userRequests.setRequestKey(LaunchpadUtility.generateOTP());
			userRequests.setLifeOfRequest(DateUtility.addDaysToDate(new Date(),1));
			userRequests.setUuid(LaunchpadUtility.generateUUIDCode());
			userRequests.setRequestType(requestType);
			return userRequests;
	
	}
	
	
	
	
	
	

	


}
