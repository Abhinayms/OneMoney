package com.sevya.launchpad.service;

import java.util.List;

import com.sevya.launchpad.dto.DepartmentDto;
import com.sevya.launchpad.dto.DivisionDto;
import com.sevya.launchpad.dto.EntityDto;
import com.sevya.launchpad.dto.OrganisationDto;
import com.sevya.launchpad.model.Department;
import com.sevya.launchpad.model.Division;
import com.sevya.launchpad.model.Entity;
import com.sevya.launchpad.model.Organisation;

public interface OrganisationService {
	
	public Organisation getOrg(Long orgId);
	
	/**
	 * Returns the Organisation
	 * @param uuid UUID code
	 * */
	public Organisation getOrg(String uuid);
	
	public List<Organisation> getAllOrg();
	
	public Organisation createOrg(OrganisationDto organisationDto);
	
	public Organisation deleteOrg(Long orgId);
	
	
	
	
	
	public Entity getEntity(Long entityId);
	
	/**
	 * Returns the Entity
	 * @param uuid UUID code of Entity
	 * */
	public Entity getEntity(String uuid);
	
	public List<Entity> getAllEntities(Long OrgId);
	
	public Entity createEntity(EntityDto entityDto) throws Exception;
	
	public Entity deleteEntity(Long entityId);
	
	
	
	
	
	public Division getDivision(Long divisionId);
	
	/**
	 * Returns division by division uuid code
	 * @param uuid UUID code of division
	 * */
	public Division getDivision(String uuid);
	
	public List<Division> getAllDivisions(Long entityId);
	
	public Division createDivision(DivisionDto divisionDto) throws Exception;
	
	public Division deleteDivision(Long divisionId);
	
	
	
	
	
	
	public Department getDepartment(Long departmentId);
	
	/**
	 * Returns the Department by department
	 * @param uuid UUID code of department
	 * */
	public Department getDepartment(String uuid);
	
	public List<Department> getAllDepartments(Long divisionId);
	
	public Department createDepartment(DepartmentDto departmentDto) throws Exception;
	
	public Department deleteDepartment(Long departmentId);
	
}
