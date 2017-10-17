package com.sevya.onemoney.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.GoalAccountDto;
import com.sevya.onemoney.dto.GoalAssetDto;
import com.sevya.onemoney.dto.GoalBudgetSummaryDto;
import com.sevya.onemoney.dto.GoalDto;
import com.sevya.onemoney.dto.mapper.AssetDtoMapper;
import com.sevya.onemoney.dto.mapper.GoalAccountDtoMapper;
import com.sevya.onemoney.dto.mapper.GoalAssetDtoMapper;
import com.sevya.onemoney.dto.mapper.GoalDtoMapper;
import com.sevya.onemoney.model.Account;
import com.sevya.onemoney.model.Asset;
import com.sevya.onemoney.model.Budget;
import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.model.GoalAccountMapper;
import com.sevya.onemoney.model.GoalAssetMapper;
import com.sevya.onemoney.repository.AccountRepository;
import com.sevya.onemoney.repository.AssetRepository;
import com.sevya.onemoney.repository.BudgetRepository;
import com.sevya.onemoney.repository.GoalAccountMapperRepository;
import com.sevya.onemoney.repository.GoalAssetMapperRepository;
import com.sevya.onemoney.repository.GoalsRepository;
import com.sevya.onemoney.service.CategoryService;
import com.sevya.onemoney.service.GoalService;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.OneMoneyConstants;
import com.sevya.onemoney.utility.Purpose;

@Service
public class GoalServiceImpl implements GoalService {
	
	@Autowired
	private GoalsRepository goalsRepository;
	
	@Autowired
	private AssetRepository assetRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private GoalAccountMapperRepository goalAccountMapperRepository;
	
	@Autowired
	private GoalAssetMapperRepository goalAssetMapperRepository;

