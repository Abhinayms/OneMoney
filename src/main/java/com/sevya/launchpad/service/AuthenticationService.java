package com.sevya.launchpad.service;

import javax.servlet.http.HttpServletRequest;

import com.sevya.launchpad.dto.ResetPasswordDto;
import com.sevya.launchpad.model.ApiCredentials;
import com.sevya.launchpad.model.User;


/**
 * Abstract interface for Authentication
 * @author Sevya
 */

public interface AuthenticationService {	
	
	public ApiCredentials getApiCredentialsByClientCredentials(String appId);
	
	public ApiCredentials generateApiCredentialsForUser(User user);
	
	public User authenticate(String mobile, String countryCode, String  password);
	
	public Boolean forgotPassword(HttpServletRequest request,String email) throws Exception;
	
	public Boolean resetPassword(ResetPasswordDto resetPasswordDto);
	
	public void trackLoginAttempts(User user, String ipAddress, boolean status);
	
	public Integer getNumberOfLoginAttemptsForUser(User user);
	
	public User incrementLoginAttemptForUser(User user);
	
	public Boolean isLoginAtemptExceeded(User user);
	
}
