package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sevya.launchpad.dto.RoleDto;
import com.sevya.launchpad.model.Role;

public class RoleDtoMapper {

	public static RoleDto toRoleDto(Role role) {
		
		if (role != null) {
			
			RoleDto roleDto = new RoleDto();
			roleDto.setId(role.getId());
			roleDto.setCode(role.getCode());
			roleDto.setName(role.getName());
			
			return roleDto;
			
		}
		return null;
	}
	
	
	public static List<RoleDto> toRoleDtos(Iterable<Role> roles) {
		
		List<RoleDto> dtos =new ArrayList<RoleDto>();
		
		for(Role role : roles){

			RoleDto roleDto = new RoleDto();
			roleDto.setId(role.getId());
			roleDto.setCode(role.getCode());
			roleDto.setName(role.getName());
	
			dtos.add(roleDto);
		}
		
		return dtos;
		
	}
	
}