	@Autowired
	private BudgetRepository budgetRepository;
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public List<GoalDto> getGoals(User user,Integer month,Integer year) throws Exception {

		List<Goal> goals = goalsRepository.getGoals(user.getId(),month,year);
		List<GoalDto> dtos = new ArrayList<>();
		for(Goal goal : goals){
			GoalDto dto = GoalDtoMapper.toGoalDto(new GoalDto(), goal);
			dto.setSuggestedMonthlyContrib(Math.round(getSuggestedAmountWhileGetGoals(goal,user)));
			Double suggestedAmonut = getSuggestedAmountWhileGetGoals(goal,user);
			
			if(suggestedAmonut < 0){
				dto.setGoalAchieved(true);
				dto.setMessage("Your goal is already achieved...!!!");
				dto.setSuggestedMonthlyContrib(0l);
			}else{
				dto.setGoalAchieved(false);
				dto.setMessage("Good going...!!!");
				dto.setSuggestedMonthlyContrib(Math.abs(Math.round(suggestedAmonut)));
			}
			
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public Double getSuggestedAmountWhileGetGoals(Goal goal, User user) throws Exception{
		Double suggestedAmonut = 0d;
		
		List<GoalAssetMapper> mappers = goalAssetMapperRepository.getGoalAssetMappersByGoalIdAndUserId(goal.getId(), user.getId());
		
		Float targetAmout = 0f;
		Date targetDate = null;
		Float assetsValue = 0f;
		
		if(!mappers.isEmpty()){
		
			for(GoalAssetMapper asset : mappers){
				targetAmout = asset.getGoal().getTargetAmount();
				targetDate = asset.getGoal().getTargetDate();
				assetsValue =  assetsValue + asset.getAsset().getOriginalValue() * asset.getProportion();
			}
		
		}else{
			
			Goal goalObj = goalsRepository.getGoalById(goal.getId());
			
			targetAmout = goalObj.getTargetAmount();
			targetDate = goalObj.getTargetDate();
			assetsValue =  0f;
			
		}
		
		long months = DateUtility.getMonthDifferenceBetween2Dates(new Date(), targetDate);
		
		if(targetAmout > 0){
			suggestedAmonut = (double) ((targetAmout-assetsValue)/months);
		}
		
		return suggestedAmonut;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public GoalDto saveGoal(User user,GoalDto goalDto) throws Exception {
		
		List<GoalAssetMapper> goalAssetMappers = new ArrayList<>();
		List<GoalAccountMapper> goalAccountMappers = new ArrayList<>();
		GoalDto dto = new GoalDto();
		
		if(goalDto.getName() == null || goalDto.getMonthlyContrib() == null || goalDto.getTargetAmount() == null || goalDto.getTargetDate() == null){
			throw new NullPointerException("Some fields are missing while creating of Goal...!!!");
		}
		
		if(new Date().getTime() > DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(goalDto.getTargetDate()).getTime()){
			throw new NullPointerException("Target date must be future date...!!!");
		}
		
		Goal goal = GoalDtoMapper.toGoal(new Goal(), goalDto);
		goal = goalsRepository.save(goal);
 		
		if(goalDto.getAccounts() != null && !goalDto.getAccounts().isEmpty()){
			goalAccountMappers = saveGoalAccountMapperWhileAddGoal(user,goalDto,goal);
		}
		
		if(goalDto.getAssets() != null && !goalDto.getAssets().isEmpty()){
			goalAssetMappers = saveGoalAssetMapperWhileAddGoal(goalDto, goal);
		}
		
		dto = GoalDtoMapper.toGoalDto(dto, goal);
		dto.setAccounts(goalDto.getAccounts());
		dto.setAssets(goalDto.getAssets());
		
		Date targetedDate = DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(goalDto.getTargetDate());
		dto.setCreatedAt(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(goal.getCreatedDate()));
		dto.setTimeToTarget(GoalDtoMapper.calculateTimeTotarget(targetedDate));
		dto.setPropertyTimeToTarget(
				GoalDtoMapper.calculatePropertyTimeToTarget(targetedDate, Calendar.getInstance().getTime()));
		dto.setPropertyFV(GoalAssetDtoMapper.calculatePropertyFV(goalAssetMappers, dto));
		dto.setPropertyCV(GoalAssetDtoMapper.calculatePropertyCV(goalAssetMappers, goal.getCreatedDate()));
		dto.setAccountBalanceFV(GoalDtoMapper.calculateAccountBalanceFV(goalAccountMappers, dto));
		dto.setSavingsTarget((long) (goal.getTargetAmount() - dto.getPropertyFV() - dto.getAccountBalanceFV()));
		dto.setMonthlyContrib(goal.getInstallmentAmount());
		dto.setSuggestedMonthlyContrib(goalAccountMappers.isEmpty()
				? goalAssetMappers.isEmpty() ? 0
						: GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, dto)
				: GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, dto));
		dto.setContribFV(GoalDtoMapper.calculateContribFV(goalAccountMappers, dto));
		dto.setAccountBalanceFV(GoalDtoMapper.calculateAccountBalanceFV(goalAccountMappers, dto));
		dto.setTotalFV(dto.getPropertyFV() + dto.getAccountBalanceFV() + dto.getContribFV());
		dto.setShortfall((long) ((goal.getTargetAmount() - dto.getTotalFV()) < 0 ? 0
				: (goal.getTargetAmount() - dto.getTotalFV())));
		dto.setSavedAmount(GoalDtoMapper.calculateSavedAmount(goalDto.getAccounts(), dto));
		dto.setExpectedAdditionalSavings(GoalDtoMapper.calculateExpectedAdditionalSavings(goalAccountMappers, dto));
		dto.setExpectedAccountBalance(GoalDtoMapper.calculateExpectedAccountBalance(goalAccountMappers, dto));
		dto.setMonthlyAdditionalContrib(GoalDtoMapper.calculateMonthlyAdditionalContrib(goalAccountMappers, dto));
		dto.setExpectedSavings(dto.getSavedAmount() + dto.getExpectedAdditionalSavings());
		
		if(dto.getGoalAchieved()){
			dto.setMessage("Your goal is already achieved...!!!");
		} else {
			dto.setMessage("Good going...!!!");
		}
		return dto;
		
	}
	
	public List<GoalAccountMapper> saveGoalAccountMapperWhileAddGoal(User user,GoalDto goalDto,Goal goal) throws Exception {
		
		List<GoalAccountMapper> goalAccountMappers = new ArrayList<>();
 		
		for(GoalAccountDto account : goalDto.getAccounts()) {
 			
			Account accountObj = accountRepository.getAccountByAccountCode(user.getId(),account.getAccountId());
 			if(accountObj == null){
 				throw new NullPointerException("Account doesn't exists...!!!");
 			}
 			
 			if(account.getProportion() == null ||  account.getRateOfInterest() == null){
 				throw new NullPointerException("Some fields are missing while adding account to Goal...!!!");
 			}
 			
 			GoalAccountMapper goalAccountMapper = new GoalAccountMapper();
 			goalAccountMapper.setAccount(accountObj);
 			goalAccountMapper.setGoal(goal);
 	 		goalAccountMapper.setProportion(account.getProportion());
 	 		goalAccountMapper.setRateOfInterest(account.getRateOfInterest());
 	 		goalAccountMapperRepository.save(goalAccountMapper);
 	 		goalAccountMapper.getAccount().setMinBalance(account.getBalance()); /*For calculating account balance*/
 	 		goalAccountMappers.add(goalAccountMapper);
 		}
 		return goalAccountMappers;
	}
	

	public List<GoalAssetMapper> saveGoalAssetMapperWhileAddGoal(GoalDto goalDto,Goal goal) throws Exception {
		
		List<GoalAssetMapper> goalAssetMappers = new ArrayList<>();
		
		for(GoalAssetDto asset : goalDto.getAssets()) {
 			
 			if(asset.getName() == null || asset.getCurrentValue() == null || asset.getProportion() == null || asset.getGrowthRate() == null){
 				throw new NullPointerException("Some fiels are missing while creating of Assest...!!!");
 			}
 			
 			GoalAssetMapper goalAssetMapper = new GoalAssetMapper();
 	 		
 			Asset newAsset = new Asset();
 	 		newAsset.setName(asset.getName());
 	 		newAsset.setOriginalValue(asset.getCurrentValue());
 	 		newAsset.setGrowthRate(asset.getGrowthRate());
 	 		newAsset = assetRepository.save(newAsset);
 	 		
 	 		goalAssetMapper.setGoal(goal);
 	 		goalAssetMapper.setAsset(newAsset);
 	 		goalAssetMapper.setProportion(asset.getProportion());
 	 		goalAssetMapperRepository.save(goalAssetMapper);	 		
 	 		goalAssetMappers.add(goalAssetMapper);
 		
 	 	}
		return goalAssetMappers;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public GoalDto editGoal(GoalDto goalDto,User user) throws Exception {
		
		List<GoalAssetMapper> goalAssetMappers = new ArrayList<>();
		List<GoalAccountMapper> goalAccountMappers = new ArrayList<>();
		GoalDto dto = new GoalDto();
		
		Goal goal = goalsRepository.getGoalById(goalDto.getGoalId());
		
		if(goal == null) {
			throw new ResourceNotFoundException("Goal does not exists");
		}
		
		goal = GoalDtoMapper.toGoal(goal, goalDto);
		goalsRepository.save(goal);
		List<GoalAccountDto> accounts = goalDto.getAccounts() == null
				? new ArrayList<>() : goalDto.getAccounts() ; 
		List<GoalAssetDto> assests = goalDto.getAssets(); 
		
		if( !accounts.isEmpty() ) {
			goalAccountMappers = editAccounts(goal,user,accounts);
		}
		
		if(assests != null) {
			goalAssetMappers = editAssets(goal,user,assests);
		}
		
		if(goalDto.getTargetAmount() == null ){
			goalDto.setTargetAmount(goal.getTargetAmount());
		}
		
		if(goalDto.getTargetDate() == null ){
			goalDto.setTargetDate(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(goal.getTargetDate()));
		}
		
		if(new Date().getTime() > DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(goalDto.getTargetDate()).getTime() ){
			throw new NullPointerException("Target date must be future date...!!!");
		}
		
		dto = GoalDtoMapper.toGoalDto(dto, goal);
		dto.setAccounts(goalDto.getAccounts());
		dto.setAssets(goalDto.getAssets());
		
		Date targetedDate = DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(goalDto.getTargetDate());
		dto.setCreatedAt(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(goal.getCreatedDate()));
		dto.setTimeToTarget(GoalDtoMapper.calculateTimeTotarget(targetedDate));
		dto.setPropertyTimeToTarget(
				GoalDtoMapper.calculatePropertyTimeToTarget(targetedDate, Calendar.getInstance().getTime()));
		dto.setPropertyFV(GoalAssetDtoMapper.calculatePropertyFV(goalAssetMappers, dto));
		dto.setPropertyCV(GoalAssetDtoMapper.calculatePropertyCV(goalAssetMappers, goal.getCreatedDate()));
		dto.setAccountBalanceFV(GoalDtoMapper.calculateAccountBalanceFV(goalAccountMappers, dto));
		dto.setSavingsTarget((long) (goal.getTargetAmount() - dto.getPropertyFV() - dto.getAccountBalanceFV()));
		dto.setMonthlyContrib(goal.getInstallmentAmount());
		dto.setSuggestedMonthlyContrib(goalAccountMappers.isEmpty()
				? goalAssetMappers.isEmpty() ? 0
						: GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, dto)
				: GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, dto));
		dto.setContribFV(GoalDtoMapper.calculateContribFV(goalAccountMappers, dto));
		dto.setAccountBalanceFV(GoalDtoMapper.calculateAccountBalanceFV(goalAccountMappers, dto));
		dto.setTotalFV(dto.getPropertyFV() + dto.getAccountBalanceFV() + dto.getContribFV());
		dto.setShortfall((long) ((goal.getTargetAmount() - dto.getTotalFV()) < 0 ? 0
				: (goal.getTargetAmount() - dto.getTotalFV())));
		dto.setSavedAmount(GoalDtoMapper.calculateSavedAmount(accounts, dto));
		dto.setExpectedAdditionalSavings(GoalDtoMapper.calculateExpectedAdditionalSavings(goalAccountMappers, dto));
		dto.setExpectedAccountBalance(GoalDtoMapper.calculateExpectedAccountBalance(goalAccountMappers, dto));
		dto.setMonthlyAdditionalContrib(GoalDtoMapper.calculateMonthlyAdditionalContrib(goalAccountMappers, dto));
		dto.setExpectedSavings(dto.getSavedAmount() + dto.getExpectedAdditionalSavings());
		
		if(dto.getGoalAchieved()) {
			dto.setMessage("Your goal is already achieved...!!!");
		} else {
			dto.setMessage("Good going...!!!");
		}
		
		return dto;
	}
	
