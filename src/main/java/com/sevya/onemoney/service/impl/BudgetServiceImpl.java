package com.sevya.onemoney.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.onemoney.dto.AccountSummaryDto;
import com.sevya.onemoney.dto.BudgetDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.GoalAccountDto;
import com.sevya.onemoney.dto.GoalAssetDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.RequestDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.dto.mapper.BudgetDtoMapper;
import com.sevya.onemoney.dto.mapper.GoalAssetDtoMapper;
import com.sevya.onemoney.dto.mapper.GoalDtoMapper;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.Budget;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.model.GoalAccountMapper;
import com.sevya.onemoney.model.GoalAssetMapper;
import com.sevya.onemoney.model.UserDetails;
import com.sevya.onemoney.repository.BudgetRepository;
import com.sevya.onemoney.service.AccountService;
import com.sevya.onemoney.service.BudgetService;
import com.sevya.onemoney.service.CategoryService;
import com.sevya.onemoney.service.GoalService;
import com.sevya.onemoney.service.UserDetailsService;
import com.sevya.onemoney.utility.CustomException;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.OneMoneyConstants;
import com.sevya.onemoney.utility.Purpose;
import com.sevya.onemoney.utility.Utility;


@Service
public class BudgetServiceImpl implements BudgetService {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BudgetRepository budgetRepository;
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BudgetDto addBudget(RequestDto requestDto,User user) throws Exception {
		
		Purpose purpose = null;
		Long parentCategoryId = 0l;
		BudgetDto budgetDto = new BudgetDto();
		
		for(BudgetDto dtoObj : requestDto.getBudgets()){
			
			if(dtoObj.getBudgetId() != null){
				
					Budget budget = budgetRepository.getBudget(dtoObj.getBudgetId());
					
					if(budget != null) {
						budget.setModifiedDate(new Date());
						budget = budgetRepository.save(budget);
					} else {
						throw new ResourceNotFoundException("Budget doesn't exists...!!!");
					}
					
					if(dtoObj.getPurpose() == null){
						dtoObj.setPurpose(dtoObj.getPurpose() == null ? budget.getPurpose().toString() : dtoObj.getPurpose());
					}
					
					if(dtoObj.getBudgetedAmount() == null){
						throw new ResourceNotFoundException("Budget amount is mandatory...!!!");
					}
					
					Budget newBudget = BudgetDtoMapper.toBudget(new Budget(), dtoObj, budget.getCategory());
					budgetRepository.save(newBudget);
					purpose = newBudget.getPurpose();
					if(budget.getCategory().getParentId() != null){
						parentCategoryId = budget.getCategory().getParentId();
					}else{
						parentCategoryId = budget.getCategory().getId();
					}
			}else{
				
				if(dtoObj.getPurpose() == null){
					throw new NullPointerException("Budget type is mandatory...!!!");
				}
				
				Category category = categoryService.getCategoryByUuid(dtoObj.getCategoryCode());
				
				if(category != null){
					Budget oldBudget = budgetRepository.getActiveBudgetByCategoryId(user.getId(),category.getId(),dtoObj.getPurpose().toUpperCase());
					if(oldBudget != null){
						throw new CustomException("Budget record already exists for this category. Consider sending update Budget call instead.");
					}
				}else{
					throw new CustomException("Category doesn't exists...!!!");
				}
				
				if(dtoObj.getBudgetedAmount() == null){
					throw new CustomException("Budget amount mandatory...!!!");
				}
				
				Budget budget = BudgetDtoMapper.toBudget(new Budget(), dtoObj, category);
				budgetRepository.save(budget);
				purpose = budget.getPurpose();
				if(category.getParentId() != null){
					parentCategoryId = category.getParentId();
				}else{
					parentCategoryId = category.getId();
				}
			}
		}
		
		if(budgetDto.getBudgetId() == null){
			budgetDto = createBudgetForParent(user,parentCategoryId,purpose);
		}
		return budgetDto; 
	}

	public BudgetDto createBudgetForParent(User user,Long parentCategoryId,Purpose purpose){
		
		Budget oldBudget = budgetRepository.getActiveBudgetByCategoryId(user.getId(),parentCategoryId,purpose.toString());
		if(oldBudget == null){
			Category category = categoryService.getCategoryById(parentCategoryId);
			Budget budget = new Budget();
			budget.setUuid(LaunchpadUtility.generateUUIDCode());
			budget.setCategory(category);
			budget.setPurpose(purpose);
			budgetRepository.save(budget);
		}
		
		Set<BudgetDto> subDtos = new HashSet<>();
		BudgetDto budgetDtoObj;
		
		Budget createdParentBudget = budgetRepository.getActiveBudgetByCategoryId(user.getId(),parentCategoryId,purpose.toString());
		
		budgetDtoObj = createParentCategoryWhileUpdate(createdParentBudget,user);
		BudgetDto parentExpenditures = getExpendituresFromCategoryTransationMapper(createdParentBudget.getCategory(),user,DateUtility.getCurrentMonth(),DateUtility.getCurrentYear());
		budgetDtoObj.setSpentAmount(parentExpenditures.getSpentAmount());
		budgetDtoObj.setAvgSpentAmount(parentExpenditures.getAvgSpentAmount());
		
		List<Budget> budgets = budgetRepository.getSubBudgetsByParentCategoryId(user.getId(),parentCategoryId,purpose.toString());
		
		budgets.forEach( budgetObj -> {
			BudgetDto subBudgetDto = BudgetDtoMapper.toBudgetDto(new BudgetDto(), budgetObj);
			BudgetDto childExpenditures = getExpendituresFromCategoryTransationMapper(budgetObj.getCategory(),user,DateUtility.getCurrentMonth(),DateUtility.getCurrentYear());
			subBudgetDto.setSpentAmount(childExpenditures.getSpentAmount());
			subBudgetDto.setAvgSpentAmount(childExpenditures.getAvgSpentAmount());
			subDtos.add(subBudgetDto);
		});
		
		UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());
		if(userDetails != null){
			if(OneMoneyConstants.BUSINESS.equals(budgetDtoObj.getPurpose())){
				budgetDtoObj.setMonthlyIncome(userDetails.getBusinessMonthlyIncome()==null?0f:userDetails.getBusinessMonthlyIncome());
			}else if(OneMoneyConstants.PERSONAL.equals(budgetDtoObj.getPurpose())){
				budgetDtoObj.setMonthlyIncome(userDetails.getPersonalMonthlyIncome()==null?0f:userDetails.getPersonalMonthlyIncome());
			}
		}
		
