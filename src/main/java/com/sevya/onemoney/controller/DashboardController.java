package com.sevya.onemoney.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.service.UserService;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.mapper.UserDetailsDtoMapper;
import com.sevya.onemoney.model.UserDetails;
import com.sevya.onemoney.service.AccountService;
import com.sevya.onemoney.service.CategoryService;
import com.sevya.onemoney.service.GoalService;
import com.sevya.onemoney.service.UserDetailsService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1")
public class DashboardController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private CategoryService categoryService;

	
	
	@RequestMapping(value="/init",method=RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> init() {
		
		logger.info("...init() view controller initiated...");
		Map<String,Object> map = new HashMap<>();
    	
		try {
    		
    		User user = userService.getUserByAuthenticationToken();
    		UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());
    		map.put("goals",goalService.getUserGoalsAndAccounts(user));
    		map.put("userProfile",UserDetailsDtoMapper.toUserDetailsDto(userDetails));
    		map.put("accounts",accountService.getAccountsList(user));
    		map.put("categories",categoryService.getCategories(user));
			return new ResponseEntity<>(map,HttpStatus.OK);
			
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		map.put("reponseMessage",e.getMessage());
    	}
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }
	
	
	
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> dashboard(@RequestBody Map<String,List<GoalDto>> goalDtos) {
		
		logger.info("...dashboard() view controller initiated...");
		
		try {
			
			User user = userService.getUserByAuthenticationToken();
			Map<String,Object> dashBoardDetails = accountService.dashBoard(user,goalDtos.get("goals"));
			return new ResponseEntity<>(dashBoardDetails, HttpStatus.OK);
		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	
	}

}