	public List<GoalAssetMapper> editAssets(Goal goal,User user,List<GoalAssetDto> goalAssetDtos) throws Exception {
		
		List<GoalAssetMapper> goalAssetMappers = new ArrayList<>();
	
		for( GoalAssetDto goalAssetDto : goalAssetDtos ) {
			
			if( goalAssetDto.getAssetId() !=null ) {
				Asset assetObj = assetRepository.getAssetById(goalAssetDto.getAssetId());
				if(assetObj == null) {
					throw new ResourceNotFoundException("Asset doesn't exists...!!!");
				}
				assetObj = AssetDtoMapper.toAsset(assetObj,goalAssetDto);
				assetRepository.save(assetObj);
				
				GoalAssetMapper goalAssetMapper = goalAssetMapperRepository.getGoalAssetMappersByGoalIdAssetIdAndUserId(goal.getId(), goalAssetDto.getAssetId(), user.getId());
				if(goalAssetMapper != null) {
					GoalAssetDtoMapper.toGoalAssetMapper(goalAssetMapper,goalAssetDto);
					goalAssetMapperRepository.save(goalAssetMapper);
					goalAssetMappers.add(goalAssetMapper);
				}
				
			} else {
				
				if( goalAssetDto.getName() ==null || goalAssetDto.getGrowthRate() ==null || goalAssetDto.getProportion()==null || goalAssetDto.getCurrentValue()==null ) {
					throw new ResourceNotFoundException("Asset fields are mandatory...!!!");
				}
				Asset newAsset =  AssetDtoMapper.toAsset(goalAssetDto);
				newAsset = assetRepository.save(newAsset);
				GoalAssetMapper goalAssetMapper = GoalAssetDtoMapper.toGoalAssetMapper(goalAssetDto, goal, newAsset);
				goalAssetMapperRepository.save(goalAssetMapper);
				goalAssetMappers.add(goalAssetMapper);
			
			}
		}
		return goalAssetMappers;
	}
	
	
	