		budgetDtoObj.setSubCategories(subDtos);
		return budgetDtoObj;
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BudgetDto updateBudget(RequestDto requestDto,User user) throws Exception {
		
		Map<Long,Long> ids = new LinkedHashMap<>();
		List<Long> newBudgetIds = new ArrayList<>();
		
		for(BudgetDto budgetDto : requestDto.getBudgets()) {

			Budget budget = budgetRepository.getBudget(budgetDto.getBudgetId());
			
			if(budget != null) {
				budget.setModifiedDate(new Date());
				budget = budgetRepository.save(budget);
			} else {
				throw new ResourceNotFoundException("Budget doesn't exists...!!!");
			}
			
			if(budgetDto.getPurpose() == null){
				budgetDto.setPurpose(budgetDto.getPurpose() == null ? budget.getPurpose().toString() : budgetDto.getPurpose());
			}
			
			if(budgetDto.getBudgetedAmount() == null){
				throw new ResourceNotFoundException("Budget amount mandatory...!!!");
			}
			
			Budget newBudget = BudgetDtoMapper.toBudget(new Budget(), budgetDto, budget.getCategory());
			newBudget = budgetRepository.save(newBudget);
			newBudgetIds.add(newBudget.getId());
			ids.put(newBudget.getId(), budget.getId());
		}
		return getBudgetDtoWhileUpdate(ids,newBudgetIds,user);
	}
	
	public BudgetDto getBudgetDtoWhileUpdate(Map<Long,Long> ids,List<Long> newBudgetIds,User user){
		
		Budget createdParentBudget;
		Set<BudgetDto> subDtos = new HashSet<>();
		BudgetDto budgetDtoObj = new BudgetDto();
		Long parentCategoryId  = null;
		String purpose = "";
		
		Integer month = DateUtility.getCurrentMonth();
		Integer year = DateUtility.getCurrentYear();
		
		for (Map.Entry<Long, Long> entry : ids.entrySet()) {
			
			Budget budget = budgetRepository.getBudget(entry.getKey());
			
			if(budget.getCategory().getParentId() == null) {
				
					budgetDtoObj = createParentCategoryWhileUpdate(budget,user);
					budgetDtoObj.setPreviousBudgetId(entry.getValue());
					BudgetDto parentExpenditures = getExpendituresFromCategoryTransationMapper(budget.getCategory(),user,month,year);
					budgetDtoObj.setSpentAmount(parentExpenditures.getSpentAmount());
					budgetDtoObj.setAvgSpentAmount(parentExpenditures.getAvgSpentAmount());
					parentCategoryId = budget.getCategory().getId();
					purpose = budget.getPurpose().toString();
				
			} else {
				
				BudgetDto subBudgetDtoObj = BudgetDtoMapper.toBudgetDto(new BudgetDto(), budget);
				subBudgetDtoObj.setPreviousBudgetId(entry.getValue());
				BudgetDto expenditures = getExpendituresFromCategoryTransationMapper(budget.getCategory(),user,month,year);
				subBudgetDtoObj.setSpentAmount(expenditures.getSpentAmount());
				subBudgetDtoObj.setAvgSpentAmount(expenditures.getAvgSpentAmount());
				subDtos.add(subBudgetDtoObj);
					
				if(budgetDtoObj.getPreviousBudgetId() == null){
					createdParentBudget = budgetRepository.getParentBudgetBySubCategoryId(user.getId(), budget.getCategory().getId());
					budgetDtoObj = createParentCategoryWhileUpdate(createdParentBudget,user);
					BudgetDto parentExpenditures = getExpendituresFromCategoryTransationMapper(budget.getCategory(),user,month,year);
					budgetDtoObj.setSpentAmount(parentExpenditures.getSpentAmount());
					budgetDtoObj.setAvgSpentAmount(parentExpenditures.getAvgSpentAmount());
					
					List<Budget> budgets = budgetRepository.getSubBudgetsByParentCategoryIdAndNotInBudgetIds(user.getId(),createdParentBudget.getCategory().getId(),newBudgetIds);
					budgets.forEach( budgetObj -> {
						BudgetDto subBudgetDto = BudgetDtoMapper.toBudgetDto(new BudgetDto(), budgetObj);
						BudgetDto childExpenditures = getExpendituresFromCategoryTransationMapper(budgetObj.getCategory(),user,month,year);
						subBudgetDto.setSpentAmount(childExpenditures.getSpentAmount());
						subBudgetDto.setAvgSpentAmount(childExpenditures.getAvgSpentAmount());
						subDtos.add(subBudgetDto);
					});
				}
			}
		}
		
		budgetDtoObj.setSubCategories(subDtos);
		
		if(budgetDtoObj.getSubCategories().isEmpty()){
			List<Budget> budgets = budgetRepository.getSubBudgetsByParentCategoryId(user.getId(),parentCategoryId,purpose);
			budgets.forEach( budgetObj -> {
				BudgetDto subBudgetDto = BudgetDtoMapper.toBudgetDto(new BudgetDto(), budgetObj);
				BudgetDto childExpenditures = getExpendituresFromCategoryTransationMapper(budgetObj.getCategory(),user,month,year);
				subBudgetDto.setSpentAmount(childExpenditures.getSpentAmount());
				subBudgetDto.setAvgSpentAmount(childExpenditures.getAvgSpentAmount());
				subDtos.add(subBudgetDto);
			});
			budgetDtoObj.setSubCategories(subDtos);
		}
		
		return budgetDtoObj;
	}
	
