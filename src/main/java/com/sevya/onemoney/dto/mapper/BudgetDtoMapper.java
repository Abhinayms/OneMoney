package com.sevya.onemoney.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.onemoney.dto.BudgetDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.model.Budget;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.utility.Purpose;

public class BudgetDtoMapper {

	private BudgetDtoMapper() {}
	
	public static Budget toBudget(Budget budget,BudgetDto budgetDto,Category category) throws Exception {
		budget.setUuid(LaunchpadUtility.generateUUIDCode());
		budget.setCategory(category);
		budget.setMaxLimit(budgetDto.getLimit() != null ? budgetDto.getLimit() : false);
		budget.setBudgetedAmount(budgetDto.getBudgetedAmount());
		
		if("P".equalsIgnoreCase(budgetDto.getPurpose())){
			budget.setPurpose(Purpose.P);
		}else if("B".equalsIgnoreCase(budgetDto.getPurpose())){
			budget.setPurpose(Purpose.B);
		}else{
			throw new NullPointerException("Budget type is mandatory...!!!");
		}
		return budget;
	}
	
	public static BudgetDto toBudgetDto(BudgetDto budgetDto,Budget budget){
		
		budgetDto.setBudgetId(budget.getId());
		budgetDto.setSpentAmount(0l);
		budgetDto.setAvgSpentAmount(0l);
		budgetDto.setMessage("Welcome ...!!!");
		
		budgetDto.setBudgetedAmount(budget.getBudgetedAmount());
		budgetDto.setPurpose(String.valueOf(budget.getPurpose()));
		
		CategoryDto dto = new CategoryDto();
		dto.setCategoryCode(budget.getCategory().getUuid());
		dto.setCategoryName(budget.getCategory().getName());
		budgetDto.setCategory(dto);
		return budgetDto;
	}
	
	public static BudgetDto toBudgetDtoWhenBudgetNotCreated(BudgetDto budgetDto,Budget budget){
		
		budgetDto.setBudgetId(budget.getId());
		CategoryDto dto = new CategoryDto();
		dto.setCategoryCode(budget.getCategory().getUuid());
		dto.setCategoryName(budget.getCategory().getName());
		budgetDto.setCategory(dto);
		
		return budgetDto;
	}
	
	public static List<BudgetDto> toBudgetDtos(List<Budget> budgets){
		List<BudgetDto> dtos = new ArrayList<>();
		budgets.forEach( budget -> {
			
			BudgetDto budgetDto = new BudgetDto();
			budgetDto.setBudgetId(budget.getId());
			budgetDto.setBudgetedAmount(budgetDto.getBudgetedAmount());
			budgetDto.setPurpose(String.valueOf(budget.getPurpose()));
			
			CategoryDto dto = new CategoryDto();
			dto.setCategoryCode(budget.getCategory().getUuid());
			dto.setCategoryName(budget.getCategory().getName());
			budgetDto.setCategory(dto);
			
			dtos.add(budgetDto);
		});
		return dtos;
	}
}