	public List<GoalAccountMapper> editAccounts(Goal goal,User user,List<GoalAccountDto> goalAccountDtos) throws Exception {
		
		List<GoalAccountMapper> goalAccountMappers = new ArrayList<>();
		
		for( GoalAccountDto goalAccountDto : goalAccountDtos ) {
			
			GoalAccountMapper goalAccountMapper = goalAccountMapperRepository.getGoalAccountMapperByGoalIdAccountCodeAndUserId(goal.getId(),goalAccountDto.getAccountId(), user.getId());
 	 		
			if(goalAccountMapper != null) {
				
				goalAccountMapper = GoalAccountDtoMapper.toGoalAccountMapper(goalAccountMapper, goalAccountDto);
	 	 		goalAccountMapperRepository.save(goalAccountMapper);
	 	 		goalAccountMapper.getAccount().setMinBalance(goalAccountDto.getBalance()); /*For calculating account balance*/
	 	 		goalAccountMappers.add(goalAccountMapper);
 	 		
			} else {
 	 			
				if( goalAccountDto.getProportion() ==null || goalAccountDto.getRateOfInterest() ==null ) {
					throw new ResourceNotFoundException("Account fields are mandatory...!!!");
				}
 	 			
				Account accountObj = accountRepository.getAccountByAccountCode(user.getId(),goalAccountDto.getAccountId());
 	 			if( accountObj == null) {
 	 				throw new ResourceNotFoundException("Account doesn't exists...!!!");
 	 			}
 	 			
 	 			GoalAccountMapper goalAccountMapper1 = GoalAccountDtoMapper.toGoalAccountMapper(goalAccountDto, goal, accountObj);
 	 	 		goalAccountMapperRepository.save(goalAccountMapper1);

 	 	 		goalAccountMapper1.getAccount().setMinBalance(goalAccountDto.getBalance()); /*For calculating account balance*/
 	 	 		goalAccountMappers.add(goalAccountMapper1);
 	 		}
		}
		return goalAccountMappers;
	}

	
	
