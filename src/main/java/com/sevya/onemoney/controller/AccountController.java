package com.sevya.onemoney.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.service.UserService;
import com.sevya.onemoney.dto.AccountDto;
import com.sevya.onemoney.dto.AccountGroupDto;
import com.sevya.onemoney.dto.PaginationDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.dto.TransactionDto;
import com.sevya.onemoney.dto.TxRequestDto;
import com.sevya.onemoney.service.AccountService;
import com.sevya.onemoney.service.TransactionService;

@CrossOrigin
@RestController
@RequestMapping(value="/api/v1")
public class AccountController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/accounts/summary",method=RequestMethod.GET)
    public ResponseEntity<ResponseDto> accountSummary() {
		logger.info("...accountSummary() view controller initiated...");
		ResponseDto response = new ResponseDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			response = accountService.getAccountSummary(user);
			return new ResponseEntity<>(response,HttpStatus.OK);
    	}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/accounts",method=RequestMethod.POST)
    public ResponseEntity<AccountGroupDto> addAccounts(@RequestBody AccountGroupDto accountGroupDto) {
    	logger.info("...addAccounts() controller initiated...");
    	AccountGroupDto dto = new AccountGroupDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		dto = accountService.addAccounts(user,accountGroupDto);
			return new ResponseEntity<>(dto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/accounts",method=RequestMethod.PUT)
    public ResponseEntity<ResponseDto> updateAccount(@RequestBody RequestDto dto) {
    	logger.info("...updateAccount() controller initiated...");
    	ResponseDto response = new ResponseDto();
    	try{
    		accountService.updateAccounts(dto);
			return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/accounts/{accountGroupId}/{accountId}",method=RequestMethod.DELETE)
    public ResponseEntity<ResponseDto> unlinkAccounts(@PathVariable Long accountGroupId,@PathVariable String accountId) {
    	logger.info("...unlinkAccounts() controller initiated...");
    	ResponseDto response = new ResponseDto();
    	try{
			User user = userService.getUserByAuthenticationToken();
    		accountService.unlickAccounts(user,accountGroupId, accountId);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/accounts/{accountGroupId}",method=RequestMethod.DELETE)
    public ResponseEntity<ResponseDto> deleteInstitution(@PathVariable Long accountGroupId) {
    	logger.info("...deleteInstitution() controller initiated...");
    	ResponseDto response = new ResponseDto();
    	try{
			User user = userService.getUserByAuthenticationToken();
    		accountService.removeInstitutionByGroupId(user,accountGroupId);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/accounts/list",method=RequestMethod.GET)
    public ResponseEntity<List<AccountGroupDto>> getAllAccounts() {
		logger.info("...getAllAccounts() view controller initiated...");
		try{
			User user = userService.getUserByAuthenticationToken();
			return new ResponseEntity<>(accountService.getAccountsList(user),HttpStatus.OK);
    	}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value="/accounts/{accountTypeGroup}/{purpose}",method=RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccounts(@PathVariable String accountTypeGroup,@PathVariable String purpose) {
		logger.info("...getAllAccounts() view controller initiated...");
		AccountDto accountDto = new AccountDto();
		try{
			User user = userService.getUserByAuthenticationToken();
			accountDto = accountService.getAccountsByAccountTypeAndPurpose(user,accountTypeGroup,purpose);
			return new ResponseEntity<>(accountDto,HttpStatus.OK);
    	}catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		accountDto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(accountDto,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/accounts/sync",method=RequestMethod.POST)
    public ResponseEntity<ResponseDto> uploadAccountTransactions(@RequestBody RequestDto requestDto) {
    	logger.info("...uploadAccountTransactions() controller initiated...");
    	ResponseDto response = new ResponseDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			transactionService.uploadAccountTransactions(user,requestDto);
			return new ResponseEntity<>(HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

	@RequestMapping(value="/transactions",method=RequestMethod.POST)
    public ResponseEntity<PaginationDto> transactions(@RequestBody TxRequestDto txRequestDto) {
    	logger.info("...transactions() controller initiated...");
    	PaginationDto paginationDto = new PaginationDto();
    	try {
			User user = userService.getUserByAuthenticationToken();
			paginationDto = transactionService.getAllTransactions(user, txRequestDto);
			return new ResponseEntity<>(paginationDto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		paginationDto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(paginationDto,HttpStatus.BAD_REQUEST);
    }
	
	@RequestMapping(value="/transactions",method=RequestMethod.PUT)
    public ResponseEntity<TransactionDto> recategoriseTransactions(@RequestBody TransactionDto transactionDto) {
    	logger.info("...recategoriseTransactions() controller initiated...");
    	TransactionDto dto = new TransactionDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		dto = transactionService.recategoriseTransaction(user,transactionDto);
			return new ResponseEntity<>(dto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }

	@RequestMapping(value="/transactions/{fingerprint}",method=RequestMethod.GET)
    public ResponseEntity<TransactionDto> transactionDetails(@PathVariable String fingerprint) {
    	logger.info("...transactionDetails() controller initiated...");
    	TransactionDto dto = new TransactionDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			dto = transactionService.getTransactionDetails(user,fingerprint);
			return new ResponseEntity<>(dto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }

	@RequestMapping(value="/transactions/split",method=RequestMethod.POST)
    public ResponseEntity<TransactionDto> splitTransactions(@RequestBody TransactionDto transactionDto) {
    	logger.info("...splitTransactions() controller initiated...");
    	TransactionDto dto = new TransactionDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
    		dto = transactionService.splitTransactions(user,transactionDto);
			return new ResponseEntity<>(dto,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		dto.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
	
	
	
	@RequestMapping(value="/transactions/suggestions",method=RequestMethod.POST)
    public ResponseEntity<ResponseDto> searchSuggestions(@RequestBody TxRequestDto txRequestDto) {
    	logger.info("...searchSuggestions() controller initiated...");
    	ResponseDto response = new ResponseDto();
    	try{
    		User user = userService.getUserByAuthenticationToken();
			Set<String> suggestions = transactionService.searchSuggestions(user, txRequestDto);
			response.setData(suggestions);
			return new ResponseEntity<>(response,HttpStatus.OK);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		response.setResponseMessage(e.getMessage());
    	}
    	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
	
   
}
