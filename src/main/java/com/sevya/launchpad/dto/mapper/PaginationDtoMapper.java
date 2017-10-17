package com.sevya.launchpad.dto.mapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.sevya.launchpad.dto.PaginationDto;

public class PaginationDtoMapper<T> {
	
	@SuppressWarnings("rawtypes")
	public static PaginationDto toPaginationDto(Page<?> page,PaginationDto<?> paginationDto){
		
			Map<String,String> metadata =new HashMap<String,String>();
		    metadata.put("numberOfPageElements", String.valueOf(page.getNumberOfElements()));
		    metadata.put("perPage",String.valueOf(page.getSize()));
		    metadata.put("totalPages",String.valueOf( page.getTotalPages()));
		    metadata.put("totalElements",String.valueOf(page.getTotalElements()));
		    
		    paginationDto.setMetadata(metadata);
		    return paginationDto;
	}
}
