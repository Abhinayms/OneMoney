package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sevya.launchpad.dto.GroupDto;
import com.sevya.launchpad.model.Group;

public class GroupDtoMapper {

	public static GroupDto toGroupDto(Group group) {
		
		if (group != null) {
			
			GroupDto groupDto = new GroupDto();
			groupDto.setId(group.getId());
			groupDto.setCode(group.getCode());
			groupDto.setName(group.getName());
			
			return groupDto;
			
		}
		return null;
	}
	
	
	public static List<GroupDto> toGroupDtos(Iterable<Group> Groups) {
		
		List<GroupDto> dtos =new ArrayList<GroupDto>();
		
		for(Group group : Groups){

			GroupDto groupDto = new GroupDto();
			groupDto.setId(group.getId());
			groupDto.setCode(group.getCode());
			groupDto.setName(group.getName());
	
			dtos.add(groupDto);
		}
		
		return dtos;
		
	}
	
}
