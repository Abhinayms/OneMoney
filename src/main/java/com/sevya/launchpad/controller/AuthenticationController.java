package com.sevya.launchpad.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sevya.launchpad.SessionVariables;
import com.sevya.launchpad.dto.AuthenticatedUserDto;
import com.sevya.launchpad.dto.LoginCredentialsDto;
import com.sevya.launchpad.dto.ResetPasswordDto;
import com.sevya.launchpad.dto.UserDto;
import com.sevya.launchpad.dto.mapper.UserDtoMapper;
import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.security.jwt.JwtAuthenticatedUserDto;
import com.sevya.launchpad.security.jwt.JwtService;
import com.sevya.launchpad.service.AuthenticationService;

@RestController
@RequestMapping("")
public class AuthenticationController extends BaseController{

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
		
	@Autowired	
	private AuthenticationService authenticationService;
	
	@Autowired
	private JwtService jwtService;
	
	 @RequestMapping(path = "/api/authenticate", method = RequestMethod.POST)
	    public AuthenticatedUserDto tokenLogin(@RequestBody LoginCredentialsDto loginCredentialsDto, HttpServletResponse response) {
		 AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(); 
		 	if(loginCredentialsDto.getCountryCode() == null){
		 		authenticatedUserDto.setResponseMessage("Country code is required...!!!");
		 		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		 		return authenticatedUserDto;
		 	}
		 
		 	if(loginCredentialsDto.getMobile() == null){
		 		authenticatedUserDto.setResponseMessage("Mobile number is required...!!!");
		 		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		 		return authenticatedUserDto;
		 	}
	    	User user = authenticationService.authenticate(loginCredentialsDto.getMobile(), loginCredentialsDto.getCountryCode(), loginCredentialsDto.getPassword());
	    	
	    	
	    	if (user != null){
	    		if(user.getPassword().equals( DigestUtils.md5Hex(loginCredentialsDto.getPassword())))
	    		{	    		
	    			 
	    			authenticatedUserDto.setMobile(user.getMobile());
	    			authenticatedUserDto.setCountryCode(user.getCountryCode());
	    			authenticatedUserDto.setAvatarUrl(user.getAvtar());
	    			authenticatedUserDto.setName(user.getFirstName());
	    			
	    		try {
	    			
	    			JwtAuthenticatedUserDto jwtAuthenticatedUserDto = new  JwtAuthenticatedUserDto();
	    			jwtAuthenticatedUserDto.setMobile(user.getMobile());
	    			jwtAuthenticatedUserDto.setUserId(user.getId());
	    			jwtAuthenticatedUserDto.setUuid(user.getUuid());
	    			String token = jwtService.tokenFor(jwtAuthenticatedUserDto);
	    			authenticatedUserDto.setToken(token);
	    			response.setHeader("Token", token);
	            
	            } catch (Exception e) {
	                logger.error(e.getMessage());
	            	throw new RuntimeException(e);            
	            }
	            
	            response.setStatus(HttpServletResponse.SC_OK);
	            
	            return authenticatedUserDto;
	            
	    		}
	        	else{
	        		
	        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        	}
	    		
	    	}
	    	else{
	    		
	    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    		
	    	}    	
	    	
	    	return null;
	    }
    
	@RequestMapping(value="/spa/authenticate", method=RequestMethod.GET)
	public ResponseEntity<UserDto> login() {
		
		try{
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			User user = (User)auth.getPrincipal();
			
			if(user == null)
			{
				throw new ResourceNotFoundException("Not Authoorized");
			}
			
			UserDto userDto = UserDtoMapper.toUserDto(user);
			
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}		
	}
	
	@RequestMapping(value="/session/authenticate", method=RequestMethod.POST)
	public ResponseEntity<UserDto> sessionlogin(@RequestBody LoginCredentialsDto loginCredentialsDto) {
		
		try{
			
			User user = authenticationService.authenticate(loginCredentialsDto.getEmail(), null, loginCredentialsDto.getPassword());			
			UserDto userDto = UserDtoMapper.toUserDto(user);
			
			SessionVariables.setUser(user);
			
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}		
	}
	
	@RequestMapping(value="/user/password", method=RequestMethod.POST)
	public ResponseEntity<JwtAuthenticatedUserDto> forgotPassword(@RequestBody JwtAuthenticatedUserDto authenticationDto,HttpServletRequest request) {
		
		logger.info("forgotPassword() controller initiated");
		
		try{
			Boolean flag =authenticationService.forgotPassword(request,authenticationDto.getEmail());
			if(flag){
				return new ResponseEntity<JwtAuthenticatedUserDto>(authenticationDto,HttpStatus.OK);
			}else{
				throw new ResourceNotFoundException("Email not registered.");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}		
	}	
	
	@RequestMapping(value="/user/resetpassword", method=RequestMethod.POST)
	public ResponseEntity<JwtAuthenticatedUserDto> resetpassword(@RequestBody ResetPasswordDto resetPasswordDto) {
		
		logger.info("forgotPassword() controller initiated");
		try{
			
			Boolean flag =authenticationService.resetPassword(resetPasswordDto);
			if(flag){
				return new ResponseEntity<JwtAuthenticatedUserDto>(HttpStatus.OK);
			}else{
				throw new ResourceNotFoundException("Not Authoorized");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
	}	
}