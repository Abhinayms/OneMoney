package com.sevya.onemoney.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.sevya.onemoney.dto.CategoricalExpensesDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.model.Transaction;

public class CategoricalExpensesDtoMapper {

	private CategoricalExpensesDtoMapper() {}
	
	
	
	public static List<CategoricalExpensesDto> toCategoricalExpensesDto(Page<CategoryTransactionMapper> categoryTransactionMappers) {
		
		List<CategoricalExpensesDto> categoricalExpensesDtos = new ArrayList<>();
		for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMappers.getContent()) {
		
			Transaction transaction = categoryTransactionMapper.getTransaction();
			Category category = categoryTransactionMapper.getCategory();
			
			if(category!=null && transaction!=null && category.getParentId()==null) {
			
				CategoricalExpensesDto categoricalExpensesDto = new CategoricalExpensesDto();
				categoricalExpensesDto.setAmount(Math.abs(transaction.getAmount()));
				CategoryDto categoryDto = new CategoryDto();
				categoryDto.setCategoryCode(category.getUuid());
				categoryDto.setCategoryName(category.getName());
				categoricalExpensesDto.setCategory(categoryDto);
				categoricalExpensesDtos.add(categoricalExpensesDto);
				
			}
			
		}
		return categoricalExpensesDtos;
		
	}
	
	
	public static List<CategoricalExpensesDto> toCategoricalExpensesDto(List<CategoryTransactionMapper> categoryTransactionMappers) {
		
		List<CategoricalExpensesDto> categoricalExpensesDtos = new ArrayList<>();
		for(CategoryTransactionMapper categoryTransactionMapper : categoryTransactionMappers) {
		
			Transaction transaction = categoryTransactionMapper.getTransaction();
			Category category = categoryTransactionMapper.getCategory();
			
			if(category!=null && transaction != null && category.getParentId()==null) {
				
				CategoricalExpensesDto categoricalExpensesDto = new CategoricalExpensesDto();
				categoricalExpensesDto.setAmount(Math.abs(transaction.getAmount()));
				CategoryDto categoryDto = new CategoryDto();
				categoryDto.setCategoryCode(category.getUuid());
				categoryDto.setCategoryName(category.getName());
				categoricalExpensesDto.setCategory(categoryDto);
				categoricalExpensesDtos.add(categoricalExpensesDto);
				
			}
		}
		return categoricalExpensesDtos;
	}
	
	
	
	
	
	
}