	public BudgetDto createParentCategoryWhileUpdate(Budget budget,User user){
		BudgetDto budgetDtoObj = BudgetDtoMapper.toBudgetDto(new BudgetDto(), budget);
		budgetDtoObj.setLimit(budget.getMaxLimit());
		BudgetDto expenditures = getExpendituresFromCategoryTransationMapper(budget.getCategory(),user,DateUtility.getCurrentMonth(),DateUtility.getCurrentYear());
		budgetDtoObj.setSpentAmount(expenditures.getSpentAmount());
		budgetDtoObj.setAvgSpentAmount(expenditures.getAvgSpentAmount());
		budgetDtoObj.setBudgetedAmount(budget.getBudgetedAmount() == null ? 0f : budget.getBudgetedAmount());
		return budgetDtoObj;
	}

	@Override
	public void deleteBudget(User user,Long budgetId) {
		
		Budget budget = budgetRepository.getBudget(budgetId);
		if(budget != null){
			
			if(budget.getCategory().getParentId() == null){
				
				List<Budget> budgets = budgetRepository.getChildBudgetsbyParentCategoryId(user.getId(), budget.getCategory().getId());
				
				if(!budgets.isEmpty()){
					budgets.forEach( budgetObj -> {
						budgetObj.setModifiedDate(new Date());
						budgetRepository.save(budgetObj);
					});
				}
				
				budget.setModifiedDate(new Date());
				budgetRepository.save(budget);
			
			}else{
				
				Budget parentBudget = budgetRepository.getParentBudgetbyChildBudgetId(user.getId(),budget.getId());
				
				if(parentBudget.getBudgetedAmount() == null){
					List<Budget> budgets = budgetRepository.getChildBudgetsbyParentCategoryId(user.getId(), parentBudget.getCategory().getId());
					
					if(budgets.size() == 1){
						parentBudget.setModifiedDate(new Date());
						budgetRepository.save(parentBudget);
					}
				}
				
				budget.setModifiedDate(new Date());
				budgetRepository.save(budget);
			}
			
		}else{
			throw new ResourceNotFoundException("Budget doesn't exist...!!!");
		}
	}

	@Override
	public Budget getBudgetByCatagoryId(User user,Long categoryId,Purpose purpose) {
		return budgetRepository.getBudgetByCategoryId(user.getId(),categoryId,purpose);
	}
	
