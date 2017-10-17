package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevya.launchpad.dto.PermissionDto;
import com.sevya.launchpad.model.Permission;
import com.sevya.launchpad.util.DateUtility;
import com.sevya.launchpad.util.LaunchpadUtility;

public class PermissionDtoMapper {

	public static Permission toPermission(PermissionDto permissionDto) {
		if (permissionDto != null) {
			Permission permission = new Permission();
			permission.setName(permissionDto.getName());
			permission.setCode(permissionDto.getCode());
			permission.setUrlPattern(permissionDto.getUrlPattern());			
			//permission.setCreatedDate(new Date());
			//permission.setModifiedDate(new Date());
			permission.setUuid(LaunchpadUtility.generateUUIDCode());
			return permission;
		}
		return null;
	}

	public static PermissionDto toPermissionDto(Permission permission) {
		if (permission != null) {
			PermissionDto permissionDto = new PermissionDto();

			permissionDto.setId(permission.getId());
			permissionDto.setName(permission.getName());
			permissionDto.setCode(permission.getCode());
			permissionDto.setUrlPattern(permission.getUrlPattern());
			permissionDto.setCreatedDate(DateUtility.convertFromSQLDateToJAVADateString(permission.getCreatedDate()));
			permissionDto.setModifiedDate(DateUtility.convertFromSQLDateToJAVADateString(permission.getModifiedDate()));
			permissionDto.setIsActive(permission.getIsActive());
			permissionDto.setUuid(permission.getUuid());
			return permissionDto;
		}
		return null;
	}
	
	
	public static List<PermissionDto> toPermissionDtos(Iterable<Permission> permissions) {
		
		List<PermissionDto> dtos =new ArrayList<>();
		for(Permission permission : permissions){

			PermissionDto permissionDto = new PermissionDto();
		
			permissionDto.setId(permission.getId());
			permissionDto.setName(permission.getName());
			permissionDto.setCode(permission.getCode());
			permissionDto.setUrlPattern(permission.getUrlPattern());
			permissionDto.setCreatedDate(DateUtility.convertFromSQLDateToJAVADateString(permission.getCreatedDate()));
			permissionDto.setModifiedDate(DateUtility.convertFromSQLDateToJAVADateString(permission.getModifiedDate()));
			permissionDto.setIsActive(permission.getIsActive());
			permissionDto.setUuid(permission.getUuid());
			dtos.add(permissionDto);
		}
		return dtos;
	}

}
