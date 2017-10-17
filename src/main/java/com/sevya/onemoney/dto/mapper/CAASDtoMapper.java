package com.sevya.onemoney.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.sevya.onemoney.dto.CAASDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.model.Transaction;

public class CAASDtoMapper {

	private CAASDtoMapper() {}

	public static List<CAASDto> toCAASDtos(List<Transaction> transactions) {

		List<CAASDto> caasDtos = new ArrayList<>();
		for (Transaction transaction : transactions) {
			CAASDto caasDto = new CAASDto();
			caasDto.setTransaction_id(transaction.getFingerprint());
			caasDto.setDescription(transaction.getDescription());
			Date date = transaction.getTransactionDate();
			caasDto.setTimestamp(Math.abs((int)date.getTime()));
			
			HashMap<String,String> metaData = new LinkedHashMap<>();
			metaData.put("account_id",transaction.getAccountCode());
			caasDto.setMetadata(metaData);
			
			caasDtos.add(caasDto);
		}
		return caasDtos;
		
	}
	
	
	public static List<CAASDto> toCAASDtos(Transaction transaction,Category category) {

		List<CAASDto> caasDtos = new ArrayList<>();
		
			CAASDto caasDto = new CAASDto();
			caasDto.setTransaction_id(transaction.getFingerprint());
			caasDto.setCategory_id(category.getUuid());
			caasDtos.add(caasDto);
	
		return caasDtos;
		
	}
	
	public static CAASDto toCAASDto(String accountCode) {

		CAASDto caasDto = new CAASDto();
		HashMap<String,String> data = new LinkedHashMap<>();
		data.put("account_id",accountCode);
		caasDto.setData(data);
		return caasDto;
		
	}
	
	public static CAASDto toCAASDto(CategoryDto categoryDto) {

		CAASDto caasDto = new CAASDto();
		caasDto.setName(categoryDto.getCategoryName());
		caasDto.setParent_category_id(categoryDto.getParentCategoryCode());
		return caasDto;
		
	}
	
	

}