	@Override
	public List<BudgetDto> getAllBudgets(User user,Object month,Object year) throws Exception {
		
		String lastDateOfMonth;
		String firstDateOfNextMonth;
		
		if(month != null && year != null && month instanceof Integer && year instanceof Integer){
			lastDateOfMonth		  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth((Integer)year,(Integer)month,0);
			firstDateOfNextMonth  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth((Integer)year,(Integer)month,1);
		}else{
			throw new NullPointerException("Month and Year values are required or not supported format"); 
		}
		
		if(user == null){
			throw new ResourceNotFoundException("User doen'e exists...!!!");
		}else{
			List<Budget> budgets = budgetRepository.getBudgets(user.getId(),lastDateOfMonth,firstDateOfNextMonth);
			if(!budgets.isEmpty()){
				List<BudgetDto> dtos = new ArrayList<>();
				budgets.forEach(budget -> {
				BudgetDto dtoObj = (BudgetDto)getBudgetForCategory(user,budget,(Integer)month,(Integer)year,lastDateOfMonth,firstDateOfNextMonth,budget.getPurpose()).get("budgetDto");
					if(dtoObj!=null){dtos.add(dtoObj);} 
				});
				return dtos;
			}else{
				return new ArrayList<>();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> getBudgetForCategory(User user,Budget budget,Integer month,Integer year,String lastDateOfMonth,String firstDateOfMonth,Purpose purpose){
		
		Long spentAmount = 0l;
		Long avgSpentAmount = 0l;
		List<Long> subCategoryIds = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		
		BudgetDto dto = new BudgetDto();
		Category category = categoryService.getCategoryById(budget.getCategory().getId());
		if(category != null){
			
			dto = BudgetDtoMapper.toBudgetDto(dto, budget);

			List<Category> catagories = categoryService.getSubCategoriesByCategoryId(user,category.getId());
			
			if(!catagories.isEmpty()){
				Map<String,Object> subMap = getBudgetForSubCategories(dto,catagories,budget.getCreatedBy(),month,year,purpose);
				BudgetDto subBudgetDto = (BudgetDto)subMap.get("BudgetDto");
				spentAmount = spentAmount + (Long)subMap.get("spentAmount");
				avgSpentAmount = avgSpentAmount + (Long)subMap.get("avgSpentAmount");
				subCategoryIds = (List<Long>)subMap.get("subCategoryIds");
				dto.setSubCategories(subBudgetDto.getSubCategories());
			}
		
			BudgetDto expenses = getExpendituresFromCategoryTransationMapper(category,budget.getCreatedBy(),month,year);
			
			spentAmount = spentAmount + expenses.getSpentAmount();
			avgSpentAmount = avgSpentAmount + expenses.getAvgSpentAmount();
			
			dto.setSpentAmount(Math.abs(spentAmount));
			dto.setAvgSpentAmount(Math.abs(avgSpentAmount));
			dto.setLimit(budget.getMaxLimit());
						
		}else{
			throw new ResourceNotFoundException();
		}
		
		if(budget.getBudgetedAmount() == null) {
			dto.setBudgetedAmount(0f);
		}
		
		map.put("BudgetDto",dto);
		map.put("spentAmount",spentAmount);
		map.put("avgSpentAmount",avgSpentAmount);
		map.put("subCategoryIds",subCategoryIds);
		return map;
	}
	

	public Map<String,Object> getBudgetForSubCategories(BudgetDto dto,List<Category> catagories,User user,Integer month,Integer year,Purpose purpose){
		
		Map<String,Object> map = new HashMap<>();
		Long spentAmount = 0l;
		Long avgSpentAmount = 0l;
		List<Long> subCategoryIds = new ArrayList<>();
		
		Set<BudgetDto> subDtos = new HashSet<>();
		for(Category categoryObj : catagories){
			Budget budgetObj = budgetRepository.getActiveBudgetByCategoryId(user.getId(),categoryObj.getId(),purpose.toString());
			if(budgetObj != null){
				BudgetDto subDto = BudgetDtoMapper.toBudgetDto(new BudgetDto(), budgetObj);
				BudgetDto expenses = getExpendituresFromCategoryTransationMapper(categoryObj,user,month,year);
				subDto.setSpentAmount(Math.abs(expenses.getSpentAmount()));
				subDto.setAvgSpentAmount(Math.abs(expenses.getAvgSpentAmount()));
				subDtos.add(subDto);
				
				spentAmount = spentAmount + expenses.getSpentAmount();
				avgSpentAmount = avgSpentAmount + expenses.getAvgSpentAmount();
				subCategoryIds.add(categoryObj.getId());
			}
		}
		dto.setSubCategories(subDtos);
		map.put("BudgetDto",dto);
		map.put("spentAmount",spentAmount);
		map.put("avgSpentAmount",avgSpentAmount);
		map.put("subCategoryIds",subCategoryIds);
		return map;
	}
	

	public BudgetDto getExpendituresFromCategoryTransationMapper(Category category,User user,Integer month,Integer year) {
		
		BudgetDto dto = new BudgetDto();
		String last3rdMonthFirstDate;
		String previousMonthLastDate;
		
		if(month == null && year == null){
			previousMonthLastDate = DateUtility.getDates(OneMoneyConstants.NO_OF_MONTHS).get("previousMonthLastDate");
			last3rdMonthFirstDate = DateUtility.getDates(OneMoneyConstants.NO_OF_MONTHS).get("last3rdMonthFirstDate");
		}else{
			last3rdMonthFirstDate = DateUtility.getLast3rdMonthFirstDateAndPreviousMonthLastDate(year, month, 1,OneMoneyConstants.NO_OF_MONTHS);
			previousMonthLastDate = DateUtility.getLast3rdMonthFirstDateAndPreviousMonthLastDate(year, month, 0,OneMoneyConstants.NO_OF_MONTHS);
		}
		
		Float currentMonthSpent = categoryService.getTotalSpentForCurrentMonthByCategoryId(user.getId(),category.getId(),OneMoneyConstants.Income_Category,month, year);
		Float averageSpentMappers = categoryService.getAverageSpentForLastThreeMonthsByCategoryId(user.getId(),category.getId(),OneMoneyConstants.Income_Category, last3rdMonthFirstDate,previousMonthLastDate);
		
		Long avgSpent = 0l;
		if(averageSpentMappers != null && !averageSpentMappers.equals(0f)) {
			avgSpent = (long) Math.round(averageSpentMappers/OneMoneyConstants.NO_OF_MONTHS);
		}
		
		if(currentMonthSpent == null){
			currentMonthSpent = 0f;
		}
		
		dto.setSpentAmount(currentMonthSpent.longValue());
		dto.setAvgSpentAmount(avgSpent);
		
		return dto;
	}
	
	
	@Override
	public Float getAllBudgetsOfUserPerMonth(Long userId, Integer month, Integer year) {
		return  budgetRepository.getAllBudgetsByUserMonthAndYear(userId, month, year);
	}

	
	@Override
	public Budget getTotalAmountBudgetedForIncome(Long userId, String categoryName, Object month, Object year) {
		
		String	lastDateOfMonth		  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth((Integer)year,(Integer)month,0);
		String	firstDateOfNextMonth  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth((Integer)year,(Integer)month,1);
		return budgetRepository.getTotalIncomeBudgeted(userId, categoryName, lastDateOfMonth, firstDateOfNextMonth);
	}


	@Override
	public Float getTotalAmountBudgeted(Long userId, String categoryName, Object month, Object year) {
		return budgetRepository.getTotalBudgetedAmountExcludingIncomeCategory(userId, categoryName, (Integer)month, (Integer)year);
	}
	
	
	@Override
	public Map<String,Float> getUserBudgetInfoByMonthAndYear(User user,String purpose, Integer month, Integer year){
		
		Float totalBudgeted = 0f;
		Float budgetSpents = 0f;
		
		Map<String,Float> amounts = new HashMap<>();
		
		String lastDateOfMonth		 = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth(year,month,0);
		String firstDateOfNextMonth  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth(year,month,1);
		
		List<Budget> budgets = budgetRepository.getBudgetsForSummary(user.getId(),lastDateOfMonth,firstDateOfNextMonth,purpose);
		Integer totalBudgets = budgets.size();
		
		for(Budget budget : budgets) {
		
			if(budget.getCategory().getParentId() == null && budget.getBudgetedAmount() != null) {
				totalBudgeted = totalBudgeted + budget.getBudgetedAmount();
			} else {
				
				List<Budget> budgetList = budgetRepository.getChildBudgetsbyParentCategoryId(user.getId(), budget.getCategory().getId());
				for(Budget b : budgetList){
					totalBudgeted = totalBudgeted + b.getBudgetedAmount();
				}
			}
			Float spents = categoryService.totalSpentsForUserByPurposeCategoryMonthAndYear(user.getId(), purpose, budget.getCategory().getName(), month, year);
			if(spents != null)
				budgetSpents = budgetSpents + spents;
			
		}
		
		amounts.put("budgetedAmount", totalBudgeted);
		amounts.put("budgetSpents", Math.abs(budgetSpents));
		amounts.put("totalBudgets", (float)totalBudgets);
		return amounts;
		
	}

	/** Budget And Goal Summary api call @author satish 
	 * @throws Exception **/ 
	@Override
	public ResponseDto getBudgetsAndGoalsSummary(User user,List<GoalDto> goalDtos) throws Exception {
		
		ResponseDto reponse = new ResponseDto();
		Integer month = DateUtility.getCurrentMonth();
		Integer year = DateUtility.getCurrentYear();
		String lastDateOfMonth		  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth(year,month,0);
		String firstDateOfNextMonth  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth(year,month,1);
		
		UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getId());
		
		/** Budget Summary Business **/
		AccountSummaryDto budgetSummaryBusiness = getBudgetSummaryForBusinessAndPersonal(user, lastDateOfMonth, firstDateOfNextMonth, month, year,OneMoneyConstants.BUSINESS);
		budgetSummaryBusiness.setMonthlyIncome(userDetails.getBusinessMonthlyIncome() == null ? 0l : userDetails.getBusinessMonthlyIncome().longValue());
		
		/** Budget Summary Personal **/
		AccountSummaryDto budgetSummaryPersonal = getBudgetSummaryForBusinessAndPersonal(user, lastDateOfMonth, firstDateOfNextMonth, month, year,OneMoneyConstants.PERSONAL);
		budgetSummaryPersonal.setMonthlyIncome(userDetails.getPersonalMonthlyIncome() == null ? 0l : userDetails.getPersonalMonthlyIncome().longValue());
		
		/** Goal Summary Business **/
		AccountSummaryDto goalSummaryBusiness = getGoalSummaryForBusinessAndPersonal(user,goalDtos,Purpose.B);
		
		/** Goal Summary Personal **/
		AccountSummaryDto goalSummaryPersonal = getGoalSummaryForBusinessAndPersonal(user,goalDtos,Purpose.P);
		
		ResponseDto budgetSummary = new ResponseDto();
		budgetSummary.setMessage("Welcome ...!!!");
		budgetSummary.setBusiness(budgetSummaryBusiness);
		budgetSummary.setPersonal(budgetSummaryPersonal);
		
		ResponseDto goalSummary = new ResponseDto();
		goalSummary.setMessage("Welcome ...!!!");
		goalSummary.setBusiness(goalSummaryBusiness);
		goalSummary.setPersonal(goalSummaryPersonal);
		
		/** Final Response **/
		reponse.setBudgetSummary(budgetSummary);
		reponse.setGoalSummary(goalSummary);
		
		Integer accounts = accountService.checkingAccountsAvailabilityByUserAndPurpose(user.getId(), Purpose.B);
		Boolean isBudgetAvailable = budgetSummary.getBusiness().getBudgets().isEmpty() && budgetSummary.getPersonal().getBudgets().isEmpty();
		Boolean isGoalAvailable = goalSummary.getBusiness().getGoals().isEmpty() && goalSummary.getPersonal().getGoals().isEmpty();
		Boolean flag = false;
		if(!isBudgetAvailable && !isGoalAvailable && accounts > 0){
			flag = true;
		}
		reponse.setBusinessFlag(flag);
		return reponse;
	}
	
	@SuppressWarnings("unchecked")
	public AccountSummaryDto getBudgetSummaryForBusinessAndPersonal(User user,String lastDateOfMonth,String firstDateOfNextMonth,Integer month,Integer year,String purpose) throws Exception {
		
		Long spentAmountBusiness = 0l;
		Long avgSpentAmountBusiness = 0l;
		Float budgetedAmountBusiness = 0f;
		Long businessUnBudgetdAmount = 0l;
		
		AccountSummaryDto business = new AccountSummaryDto();
		
		List<BudgetDto> businessBudgets = new ArrayList<>();
		List<Budget> budgets = budgetRepository.getBudgetsForSummary(user.getId(),lastDateOfMonth,firstDateOfNextMonth,purpose);
			
		for(Budget budget : budgets){
			Double unBudgetedSubCategoriesAmount = 0d;
			Map<String,Object> budgetsMap = getBudgetForCategory(user,budget,month,year,lastDateOfMonth,firstDateOfNextMonth,Utility.getPurposeEnum(purpose));
			BudgetDto dtoObj = (BudgetDto)budgetsMap.get("BudgetDto");
			
			spentAmountBusiness = spentAmountBusiness + (Long)budgetsMap.get("spentAmount");
			avgSpentAmountBusiness = avgSpentAmountBusiness + (Long)budgetsMap.get("avgSpentAmount");
			
			Map<String,Object> map = getUnBudgetedSubCategories(user, budgetsMap, dtoObj, month, year,purpose);
			unBudgetedSubCategoriesAmount = unBudgetedSubCategoriesAmount + (long)map.get("unBudgetedSubCategoriesAmount");
			
			dtoObj.setUnBudgetedAmount(Math.round(unBudgetedSubCategoriesAmount));
			dtoObj.setUnbudgetedCategories((Set<BudgetDto>) map.get("businessUnbudgetedSubCategories"));
			budgetedAmountBusiness = budgetedAmountBusiness + dtoObj.getBudgetedAmount();
			businessBudgets.add(dtoObj);
		}
		business.setBudgets(businessBudgets);
		
		List<BudgetDto> businessUnbudgetedMainCategories = new ArrayList<>();
 		List<Object[]> businessUnBudgtesCategoires = categoryService.getUnBudgedCategoriesByUserIdAndMonthAndYear(user.getId(),purpose, month, year);
 		
 		List<String> parent = new ArrayList<>();
 		List<String> parentFromChild = new ArrayList<>();
 		for(Object[] array : businessUnBudgtesCategoires) {
 			if(array.length != 0){
 				BudgetDto dto = new BudgetDto();
 				CategoryDto categoryDto = new CategoryDto();
 				
 				if(array[3] == null){
 					parent.add(array[2].toString());
	 				categoryDto.setCategoryName(array[0].toString());
		 			categoryDto.setCategoryCode(array[1].toString());
		 			dto.setCategory(categoryDto);
		 			 
		 			BigInteger amount = categoryService.getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose
		 					(user.getId(),((BigInteger)array[2]).longValue(),purpose,month,year);
		 			long amonutValue = amount != null ? Math.abs(amount.longValue()) : 0l;
		 			dto.setSpentAmount(amonutValue);
		 			businessUnBudgetdAmount = businessUnBudgetdAmount + amonutValue;
 				}else{
 					
 					Budget budget = budgetRepository.getActiveBudgetByCategoryId(user.getId(), Long.parseLong(array[3].toString()), purpose);   /***/
 					if(budget!=  null){																										 	/***/
						continue;
 					}
 					
 					if(parentFromChild.isEmpty()){																									/***/
 						parentFromChild.add(array[3].toString());																					/***/
 	 				}else{																															/***/
 	 					if(parentFromChild.contains(array[3].toString()) || parent.contains(array[3].toString())){									/** This code for remove  **/ 
 		 					continue;																												/** duplicate categories. **/
 		 				}else{																														/***/
 		 					parentFromChild.add(array[3].toString());																				/***/
 		 				}																															/***/
 	 				}
 					
 					Category category = categoryService.getCategoryById(Long.parseLong(array[3].toString()));
 					if(category != null){
	 					categoryDto.setCategoryName(category.getName());
			 			categoryDto.setCategoryCode(category.getUuid());
			 			dto.setCategory(categoryDto);
			 			
			 			BigInteger amount = categoryService.getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose
			 					(user.getId(),category.getId(),purpose,month,year);
			 			long amonutValue = amount != null ? Math.abs(amount.longValue()) : 0l;
			 			dto.setSpentAmount(amonutValue);
			 			businessUnBudgetdAmount = businessUnBudgetdAmount + amonutValue;
 					}
 				}
 				businessUnbudgetedMainCategories.add(dto);
 			}
 		}
		business.setUnbudgetedCategories(new LinkedHashSet<>(businessUnbudgetedMainCategories));
		business.setAvgSpentAmount(Math.abs(avgSpentAmountBusiness));
		business.setBudgetedAmount(budgetedAmountBusiness);
		business.setSpentAmount(Math.abs(spentAmountBusiness));
		business.setUnBudgetedAmount(Math.abs(businessUnBudgetdAmount));
		return business;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getUnBudgetedSubCategories(User user,Map<String,Object> budgetsMap,BudgetDto dtoObj,Integer month,Integer year,String purpose){
		
		Long unBudgetedSubCategoriesAmount = 0l;
		Map<String,Object> map = new HashMap<>();
		List<Long> ids = new ArrayList<>();
		List<Long> subBudgetedCategoryIds = (List<Long>) budgetsMap.get("subCategoryIds");
		List<BigInteger> subCategoryIds = categoryService.getSubCategoryIdsByParentCategoryId(user,dtoObj.getCategory().getCategoryCode());
		
		for(BigInteger value  : subCategoryIds){
			ids.add(value.longValue());
		}
		
		for(Long id : subBudgetedCategoryIds){
			if(ids.contains(id)){
				ids.remove(id);
			}
			if(ids.isEmpty())
				ids.add(0l);
		}
		
		List<BudgetDto> businessUnbudgetedSubCategories = new ArrayList<>();
		
		if(!ids.isEmpty()) {
			List<Object[]> ubBudgetedCategories = categoryService.getUnBudgedSubCategoriesByUserIdAndMonthAndYearAndSubCatagoryIds(user.getId(),purpose,ids,month,year);
			for(Object[] array : ubBudgetedCategories) {
				BudgetDto dto = new BudgetDto();
					
	 			CategoryDto categoryDto = new CategoryDto();
	 			categoryDto.setCategoryName(array[0].toString());
	 			categoryDto.setCategoryCode(array[1].toString());
	 			dto.setCategory(categoryDto);
	 			
	 			BigInteger amount = categoryService.getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose(user.getId(),((BigInteger)array[2]).longValue(),purpose,month,year);
	 			long amonutValue = amount != null ? Math.abs(amount.longValue()) : 0l;
	 			
	 			dto.setSpentAmount(amonutValue);
	 			businessUnbudgetedSubCategories.add(dto);
	 			unBudgetedSubCategoriesAmount = unBudgetedSubCategoriesAmount + amonutValue;
	 		}
		}
		map.put("businessUnbudgetedSubCategories",new LinkedHashSet<>(businessUnbudgetedSubCategories));
		map.put("unBudgetedSubCategoriesAmount",unBudgetedSubCategoriesAmount);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public AccountSummaryDto getGoalSummaryForBusinessAndPersonal(User user,List<GoalDto> goalDtos,Purpose purpose) throws Exception {
		
		AccountSummaryDto goalSummary = new AccountSummaryDto();
		List<GoalDto> dtos = new ArrayList<>();
		
		Long totalSavedAmount = 0l;
		Long totalTargetAmount = 0l;
		
		List<GoalDto> inputDtos = new ArrayList<>();
		
		if(goalDtos == null || goalDtos.isEmpty()) {
			List<Goal> goals = goalService.getGoalsByUserId(user.getId());
			for(Goal g : goals) {
				GoalDto dto = new GoalDto();
				List<GoalAccountMapper> goalAccountMappers = goalService.getGoalAccountMappersByGoalIdAndUserId(user.getId(), g.getId());
				List<GoalAccountDto> goalAccountDtos = new ArrayList<>();
				if(!goalAccountMappers.isEmpty()){
					goalAccountMappers.forEach(accc -> {
						GoalAccountDto goalAccountDto = new GoalAccountDto();
						goalAccountDto.setAccountId(accc.getAccount().getAccountCode());
						goalAccountDto.setBalance(accc.getAccount().getMinBalance());
						goalAccountDtos.add(goalAccountDto);
					});
				}
				dto.setAccounts(goalAccountDtos);
				dto.setGoalId(g.getId());
				inputDtos.add(dto);
			}
		}else{
			inputDtos = goalDtos;
		}
		
		if(inputDtos != null && !inputDtos.isEmpty()) {
			for(GoalDto inputGoalDto : inputDtos) {
				GoalDto outputGoalDto = new GoalDto();
				Goal goal = goalService.getGoalByIdAndPurpose(inputGoalDto.getGoalId(),purpose);
				
				
				List<GoalAccountDto> goalAccountDtos = inputGoalDto.getAccounts() !=null ? inputGoalDto.getAccounts() : new ArrayList<>();
				
				if(goal != null){
					outputGoalDto.setGoalId(goal.getId());
					outputGoalDto.setName(goal.getName());
					outputGoalDto.setTargetAmount(goal.getTargetAmount());
					outputGoalDto.setPurpose(goal.getPurpose().toString());
					outputGoalDto.setTargetDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(goal.getTargetDate()));
					
					Map<String,Object> map = getGoalDtoForGoalSummary(user, outputGoalDto, inputGoalDto, goal);
					outputGoalDto = (GoalDto) map.get("goalDto");
					List<GoalAssetMapper> goalAssetMappers = (List<GoalAssetMapper>) map.get("goalAssetMappers");
					List<GoalAccountMapper> goalAccountMappers = (List<GoalAccountMapper>) map.get("goalAccountMappers");
					

					Date targetedDate =	DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(outputGoalDto.getTargetDate());
					outputGoalDto.setCreatedAt(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(goal.getCreatedDate()));
					outputGoalDto.setTimeToTarget(GoalDtoMapper.calculateTimeTotarget(targetedDate));
					outputGoalDto.setPropertyTimeToTarget(GoalDtoMapper.calculatePropertyTimeToTarget(targetedDate,Calendar.getInstance().getTime()));
					outputGoalDto.setPropertyFV(GoalAssetDtoMapper.calculatePropertyFV(goalAssetMappers,outputGoalDto));
					outputGoalDto.setPropertyCV(GoalAssetDtoMapper.calculatePropertyCV(goalAssetMappers,goal.getCreatedDate()));
					outputGoalDto.setAccountBalanceFV(GoalDtoMapper.calculateAccountBalanceFV(goalAccountMappers, outputGoalDto));
					outputGoalDto.setSavingsTarget((long)(goal.getTargetAmount() - outputGoalDto.getPropertyFV() - outputGoalDto.getAccountBalanceFV()));
					outputGoalDto.setMonthlyContrib(goal.getInstallmentAmount());
					outputGoalDto.setSuggestedMonthlyContrib(goalAccountMappers.isEmpty() ? goalAssetMappers.isEmpty() 
							? 0 : GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, outputGoalDto) 
							: GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, outputGoalDto));
					outputGoalDto.setContribFV(GoalDtoMapper.calculateContribFV(goalAccountMappers, outputGoalDto));
					
					outputGoalDto.setTotalFV(outputGoalDto.getPropertyFV() + outputGoalDto.getAccountBalanceFV() + outputGoalDto.getContribFV());
					outputGoalDto.setSavedAmount(GoalDtoMapper.calculateSavedAmount(goalAccountDtos,outputGoalDto));
					outputGoalDto.setShortfall((long) ((goal.getTargetAmount() - outputGoalDto.getTotalFV()) < 0 ? 0
							: (goal.getTargetAmount() - outputGoalDto.getTotalFV())));
					outputGoalDto.setExpectedAdditionalSavings(GoalDtoMapper.calculateExpectedAdditionalSavings(goalAccountMappers, outputGoalDto));
					outputGoalDto.setExpectedAccountBalance(GoalDtoMapper.calculateExpectedAccountBalance(goalAccountMappers, outputGoalDto));
					outputGoalDto.setMonthlyAdditionalContrib(GoalDtoMapper.calculateMonthlyAdditionalContrib(goalAccountMappers, outputGoalDto));
					outputGoalDto.setExpectedSavings(outputGoalDto.getSavedAmount() + outputGoalDto.getExpectedAdditionalSavings());
					
					totalSavedAmount =  totalSavedAmount + outputGoalDto.getSavedAmount();
					totalTargetAmount = (long) (totalTargetAmount + goal.getTargetAmount());
					
					
					if(!goal.getGoalAchieved()) {
						dtos.add(outputGoalDto);
						outputGoalDto.setGoalAchieved(false);
						outputGoalDto.setMessage("Good going...!!!");
					}

				}
			}
		}
		goalSummary.setGoals(dtos);
		goalSummary.setTotalSavedAmount(totalSavedAmount);
		goalSummary.setTotalTargetAmount(totalTargetAmount);
		
		return goalSummary;
	}
	
	public Map<String,Object> getGoalDtoForGoalSummary(User user,GoalDto outputGoalDto,GoalDto inputGoalDto,Goal goal) throws Exception{
		
		List<GoalAssetDto> goalAssetDtos = new ArrayList<>();
		List<GoalAssetMapper> goalAssetMappers = goalService.getGoalAssetMappersByGoalIdAndUserId(user.getId(),inputGoalDto.getGoalId());
		
		if(!goalAssetMappers.isEmpty()){
			goalAssetMappers.forEach(assets -> {
				GoalAssetDto goalAssetDto = new GoalAssetDto();
				goalAssetDto.setProportion(assets.getProportion());
				goalAssetDto.setAssetId(assets.getAsset().getId());
				goalAssetDto.setName(assets.getAsset().getName());
				goalAssetDto.setGrowthRate(assets.getAsset().getGrowthRate());
				goalAssetDto.setCurrentValue(assets.getAsset().getOriginalValue());
				goalAssetDtos.add(goalAssetDto);
			});
		}
		outputGoalDto.setAssets(goalAssetDtos);
		
		List<GoalAccountMapper> goalAccountMappers = new ArrayList<>();
		List<GoalAccountDto> goalAccountDtos = new ArrayList<>();
		if(inputGoalDto.getAccounts() != null && !inputGoalDto.getAccounts().isEmpty()){
			for(GoalAccountDto account : inputGoalDto.getAccounts() ) {
				GoalAccountMapper goalAccountMapper = goalService.getGoalAccountMappersByGoalIdAndAccountCodeAndUserId(user.getId(),inputGoalDto.getGoalId(),account.getAccountId());
				if(goalAccountMapper != null){
					GoalAccountDto goalAccountDto = new GoalAccountDto();
					goalAccountDto.setAccountId(account.getAccountId());
					goalAccountDto.setRateOfInterest( goalAccountMapper.getRateOfInterest());
					goalAccountDto.setProportion(goalAccountMapper.getProportion());
					goalAccountDtos.add(goalAccountDto);
					
					Account a = goalAccountMapper.getAccount();
					a.setMinBalance(account.getBalance() == null ? 0 : account.getBalance());
					goalAccountMappers.add(goalAccountMapper);
				}
			}
		}else{
			List<GoalAccountMapper> mappers = goalService.getGoalAccountMappersByGoalIdAndUserId(user.getId(),inputGoalDto.getGoalId());
			mappers.forEach(mapper -> {
				GoalAccountMapper gAccounts = new GoalAccountMapper();
				Account a = mapper.getAccount();
				a.setMinBalance(0f);
				gAccounts.setAccount(a);
				gAccounts.setRateOfInterest(mapper.getRateOfInterest() == null ? 0 : mapper.getRateOfInterest());
				goalAccountMappers.add(gAccounts);
			});
		}
		outputGoalDto.setAccounts(goalAccountDtos);
		
		Map<String,Object> map = new HashMap<>();
		map.put("goalDto",outputGoalDto);
		map.put("goalAssetMappers",goalAssetMappers);
		map.put("goalAccountMappers",goalAccountMappers);
		return map;
	}
	
}
