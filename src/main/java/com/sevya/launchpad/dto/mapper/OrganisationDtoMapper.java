package com.sevya.launchpad.dto.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevya.launchpad.dto.OrganisationDto;
import com.sevya.launchpad.model.Organisation;

public class OrganisationDtoMapper {

	
	public static Organisation toOrganisation(OrganisationDto organisationDto) {
		if (organisationDto != null) {
			Organisation organisation = new Organisation();
			organisation.setName(organisationDto.getName());
			organisation.setCode(organisationDto.getCode());
			organisation.setConfigLabelName(organisationDto.getConfigLabelName());
			organisation.setCreatedDate(new Date());
			organisation.setModifiedDate(new Date());
			return organisation;
		}
		return null;
	}

	public static OrganisationDto toOrganisationDto(Organisation organisation) {
		if (organisation != null) {
			OrganisationDto organisationDto = new OrganisationDto();
			organisationDto.setOrgId(organisation.getId());
			organisationDto.setName(organisation.getName());
			organisationDto.setCode(organisation.getCode());
			organisationDto.setConfigLabelName(organisation.getConfigLabelName());
			return organisationDto;
		}
		return null;
	}
	
	
	public static List<OrganisationDto> toOrganisationDtos(List<Organisation> organisations) {
		List<OrganisationDto> dtos =new ArrayList<>();
		for(Organisation organisation : organisations){
			OrganisationDto organisationDto = new OrganisationDto();
			organisationDto.setOrgId(organisation.getId());
			organisationDto.setName(organisation.getName());
			organisationDto.setCode(organisation.getCode());
			organisationDto.setConfigLabelName(organisation.getConfigLabelName());
			dtos.add(organisationDto);
		}
		return dtos;
	}
}
