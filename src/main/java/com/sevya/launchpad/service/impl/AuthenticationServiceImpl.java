package com.sevya.launchpad.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sevya.launchpad.dto.MailDto;
import com.sevya.launchpad.dto.ResetPasswordDto;
import com.sevya.launchpad.dto.mapper.LoginAttemptsDtoMapper;
import com.sevya.launchpad.model.ApiCredentials;
import com.sevya.launchpad.model.LoginAttempts;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.model.UserRequests.RequestType;
import com.sevya.launchpad.repository.ApiCredentialsRepository;
import com.sevya.launchpad.repository.LoginAttemptsRepository;
import com.sevya.launchpad.service.AuthenticationService;
import com.sevya.launchpad.service.UserService;
import com.sevya.launchpad.util.EmailUtility;
import com.sevya.launchpad.util.LaunchpadUtility;


@Service
@PropertySource("classpath:launchpad.properties")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private ApiCredentialsRepository apiCredentialsRepository;
	
	@Autowired
	private LoginAttemptsRepository loginAttemptsRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment env;
	
	@Override
	public User authenticate(String mobile, String countryCode, String  password){
		
		String md5Password= DigestUtils.md5Hex(password!=null?password.trim():"");
		User user = userService.getUserByMobileAndCountryCode(mobile!=null?mobile.trim():"", countryCode!=null?countryCode.trim():"");
		
		if(user!=null){
			
			boolean isPasswordMatch = md5Password.equals(user.getPassword())?true:false;
			
			if(isPasswordMatch == false){
				
				Integer noOfLoginAttempts =user.getLogInFailureAttempts();
				if(noOfLoginAttempts != null) {
					noOfLoginAttempts+=1;
					user.setLogInFailureAttempts(noOfLoginAttempts);
				}
				userService.updateUser(user);
				//trackLoginAttempts(user,authenticationDto.getIpAddress(), false);
				
			}else{
				user.setLogInFailureAttempts(0);
				userService.updateUser(user);
				//trackLoginAttempts(user,authenticationDto.getIpAddress(), true);
				
			}
			
			return user;	
		}
		
		//trackLoginAttempts(user, authenticationDto.getIpAddress(), false);
		return null;		
	}	
	
	@Override
	public ApiCredentials getApiCredentialsByClientCredentials(String appId) {
		return apiCredentialsRepository.findApiCredentialsByApiId(appId);
	}

	@Override
	public ApiCredentials generateApiCredentialsForUser(User user) {
		
		ApiCredentials apiCredentials = new ApiCredentials();
		apiCredentials.setApiId(LaunchpadUtility.generateApiSecret().substring(0, 25));
		apiCredentials.setApiSecret(LaunchpadUtility.generateApiSecret());
		apiCredentials.setEnable(true);
		apiCredentials.setUser(user);
		apiCredentialsRepository.save(apiCredentials);
		return apiCredentials;
	
	}

	@Override
	public Boolean forgotPassword(HttpServletRequest request,String email) throws Exception {
		User user = userService.getUserByEmail(email);
		if(user!=null){
			MailDto mailDto =new MailDto();
			
			try {
				
				Map < String, String > variables = new HashMap < String, String > ();
				variables.put("NAME", user.getFirstName());
				variables.put("SUBJECT", "Welcome to Launchpad");
				
				UserRequests userRequestsObj = userService.generateRequestToken(user, LaunchpadUtility.getIP(request), RequestType.forgotPassword);
				String requestKey = userRequestsObj.getRequestKey();
				String actionUrl = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"+ requestKey;
						
				variables.put("ACTIONURL",actionUrl);
				
				mailDto.setBaseUrl(env.getProperty("sendinblumailer.baseUrl"));
				mailDto.setAccessKey(env.getProperty("sendinblumailer.accessKey"));
				mailDto.setVariables(variables);
				mailDto.setEmail(user.getEmail());
				mailDto.setTemplateId(Integer.valueOf(env.getProperty("sendinblumailer.template.forgotpassword")));
			} catch (NumberFormatException ne) {
				mailDto.setTemplateId(1);
			}
	       
			//To-DO : store accesstoken in userrequests
			
			mailDto =EmailUtility.sendMail(mailDto);
			
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void trackLoginAttempts(User user,String ipAddress,boolean status) {
		LoginAttempts loginAttempts = LoginAttemptsDtoMapper.toLoginAttempts(user,ipAddress,status);
		loginAttemptsRepository.save(loginAttempts);
	}

	@Override
	@Transactional
	public Boolean resetPassword(ResetPasswordDto resetPasswordDto) {
	
		UserRequests userRequests = userService.getUserRequestsByEmailAndActivationKey(resetPasswordDto.getEmail(), resetPasswordDto.getActivationKey());
		if(userRequests != null) {
			
			User user = userRequests.getUser();
			user.setPassword(resetPasswordDto.getPassword());
			userService.updateUser(user);
			userRequests.setIsComplete(true);
			userService.createUserRequest(userRequests);
			return true;
		
		}
		return false;
		
	}

	@Override
	public Integer getNumberOfLoginAttemptsForUser(User user) {
		user =userService.getUserByEmail(user.getEmail());
		return user.getLogInFailureAttempts();
	}

	@Override
	public User incrementLoginAttemptForUser(User user) {
		user =userService.getUserByEmail(user.getEmail());
		Integer noOfLoginAttempts =user.getLogInFailureAttempts();
		noOfLoginAttempts+=1;
		user.setLogInFailureAttempts(noOfLoginAttempts);
		return user;
	}

	@Override
	public Boolean isLoginAtemptExceeded(User user) {
		user =userService.getUserByEmail(user.getEmail());
		return user!=null?user.getLogInFailureAttempts()>=3?true:false:false;
	}
	
}