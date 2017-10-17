package com.sevya.onemoney.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.service.UserService;
import com.sevya.onemoney.dto.GoalBudgetSummaryDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.service.GoalService;

@RestController
@RequestMapping(value="/api/v1")
public class GoalController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GoalService goalService;
	
	@RequestMapping(value="/goalaccounts",method=RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getUserGoalsAndAccounts() {
		
		logger.info("...getUserGoalsAndAccounts() view controller initiated...");
		Map<String,Object> map = new HashMap<>();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		map.put("goals",goalService.getUserGoalsAndAccounts(user));
			return new ResponseEntity<>(map,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		map.put("reponseMessage",e.getMessage());
    	}
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/goal/{month}/{year}",method=RequestMethod.GET)
    public ResponseEntity<List<GoalDto>> getGoal(@PathVariable Integer month,@PathVariable Integer year) {
		
		logger.info("...getGoal() view controller initiated...");
    	try{
    		User user = userService.getUserByAuthenticationToken();
			return new ResponseEntity<>(goalService.getGoals(user,month,year),HttpStatus.OK);
    	}catch(IOException e) {
    		logger.error(e.getMessage(), e);
    	}catch(URISyntaxException e1){ 
    		logger.error(e1.getMessage(), e1);
    	}catch(Exception e2){
    		logger.error(e2.getMessage(), e2);
    	}
		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/goal",method=RequestMethod.POST)
    public ResponseEntity<GoalDto> addGoal(@RequestBody GoalDto goalDto) {
		
		logger.info("...addGoal() view controller initiated...");
		GoalDto dto = new GoalDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		dto = goalService.saveGoal(user,goalDto);
    		return new ResponseEntity<>(dto,HttpStatus.OK);
		}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
		return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/goal",method=RequestMethod.PUT)
    public ResponseEntity<GoalDto> updateGoal(@RequestBody GoalDto goalDto) {
		
		logger.info("...updateGoal() view controller initiated...");
		GoalDto dto = new GoalDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			dto = goalService.editGoal(goalDto,user);
    		return new ResponseEntity<>(dto,HttpStatus.OK);
		}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
		return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/goal/{goalId}",method=RequestMethod.DELETE)
    public ResponseEntity<GoalDto> deleteGoal(@PathVariable Long goalId) {
		
		logger.info("...deleteGoal() view controller initiated...");
		GoalDto dto = new GoalDto();
		try {
			User user = userService.getUserByAuthenticationToken();
			goalService.deleteGoal(goalId,user);
			dto.setResponseMessage("Goal deleted successfully...!!!");
    		return new ResponseEntity<>(dto,HttpStatus.OK);
		
		} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
		return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
	}
	
	
	@RequestMapping(value="/goal/summary/{month}/{year}",method=RequestMethod.GET)
    public ResponseEntity<GoalBudgetSummaryDto> getGoalsAndBudgetSummary(@PathVariable Integer month,@PathVariable Integer year) {
		
		logger.info("...getGoalsAndBudgetSummary() view controller initiated...");
		try {
			User user = userService.getUserByAuthenticationToken();
			GoalBudgetSummaryDto goalBudgetSummaryDto = goalService.getGoalsAndBudgetSummary(user, month, year);
    		return new ResponseEntity<>(goalBudgetSummaryDto,HttpStatus.OK);
		
		} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    	}
		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/monthlyContrib",method=RequestMethod.POST)
    public ResponseEntity<GoalDto> suggestedMonthlyContribution(@RequestBody GoalDto goalDto) {
		
		logger.info("...suggestedMonthlyContribution() view controller initiated...");
		GoalDto dto =new GoalDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		dto.setSuggestedMonthlyContrib(goalService.getSuggetedMonthlyContribution(goalDto, user));
    		return new ResponseEntity<>(dto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		dto.setResponseMessage(e.getMessage());
    	}
		return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
	
}