	@Override
	public void deleteGoal(Long goalId,User user){
		
		Goal goal = goalsRepository.getGoalById(goalId);
		
		if(goal == null) {
			throw new ResourceNotFoundException("Goal does not exists");
		}
		
		List<GoalAccountMapper> goalAccountMappers = goalAccountMapperRepository.getGoalAccountMappersByGoalIdAndUserId(goalId, user.getId());
		
		List<GoalAssetMapper> goalAssetMappers = goalAssetMapperRepository.getGoalAssetMappersByGoalIdAndUserId(goalId, user.getId());
		
		for(GoalAccountMapper goalAccountMapper : goalAccountMappers){
			goalAccountMapper.setIsActive(false);
			goalAccountMapperRepository.save(goalAccountMapper);
		}
		
		for(GoalAssetMapper goalAssetMapper : goalAssetMappers){
			goalAssetMapper.setIsActive(false);
			goalAssetMapperRepository.save(goalAssetMapper);
		}
		
		goal.setIsActive(false);
		goalsRepository.save(goal);
		
	}

	@Override
	public GoalBudgetSummaryDto getGoalsAndBudgetSummary(User user, Integer month, Integer year) {
		
		Float totalAvailable = 0f;
		GoalBudgetSummaryDto goalBudgetSummaryDto = new GoalBudgetSummaryDto();
		
		String last3rdMonthFirstDate = DateUtility.getLast3rdMonthFirstDateAndPreviousMonthLastDate(year, month, 1,OneMoneyConstants.NO_OF_MONTHS);
		String previousMonthLastDate = DateUtility.getLast3rdMonthFirstDateAndPreviousMonthLastDate(year, month, 0,OneMoneyConstants.NO_OF_MONTHS);
		
		Map<String,Float> amounts = getTotalBudgetedAmount(user,month,year);
		Float totalBudgeted = amounts.get("totalBudgeted");
		Float totalIncomeBudgeted = amounts.get("totalIncomeBudgeted");
		Float totalIncomeCredited = categoryService.getTotalAmountForCategory(user.getId(), OneMoneyConstants.Income_Category, month, year);
		//Float totalSaving = goalsRepository.getAllGoalBudgetsOfUserPerMonth(user.getId(), month, year);
		Float totalSpent = categoryService.getTotalSpentForCurrentMonthByCategoryName(user.getId(),OneMoneyConstants.Income_Category,month, year);
		Float avgSpent = categoryService.getAverageSpentForLastThreeMonthsByCategoryName(user.getId(),OneMoneyConstants.Income_Category,last3rdMonthFirstDate,previousMonthLastDate);
		if(totalIncomeBudgeted != null && totalBudgeted != null){
			totalAvailable = totalIncomeBudgeted - totalBudgeted;
		}
		
		goalBudgetSummaryDto.setTotalAvailable(totalAvailable.intValue());
		//goalBudgetSummaryDto.setTotalSaving(totalSaving !=null ? totalSaving.intValue() : 0);
		goalBudgetSummaryDto.setAvgSpent(avgSpent !=null ? Math.abs(avgSpent.intValue()/OneMoneyConstants.NO_OF_MONTHS) : 0);
		goalBudgetSummaryDto.setTotalSpent(totalSpent !=null ? Math.abs(totalSpent.intValue()) : 0);
		goalBudgetSummaryDto.setTotalBudgeted(totalBudgeted == null ? 0 : totalBudgeted.intValue());
		goalBudgetSummaryDto.setTotalIncomeBudgted(totalIncomeBudgeted == null ? 0 : totalIncomeBudgeted.intValue());
		goalBudgetSummaryDto.setTotalIncomeCredited(totalIncomeCredited == null ? 0 : totalIncomeCredited.intValue());
		
		return goalBudgetSummaryDto;
	}
	
