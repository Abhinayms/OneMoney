package com.sevya.onemoney.dto.mapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sevya.onemoney.dto.TxRequestDto;

public class PaginationDtoMapper {

	private PaginationDtoMapper(){}
	
	public static Pageable toPagable(TxRequestDto txRequestDto) {
		
		if(txRequestDto.getPageNo() == null || txRequestDto.getPageSize() == null){
			return null;
		}
		return new PageRequest(txRequestDto.getPageNo(),txRequestDto.getPageSize());
	
	}
	
}
