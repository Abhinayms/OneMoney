package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevya.launchpad.dto.EntityDto;
import com.sevya.launchpad.model.Entity;

public class EntityDtoMapper {

	public static Entity toEntity(EntityDto entityDto) {
		if (entityDto != null) {
			Entity organisation = new Entity();
			organisation.setName(entityDto.getName());
			organisation.setCode(entityDto.getCode());
			organisation.setConfigLabelName(entityDto.getConfigLabelName());
			organisation.setCreatedDate(new Date());
			organisation.setModifiedDate(new Date());
			return organisation;
		}
		return null;
	}

	public static EntityDto toEntityDto(Entity entity) {
		if (entity != null) {
			EntityDto entityDto = new EntityDto();
			entityDto.setEntityId(entity.getId());
			entityDto.setName(entity.getName());
			entityDto.setCode(entity.getCode());
			entityDto.setConfigLabelName(entity.getConfigLabelName());
			return entityDto;
		}
		return null;
	}
	
	
	public static List<EntityDto> toEntityDtos(List<Entity> entities) {
		List<EntityDto> dtos =new ArrayList<>();
		for(Entity entity : entities){
			EntityDto entityDto = new EntityDto();
			entityDto.setEntityId(entity.getId());
			entityDto.setName(entity.getName());
			entityDto.setCode(entity.getCode());
			entityDto.setConfigLabelName(entity.getConfigLabelName());
			dtos.add(entityDto);
		}
		return dtos;
	}
	
}
