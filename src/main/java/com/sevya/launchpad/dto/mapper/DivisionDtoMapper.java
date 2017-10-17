package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevya.launchpad.dto.DivisionDto;
import com.sevya.launchpad.model.Division;

public class DivisionDtoMapper {
	
	public static Division toDivision(DivisionDto divisionDto) {
		if (divisionDto != null) {
			Division division = new Division();
			division.setName(divisionDto.getName());
			division.setCode(divisionDto.getCode());
			division.setConfigLabelName(divisionDto.getConfigLabelName());
			division.setCreatedDate(new Date());
			division.setModifiedDate(new Date());
			return division;
		}
		return null;
	}

	public static DivisionDto toDivisionDto(Division division) {
		if (division != null) {
			DivisionDto divisionDto = new DivisionDto();
			divisionDto.setDivisionId(division.getId());
			divisionDto.setName(division.getName());
			divisionDto.setCode(division.getCode());
			divisionDto.setConfigLabelName(division.getConfigLabelName());
			return divisionDto;
		}
		return null;
	}
	
	
	public static List<DivisionDto> toDivisionDtos(List<Division> divisions) {
		List<DivisionDto> dtos =new ArrayList<>();
		for(Division division : divisions){
			DivisionDto divisionDto = new DivisionDto();
			divisionDto.setDivisionId(division.getId());
			divisionDto.setName(division.getName());
			divisionDto.setCode(division.getCode());
			divisionDto.setConfigLabelName(division.getConfigLabelName());
			dtos.add(divisionDto);
		}
		return dtos;
	}
}
