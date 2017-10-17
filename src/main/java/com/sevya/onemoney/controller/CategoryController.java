package com.sevya.onemoney.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.service.CategoryService;

@CrossOrigin
@Controller
@RequestMapping(value="/api/v1")
public class CategoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private CategoryService catagoryService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/category",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseDto> getCatagories(HttpServletRequest request) {
    	logger.info("...getCatagories() view controller initiated...");
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		return new ResponseEntity<>(catagoryService.getCategories(user),HttpStatus.OK);
    	}catch(Exception e){
    		 logger.error(e.getMessage(), e);
    	}	    	
    	
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/category",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CategoryDto> createCatagory(@RequestBody CategoryDto dto) {
    	logger.info("...createCatagory() controller initiated...");
    	try{
    		return new ResponseEntity<>(catagoryService.addCategory(dto),HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);   	
    	}
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/category",method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity editCatagory(@RequestBody CategoryDto dto) {
    	logger.info("...editCatagory() controller initiated...");
    	try{
    		catagoryService.updateCategory(dto);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);   	
    	}
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/category/{categoryCode}",method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteCatagory(@PathVariable String categoryCode) {
    	logger.info("...deleteCatagory() controller initiated...");
    	try{
    		catagoryService.deleteCategoryByUuid(categoryCode);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e); 	
    	}
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/subcategory",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CategoryDto> createSubCatagory(@RequestBody CategoryDto dto) {
    	logger.info("...createSubCatagory() controller initiated...");
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		return new ResponseEntity<>(catagoryService.addSubCategory(user,dto),HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);  	
    	}
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/subcategory",method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CategoryDto> editSubCatagory(@RequestBody CategoryDto dto) {
    	logger.info("...editSubCatagory() controller initiated...");
    	try{
    		return new ResponseEntity<>(catagoryService.updateSubCategory(dto),HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);  	
    	}
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/subcategory/{categoryCode}",method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteSubCatagory(@PathVariable String categoryCode) {
    	logger.info("...deleteSubCatagory() controller initiated...");
    	try{
    		catagoryService.deleteCategoryByUuid(categoryCode);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);    	
    	}
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
    
    
}