	public Map<String,Float> getTotalBudgetedAmount(User user, Integer month, Integer year){
		
		Float income = 0f;
		Float totalBudgeted = 0f;
		Map<String,Float> amounts = new HashMap<>();
		
		String lastDateOfMonth		 = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth(year,month,0);
		String firstDateOfNextMonth  = DateUtility.getLastDateOfMonthAndFirstDateOfNextMonth(year,month,1);
		
		List<Budget> budgets = budgetRepository.getBudgets(user.getId(),lastDateOfMonth,firstDateOfNextMonth);
		
		for(Budget budget : budgets){
			
			if(OneMoneyConstants.Income_Category.equals(budget.getCategory().getName())){
				income = budget.getBudgetedAmount();
				continue;
			}
			
			if(budget.getCategory().getParentId() == null && budget.getBudgetedAmount() != null){
				totalBudgeted = totalBudgeted + budget.getBudgetedAmount();
			}else{
				
				List<Budget> budgetList = budgetRepository.getChildBudgetsbyParentCategoryId(user.getId(), budget.getCategory().getId());
				
				for(Budget b : budgetList){
					totalBudgeted = totalBudgeted + b.getBudgetedAmount();
				}
			}
		}
		
		amounts.put("totalBudgeted", totalBudgeted);
		amounts.put("totalIncomeBudgeted", income);
		return amounts;
	}

	@Override
	public Map<String, Float> getGoalsInfoByUser(User user) {
		Map<String, Float> goalInfo = new LinkedHashMap<>();
		List<Object[]> totalGoals = goalsRepository.getTotalGoalsOfUser(user.getId());
		for(Object[] objArr : totalGoals){
			goalInfo.put("totalGoals", objArr[0] == null ? 0: Float.parseFloat(objArr[0].toString()));
			goalInfo.put("savedAmount", objArr[1] == null ? 0: Float.parseFloat(objArr[1].toString()));
		}
		return goalInfo;
	}

	@Override
	public Map<String, Long> getGoalsData(User user, List<GoalDto> goalDtos, String purpose) throws Exception {
		
		Map<String, Long> goalInfo = new LinkedHashMap<>();
		
		Long savedAmount = 0l;
		Long toatal = 0l;
		Long targetAmount = 0l;
		
		for(GoalDto goalDto : goalDtos) {
			
			if(goalDto.getPurpose().equalsIgnoreCase(purpose)) {
				
				Goal goal = goalsRepository.getGoalById(goalDto.getGoalId());
				
				if(goal != null) {
					
					toatal = toatal + 1;
					targetAmount = (long) (targetAmount + goal.getTargetAmount());
				
					List<GoalAssetMapper> goalAssetMappers = goalAssetMapperRepository.getGoalAssetMappersByGoalIdAndUserId(goal.getId(), user.getId());
					Long propertyCV = GoalAssetDtoMapper.calculatePropertyCV(goalAssetMappers, goal.getCreatedDate());
					goalDto.setPropertyCV(propertyCV);
					savedAmount = savedAmount + GoalDtoMapper.calculateSavedAmount(goalDto.getAccounts() != null ? goalDto.getAccounts() : new ArrayList<>() ,goalDto);
				
				}
			}
		}
		
		goalInfo.put("totalGoals", toatal);
		goalInfo.put("savedAmount", savedAmount);
		goalInfo.put("targetAmount", targetAmount);
		
		return goalInfo;
	}
	
	
	
	
	@Override
	public List<GoalDto> getUserGoalsAndAccounts(User user) {
		
		List<GoalDto> goalDtos = new ArrayList<>();
		List<Goal> goals = goalsRepository.getGoalsByUserId(user.getId());
		
		
		for(Goal goal : goals) {
			
			GoalDto dto = new GoalDto();
			dto.setGoalId(goal.getId());
		
			List<GoalAccountDto> goalAccountDtos = new ArrayList<>();
			List<Object[]> accounts = goalAccountMapperRepository.getAccountCodesByUserIdAndGoalId(user.getId(),goal.getId());
			
			for(Object[] account : accounts) {
				GoalAccountDto goalAccountDto = new GoalAccountDto();
				goalAccountDto.setAccountId(account[0].toString());
				goalAccountDtos.add(goalAccountDto);
			}
			
			dto.setAccounts(goalAccountDtos);
			dto.setPurpose(goal.getPurpose().toString());
			goalDtos.add(dto);
		}
		return goalDtos;
	}

