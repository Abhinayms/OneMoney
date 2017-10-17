package com.sevya.launchpad.util;

import java.security.SecureRandom;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

public class LaunchpadUtility {
	
	
	public static String getIP(HttpServletRequest request) {
		
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null)
			ipAddress = request.getRemoteAddr();
		return ipAddress;
	
	}
	
	public static String generateApiSecret() {
		
		String uuid = md5UniqueCode();
		return uuid.replaceAll("-", "");		
	
	}
	
	public static String md5UniqueCode() {
		
		String content = getTimeStampValue();
		return DigestUtils.md5Hex(content);
	
	}
	
	
	public static String generateOTP() {
		
		 SecureRandom random = new SecureRandom();
		 int otp = 100000 + random.nextInt(900000);
	     return String.valueOf(otp);
	
	}
	
	

	public static String getTimeStampValue() {
		
		java.util.Date today = new java.util.Date();
		java.sql.Timestamp ts1 = new java.sql.Timestamp(today.getTime());
		return String.valueOf(ts1.getTime());

	}
	
	/***
	 * Generate UUID code
	 * */
	public static String generateUUIDCode(){
		String uuid =UUID.randomUUID().toString().replace("-", "");
		uuid =uuid.substring(0,6);
		String content = getTimeStampValue();
		content =DigestUtils.md5Hex(content).substring(0,5);		
		return content.concat(uuid);
	}
}
