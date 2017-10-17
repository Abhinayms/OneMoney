package com.sevya.onemoney.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sevya.launchpad.controller.UserController;
import com.sevya.launchpad.dto.ChangePasswordDto;
import com.sevya.launchpad.dto.UserDto;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.model.UserRequests.RequestType;
import com.sevya.launchpad.service.UserService;
import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.dto.UserDetailsDto;
import com.sevya.onemoney.dto.mapper.UserDetailsDtoMapper;
import com.sevya.onemoney.model.UserDetails;
import com.sevya.onemoney.service.UserDetailsService;

@CrossOrigin
@Controller
@RequestMapping(value="/api/v1")
public class AppUserController extends UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> createUser(@RequestBody UserDetailsDto userDetailsDto,HttpServletRequest request) {
		
		logger.info("AppUserController createUser()");
		ResponseDto response = new ResponseDto();
		
		try {
			if(userDetailsDto.getCountryCode() == null){
				throw new NullPointerException("Country code need to send...!!!");
			}
			
			User user = userService.getExistedUserByMobileAndCountryCode(userDetailsDto.getMobile(),userDetailsDto.getCountryCode());
			
			if(userDetailsDto.getMobile() == null) {
				
				throw new NullPointerException("Mobile number need to send...!!!");
			
			}else if(user != null && user.getIsActive()){
				
				throw new NullPointerException("Mobile number already registered...!!!"); 
			
			} else if(user != null && !user.getIsActive()){
			
				userService.generateRequestOTP(user,LaunchpadUtility.getIP(request),RequestType.activation);
				return new ResponseEntity<>(HttpStatus.OK);
				
			} else {
				
				user = UserDetailsDtoMapper.toUser(userDetailsDto);
				user = userService.createUser(user);
				UserDetails userDetails = UserDetailsDtoMapper.toUserDetails(user, new UserDetails(), userDetailsDto);
				userDetailsService.createUserDetails(userDetails);
				userService.generateRequestOTP(user,LaunchpadUtility.getIP(request),RequestType.activation);
				user.setCreatedBy(user);
				userService.updateUser(user);
				return new ResponseEntity<>(HttpStatus.OK);
			
			}
			
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
		}
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
	@RequestMapping(value = "/profile", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto> updateProfile(@RequestBody UserDetailsDto userDetailsDto){
		
		logger.info("AppUserController updateProfile()");
		ResponseDto response = new ResponseDto();
		
		try {
			User user = userService.getUserByAuthenticationToken();
			if(user == null) {
				throw new NullPointerException("User doesn't exists...!!!"); 
			}
			user = UserDetailsDtoMapper.toUser(user,userDetailsDto);
			user = userService.updateUser(user);
			
			UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());
			if(userDetails == null) {
				throw new NullPointerException("User doesn't exists...!!!"); 
			}
			
			userDetails = UserDetailsDtoMapper.toUserDetails(user, userDetails, userDetailsDto);
			userDetailsService.createUserDetails(userDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
		}
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ResponseEntity<UserDetailsDto> getUserProfile(){
		
		logger.info("AppUserController getUserProfile()");
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		
		try {
			User user = userService.getUserByAuthenticationToken();
			if(user == null) {
				throw new NullPointerException("User doesn't exists...!!!"); 
			}
			UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());
			userDetailsDto = UserDetailsDtoMapper.toUserDetailsDto(userDetails);
			return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
		
		}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		userDetailsDto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(userDetailsDto,HttpStatus.BAD_REQUEST);
		
	}
	
	
	@RequestMapping(value = "/password/change", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
		
		logger.info("AppUserController changePassword()");
		ResponseDto response = new ResponseDto();
		
		try {
			User user = userService.getUserByAuthenticationToken();
			if(user == null) {
				throw new NullPointerException("User doesn't exists...!!!"); 
			}
			
			if(user.getPassword().equals(DigestUtils.md5Hex(changePasswordDto.getOldPassword()))){
				user.setPassword(DigestUtils.md5Hex(changePasswordDto.getNewPassword()));
				userService.updateUser(user);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				throw new NullPointerException("Old password doesn't match...!!!"); 
			}
			
		}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
	@RequestMapping(value = "/password/forgot", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> ForgotPassword(@RequestBody UserDto userDto,HttpServletRequest request){
		
		logger.info("AppUserController ForgotPassword()");
		ResponseDto response = new ResponseDto();
		try {
			if(userDto.getCountryCode() == null){
				throw new NullPointerException("Country code is required...!!!");
			}
			if(userDto.getMobile() == null){
				throw new NullPointerException("Mobile number is required...!!!");
			}
			User user = userService.getExistedUserByMobileAndCountryCode(userDto.getMobile(),userDto.getCountryCode());
			if(user == null) {
				throw new NullPointerException("User doesn't exists with this Mobile...!!!");  
			} else if(!user.getIsActive()) {
				throw new NullPointerException("Please Activate your account...!!!");  
			} 
			userService.generateRequestOTP(user,LaunchpadUtility.getIP(request),RequestType.forgotPassword);
			return new ResponseEntity<>(HttpStatus.OK);
		
		}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	@RequestMapping(value = "/validateotp", method = RequestMethod.POST)
	public ResponseEntity<UserDetailsDto> ValidateOTP(@RequestBody UserDto userDto) {
		
		logger.info("AppUserController ValidateOTP()");
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		
		try {
			if(userDto.getCountryCode() == null){
				throw new NullPointerException("Country code is required...!!!");
			}
			if(userDto.getMobile() == null){
				throw new NullPointerException("Mobile number is required...!!!");
			}
			User user = userService.getExistedUserByMobileAndCountryCode(userDto.getMobile(),userDto.getCountryCode());
			if(user == null) {
				throw new NullPointerException("User doesn't exists with this Mobile...!!!");  
			}
			UserRequests userRequests = userService.getUserRequestsByMobileAndCountryCodeAndActivationKey(user.getMobile(), user.getCountryCode(), userDto.getOtp());
			if(userRequests == null) {
				throw new NullPointerException("Please enter valid OTP...!!!");  
			}
			userRequests = userService.validateToken(userRequests);
			String token = userService.getAuthTokenByUser(user);
			userDetailsDto = UserDetailsDtoMapper.toUserDetailsDto(userRequests);
			userDetailsDto.setToken(token);
			return new ResponseEntity<>(userDetailsDto,HttpStatus.OK);
		
		}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		userDetailsDto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(userDetailsDto,HttpStatus.BAD_REQUEST);
		
	}
	
	
	@RequestMapping(value = "/password/reset", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> ResetPassword(@RequestBody ChangePasswordDto changePasswordDto){
		
		logger.info("AppUserController ResetPassword()");
		ResponseDto response = new ResponseDto();
		
		try {
			User user = userService.getUserByAuthenticationToken();
			if(user == null) {
				throw new NullPointerException("User doesn't existed...!!!"); 
			}
			user.setPassword(DigestUtils.md5Hex(changePasswordDto.getNewPassword()));
			userService.updateUser(user);
			return new ResponseEntity<>(HttpStatus.OK);
		
		}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
	
	
}