	@Override
	public List<GoalAssetMapper> getGoalAssetMappersByGoalIdAndUserId(Long userId, Long goalId) {
		return goalAssetMapperRepository.getGoalAssetMappersByGoalIdAndUserId(goalId,userId);
	}

	@Override
	public List<GoalAccountMapper> getGoalAccountMappersByGoalIdAndUserId(Long userId, Long goalId) {
		return goalAccountMapperRepository.getGoalAccountMappersByGoalIdAndUserId(goalId,userId);
	}

	@Override
	public GoalAccountMapper getGoalAccountMappersByGoalIdAndAccountCodeAndUserId(Long userId, Long goalId,String accountCode) {
		return goalAccountMapperRepository.getGoalAccountMapperByGoalIdAccountCodeAndUserId(goalId, accountCode, userId);
	}

	@Override
	public Goal getGoalByIdAndPurpose(Long goalId,Purpose purpose) {
		return goalsRepository.getGoalByIdAndPurpose(goalId,purpose);
	}

	@Override
	public Goal getGoal(Long goalId) {
		return goalsRepository.getGoalById(goalId);
	}

	@Override
	public Long getSuggetedMonthlyContribution(GoalDto dto, User user) throws Exception {
		
		List<GoalAssetMapper> goalAssetMappers = new ArrayList<>();
		dto.getAssets().forEach(asset -> {
			GoalAssetMapper goalAssetMapper = new GoalAssetMapper();
			Asset a = new Asset();
			a.setOriginalValue(asset.getCurrentValue() == null ? 0 : asset.getCurrentValue());
			a.setGrowthRate(asset.getGrowthRate() == null ? 0 : asset.getGrowthRate());
			goalAssetMapper.setAsset(a);
			goalAssetMappers.add(goalAssetMapper);
		});
		
		List<GoalAccountMapper> goalAccountMappers = new ArrayList<>();
		dto.getAccounts().forEach(account -> {
			GoalAccountMapper goalAccountMapper = new GoalAccountMapper();
			goalAccountMapper.setRateOfInterest(account.getRateOfInterest() == null ? 0 : account.getRateOfInterest());
			Account accountBal = new Account();
			accountBal.setMinBalance(account.getBalance()!= null ? account.getBalance() : 0 );
			goalAccountMapper.setAccount(accountBal);
			goalAccountMappers.add(goalAccountMapper);
		});
		
		GoalDto goalDto = new GoalDto();
		Date targetedDate =	DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.parse(dto.getTargetDate());
		goalDto.setTimeToTarget(GoalDtoMapper.calculateTimeTotarget(targetedDate));
		goalDto.setPropertyTimeToTarget(GoalDtoMapper.calculatePropertyTimeToTarget(targetedDate,Calendar.getInstance().getTime()));
		goalDto.setPropertyFV(GoalAssetDtoMapper.calculatePropertyFV(goalAssetMappers,goalDto));
		goalDto.setAccountBalanceFV(GoalDtoMapper.calculateAccountBalanceFV(goalAccountMappers, goalDto));
		goalDto.setSavingsTarget((long)(dto.getTargetAmount() - goalDto.getPropertyFV() - goalDto.getAccountBalanceFV()));
		goalDto.setSuggestedMonthlyContrib(goalAccountMappers.isEmpty() ? goalAssetMappers.isEmpty() 
				? 0 : GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, goalDto) 
				: GoalDtoMapper.calculateMonthlyContributionByAcountMappers(goalAccountMappers, goalDto));
		return goalDto.getSuggestedMonthlyContrib() != null ? goalDto.getSuggestedMonthlyContrib() : 0l;
	}

	@Override
	public List<Goal> getGoalsByUserId(Long userId) {
		return goalsRepository.getGoalsByUserId(userId);
	}

}
