package com.sevya.launchpad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sevya.launchpad.dto.DepartmentDto;
import com.sevya.launchpad.dto.DivisionDto;
import com.sevya.launchpad.dto.EntityDto;
import com.sevya.launchpad.dto.OrganisationDto;
import com.sevya.launchpad.dto.mapper.DepartmentDtoMapper;
import com.sevya.launchpad.dto.mapper.DivisionDtoMapper;
import com.sevya.launchpad.dto.mapper.EntityDtoMapper;
import com.sevya.launchpad.dto.mapper.OrganisationDtoMapper;
import com.sevya.launchpad.model.Department;
import com.sevya.launchpad.model.Division;
import com.sevya.launchpad.model.Entity;
import com.sevya.launchpad.model.Organisation;
import com.sevya.launchpad.repository.DepartmentRepository;
import com.sevya.launchpad.repository.DivisionRepository;
import com.sevya.launchpad.repository.EntityRepository;
import com.sevya.launchpad.repository.OrganisationRepository;
import com.sevya.launchpad.service.OrganisationService;

@Service
public class OrganisationServiceImpl implements OrganisationService {

	@Autowired
	private OrganisationRepository organisationRepository;
	
	@Autowired
	private EntityRepository entityRepository;
	
	@Autowired
	private DivisionRepository divisionRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Override
	public Organisation getOrg(Long orgId) {
		return organisationRepository.getOrgById(orgId);
	}

	@Override
	public List<Organisation> getAllOrg(){
		return organisationRepository.getAllOrganisations();
	}

	@Override
	public Organisation createOrg(OrganisationDto organisationDto) {
		Organisation organisation = OrganisationDtoMapper.toOrganisation(organisationDto);
		return organisationRepository.save(organisation);
	}

	@Override
	public Organisation deleteOrg(Long orgId) {
		Organisation org = organisationRepository.getOrgById(orgId);
		if(org != null){
			org.setIsActive(false);
			return organisationRepository.save(org);
		}else{
			return new Organisation();
		}
	}

	@Override
	public Entity getEntity(Long entityId) {
		return entityRepository.getEntityById(entityId);
	}

	@Override
	public List<Entity> getAllEntities(Long orgId) {
		return entityRepository.getAllEntitiesByOrgId(orgId);
	}

	@Override
	public Entity createEntity(EntityDto entityDto) throws Exception {
		Entity entity = EntityDtoMapper.toEntity(entityDto);
		Organisation org = organisationRepository.getOrgById(entityDto.getOrgId());
		if(org == null){
			throw new NullPointerException("Organisation not found");
		}
		entity.setOrganisation(org);
		return entityRepository.save(entity);
	}

	@Override
	public Entity deleteEntity(Long entityId) {
		Entity entity = entityRepository.getEntityById(entityId);
		if(entity != null){
			entity.setIsActive(false);
			return entityRepository.save(entity);
		}else{
			return new Entity();
		}
	}

	@Override
	public Division getDivision(Long divisionId) {
		return divisionRepository.getDivisionById(divisionId);
	}

	@Override
	public List<Division> getAllDivisions(Long entityId) {
		return divisionRepository.getAllDivisionsByEntityId(entityId);
	}

	@Override
	public Division createDivision(DivisionDto divisionDto) throws Exception {
		Division division = DivisionDtoMapper.toDivision(divisionDto);
		Entity entity = entityRepository.getEntityById(divisionDto.getEntityId());
		if(entity == null){
			throw new NullPointerException("Entity not found");
		}
		division.setEntity(entity);
		return divisionRepository.save(division);
	}

	@Override
	public Division deleteDivision(Long divisionId) {
		Division division = divisionRepository.getDivisionById(divisionId);
		if(division != null){
			division.setIsActive(false);
			return divisionRepository.save(division);
		}else{
			return new Division();
		}
	}

	@Override
	public Department getDepartment(Long departmentId) {
		return departmentRepository.getDepartmentById(departmentId);
	}

	@Override
	public List<Department> getAllDepartments(Long divisionId) {
		return departmentRepository.getAllDepartmentsByDivisionId(divisionId);
	}

	@Override
	public Department createDepartment(DepartmentDto departmentDto) throws Exception {
		Department department = DepartmentDtoMapper.toDepartment(departmentDto);
		Division division = divisionRepository.getDivisionById(departmentDto.getDivisionId());
		if(division == null){
			throw new NullPointerException("Division not found");
		}
		department.setDivision(division);
		return departmentRepository.save(department);
	}

	@Override
	public Department deleteDepartment(Long departmentId) {
		Department department = departmentRepository.getDepartmentById(departmentId);
		if(department != null){
			department.setIsActive(false);
			return departmentRepository.save(department);
		}else{
			return new Department();
		}
	}

	@Override
	public Organisation getOrg(String uuid) {
		return organisationRepository.getOrgByUUID(uuid);
	}

	@Override
	public Entity getEntity(String uuid) {
		return entityRepository.getEntityByUUID(uuid);
	}

	@Override
	public Division getDivision(String uuid) {
		return divisionRepository.getDivisionByUUID(uuid);
	}

	@Override
	public Department getDepartment(String uuid) {
		return departmentRepository.getDepartmentByUUID(uuid);
	}

}
