package com.sevya.onemoney.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.service.UserService;
import com.sevya.onemoney.dto.BudgetDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.service.BudgetService;

@CrossOrigin
@Controller
@RequestMapping(value="/api/v1")
public class BudgetController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private BudgetService budgetService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/budget/{month}/{year}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<BudgetDto>> getBudgets(@PathVariable Integer month,@PathVariable Integer year) {
		
		logger.info("...getBudgets() view controller initiated...");
		List<BudgetDto> budgetDtos  = new ArrayList<>();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			return new ResponseEntity<>(budgetService.getAllBudgets(user,month,year),HttpStatus.OK);
    	}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		BudgetDto dto = new BudgetDto();
    		dto.setResponseMessage(e.getMessage());
    		budgetDtos.add(dto);
    	}
    	return new ResponseEntity<>(budgetDtos,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/budgetgoal/summary",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseDto> getBudgetsAndGoalsSummary(@RequestBody Map<String,List<GoalDto>> goalDtos) {
		
		logger.info("...getBudgetsAndGoalsSummary() view controller initiated...");
		ResponseDto responseDto  = new ResponseDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			responseDto = budgetService.getBudgetsAndGoalsSummary(user,goalDtos.get("goals"));
			return new ResponseEntity<>(responseDto,HttpStatus.OK);
    	}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		responseDto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/budget",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BudgetDto> createBudget(@RequestBody RequestDto dto) {
    	logger.info("...createBudget() controller initiated...");
    	BudgetDto budgetDto  = new BudgetDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			budgetDto  = budgetService.addBudget(dto,user);
			return new ResponseEntity<>(budgetDto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		budgetDto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(budgetDto,HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/budget",method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<BudgetDto> editBudget(@RequestBody RequestDto dto) {
    	logger.info("...editBudget() controller initiated...");
    	BudgetDto budgetDto  = new BudgetDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		return new ResponseEntity<>(budgetService.updateBudget(dto,user),HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		budgetDto.setResponseMessage(e.getMessage());
    	}
        return new ResponseEntity<>(budgetDto,HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/budget/{id}",method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<BudgetDto> deleteBudget(@PathVariable Long id) {
    	logger.info("...deleteBudget() controller initiated...");
    	BudgetDto budgetDto  = new BudgetDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			budgetService.deleteBudget(user,id);
			budgetDto.setResponseMessage("Budget deleted successfully...!!!");
    		return new ResponseEntity<>(budgetDto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		budgetDto.setResponseMessage(e.getMessage());
    	}
        return new ResponseEntity<>(budgetDto,HttpStatus.BAD_REQUEST);
    }

}
