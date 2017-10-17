package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevya.launchpad.dto.DepartmentDto;
import com.sevya.launchpad.model.Department;

public class DepartmentDtoMapper {

	public static Department toDepartment(DepartmentDto departmentDto) {
		if (departmentDto != null) {
			Department department = new Department();
			department.setName(departmentDto.getName());
			department.setCode(departmentDto.getCode());
			department.setConfigLabelName(departmentDto.getConfigLabelName());
			department.setCreatedDate(new Date());
			department.setModifiedDate(new Date());
			return department;
		}
		return null;
	}

	public static DepartmentDto toDepartmentDto(Department department) {
		if (department != null) {
			DepartmentDto departmentDto = new DepartmentDto();
			departmentDto.setDepartmentId(department.getId());
			departmentDto.setName(department.getName());
			departmentDto.setCode(department.getCode());
			departmentDto.setConfigLabelName(department.getConfigLabelName());
			return departmentDto;
		}
		return null;
	}
	
	
	public static List<DepartmentDto> toDepartmentDtos(List<Department> departments) {
		List<DepartmentDto> dtos =new ArrayList<>();
		for(Department department : departments){
			DepartmentDto departmentDto = new DepartmentDto();
			departmentDto.setDepartmentId(department.getId());
			departmentDto.setName(department.getName());
			departmentDto.setCode(department.getCode());
			departmentDto.setConfigLabelName(department.getConfigLabelName());
			dtos.add(departmentDto);
		}
		return dtos;
	}
}
