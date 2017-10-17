package com.sevya.launchpad.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.sevya.launchpad.dto.PaginationDto;

public class PaginationUtility<T> {

	public static Pageable createPageRequest(PaginationDto<?> paginationDto) {
		
		if(paginationDto.getLimit() == 0)
			return null;
		
		Sort.Direction directionType = paginationDto.getSort() == null || 
										!paginationDto.getSort().equalsIgnoreCase("desc")?
												Sort.Direction.ASC: Sort.Direction.DESC;
		boolean isSortable =paginationDto.getFields()!=null?paginationDto.getFields().size()>0?true:false:false;
		if(isSortable){
			Pageable pageable = new PageRequest(paginationDto.getOffset(),paginationDto.getLimit(),
					new Sort(directionType,paginationDto.getFields()));
			return pageable;
		}else{
			Pageable pageable = new PageRequest(paginationDto.getOffset(),paginationDto.getLimit());
			return pageable;
		}
		

		
	}
	
}
