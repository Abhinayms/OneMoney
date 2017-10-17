package com.sevya.launchpad.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sevya.launchpad.dto.DepartmentDto;
import com.sevya.launchpad.dto.DivisionDto;
import com.sevya.launchpad.dto.EntityDto;
import com.sevya.launchpad.dto.OrganisationDto;
import com.sevya.launchpad.dto.mapper.DepartmentDtoMapper;
import com.sevya.launchpad.dto.mapper.DivisionDtoMapper;
import com.sevya.launchpad.dto.mapper.EntityDtoMapper;
import com.sevya.launchpad.dto.mapper.OrganisationDtoMapper;
import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.Department;
import com.sevya.launchpad.model.Division;
import com.sevya.launchpad.model.Entity;
import com.sevya.launchpad.model.Organisation;
import com.sevya.launchpad.service.OrganisationService;

@RestController
@RequestMapping("/api/v1")
public class OrganisationController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OrganisationService organisationService;
	
	@RequestMapping(value = "/org/{orgId}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrg(@PathVariable("orgId") Long orgId) {
		logger.info("OrganisationController getOrg() method initiated...");
		try{
			Organisation organisation = organisationService.getOrg(orgId);
			if(organisation==null){
				throw new ResourceNotFoundException("Organisation not found");
			}
			return new ResponseEntity<>(OrganisationDtoMapper.toOrganisationDto(organisation), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	@RequestMapping(value = "/org/code/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<OrganisationDto> getOrg(@PathVariable("uuid") String uuid) {
		logger.info("getOrg() by uuid code controller initiated...");
		try{
			Organisation organisation = organisationService.getOrg(uuid);
			if(organisation==null){
				throw new ResourceNotFoundException("Organisation not found");
			}
			return new ResponseEntity<OrganisationDto>(OrganisationDtoMapper.toOrganisationDto(organisation), HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<OrganisationDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/org", method = RequestMethod.GET)
	public ResponseEntity<?> getAllOrg() {
		logger.info("OrganisationController getAllOrg() method initiated...");
		try{
			List<Organisation> organisations = organisationService.getAllOrg();
			return new ResponseEntity<>(OrganisationDtoMapper.toOrganisationDtos(organisations), HttpStatus.OK);	
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/org", method=RequestMethod.POST)
	public ResponseEntity<?> createOrg(@RequestBody OrganisationDto organisationDto) {
		logger.info("OrganisationController createOrg() method initiated...");
		try{
			Organisation organisation = organisationService.createOrg(organisationDto);
			return new ResponseEntity<>(OrganisationDtoMapper.toOrganisationDto(organisation),HttpStatus.CREATED);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(organisationDto,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/org/{orgId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOrg(@PathVariable("orgId") Long orgId) {
		logger.info("OrganisationController getOrg() method initiated...");
		try{
			Organisation organisation = organisationService.deleteOrg(orgId);
			if(organisation==null){
				return new ResponseEntity<>("Organisation not found",HttpStatus.OK);
			}else{
				return new ResponseEntity<>(OrganisationDtoMapper.toOrganisationDto(organisation),HttpStatus.OK);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/entity/{entityId}", method = RequestMethod.GET)
	public ResponseEntity<?> getEntity(@PathVariable("entityId") Long entityId) {
		logger.info("OrganisationController getEntity() method initiated...");
		try{
			Entity entity = organisationService.getEntity(entityId);
			if(entity==null){
				return new ResponseEntity<>("Entity not found", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(EntityDtoMapper.toEntityDto(entity), HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Entity not found",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	@RequestMapping(value = "/entity/code/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<EntityDto> getEntity(@PathVariable("uuid") String uuid) {
		logger.info("getEntity() by uuid code controller  initiated...");
		try{
			Entity entity = organisationService.getEntity(uuid);
			if(entity==null){
				throw new ResourceNotFoundException("Resource Entity not found");
			}else{
				return new ResponseEntity<EntityDto>(EntityDtoMapper.toEntityDto(entity), HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<EntityDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/entity/org/{orgId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEntities(@PathVariable("orgId") Long orgId) {
		logger.info("OrganisationController getAllEntities() method initiated...");
		try{
			List<Entity> entities = organisationService.getAllEntities(orgId);
			if(entities.size() > 0){
				return new ResponseEntity<>(EntityDtoMapper.toEntityDtos(entities), HttpStatus.OK);
			}else{
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/entity/{orgId}", method=RequestMethod.POST)
	public ResponseEntity<?> createEntity(@PathVariable("orgId") Long orgId,@RequestBody EntityDto entityDto) {
		logger.info("OrganisationController createEntity() method initiated...");
		try{
			entityDto.setOrgId(orgId);
			Entity entity = organisationService.createEntity(entityDto);
			return new ResponseEntity<>(EntityDtoMapper.toEntityDto(entity),HttpStatus.CREATED);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/entity/{entityId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEntity(@PathVariable("entityId") Long entityId) {
		logger.info("OrganisationController getOrg() method initiated...");
		try{
			Entity entity = organisationService.deleteEntity(entityId);
			if(entity == null){
				return new ResponseEntity<>("Entity not found",HttpStatus.OK);
			}else{
				return new ResponseEntity<>(EntityDtoMapper.toEntityDto(entity),HttpStatus.OK);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/division/{divisionId}", method = RequestMethod.GET)
	public ResponseEntity<?> getDivision(@PathVariable("divisionId") Long divisionId) {
		logger.info("OrganisationController getDivision() method initiated...");
		try{
			Division division = organisationService.getDivision(divisionId);
			if(division==null){
				return new ResponseEntity<>("Division not found", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(DivisionDtoMapper.toDivisionDto(division), HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/division/code/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> getDivision(@PathVariable("uuid") String uuid) {
		logger.info("OrganisationController getDivision() method initiated...");
		try{
			Division division = organisationService.getDivision(uuid);
			if(division==null){
				throw new ResourceNotFoundException("Division not found.");
			}else{
				return new ResponseEntity<DivisionDto>(DivisionDtoMapper.toDivisionDto(division), HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DivisionDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/division/entity/{entityId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDivisions(@PathVariable("entityId") Long entityId) {
		logger.info("OrganisationController getAllDivisions() method initiated...");
		try{
			List<Division> divisions = organisationService.getAllDivisions(entityId);
			if(divisions.size() > 0){
				return new ResponseEntity<>(DivisionDtoMapper.toDivisionDtos(divisions), HttpStatus.OK);
			}else{
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/division/{entityId}", method=RequestMethod.POST)
	public ResponseEntity<?> createDivision(@PathVariable("entityId") Long entityId,@RequestBody DivisionDto divisionDto) {
		logger.info("OrganisationController createDivision() method initiated...");
		try{
			divisionDto.setEntityId(entityId);
			Division division =organisationService.createDivision(divisionDto);
			return new ResponseEntity<>(DivisionDtoMapper.toDivisionDto(division),HttpStatus.CREATED);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/division/{divisionId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDivision(@PathVariable("divisionId") Long divisionId) {
		logger.info("OrganisationController deleteDivision() method initiated...");
		try{
			Division division = organisationService.deleteDivision(divisionId);
			if(division == null){
				return new ResponseEntity<>("Division not Found",HttpStatus.OK);
			}else{
				return new ResponseEntity<>(DivisionDtoMapper.toDivisionDto(division),HttpStatus.OK);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/department/{departmentId}", method = RequestMethod.GET)
	public ResponseEntity<?> getDepartment(@PathVariable("departmentId") Long departmentId) {
		logger.info("OrganisationController getDepartment() method initiated...");
		try{
			Department department = organisationService.getDepartment(departmentId);
			if(department==null){
				return new ResponseEntity<>("Department not found", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(DepartmentDtoMapper.toDepartmentDto(department), HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/department/code/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> getDepartment(@PathVariable("uuid") String uuid) {
		logger.info("getDepartment() by uuid code controller initiated...");
		try{
			Department department = organisationService.getDepartment(uuid);
			if(department==null){
				throw new ResourceNotFoundException("Department not found");
			}else{
				return new ResponseEntity<DepartmentDto>(DepartmentDtoMapper.toDepartmentDto(department), HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DepartmentDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	@RequestMapping(value = "/department/division/{divisionId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDepartments(@PathVariable("divisionId") Long divisionId) {
		logger.info("OrganisationController getAllDepartments() method initiated...");
		try{
			List<Department> departments = organisationService.getAllDepartments(divisionId);
			if(departments.size() > 0){
				return new ResponseEntity<>(DepartmentDtoMapper.toDepartmentDtos(departments), HttpStatus.OK);
			}else{
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/department/{divisionId}", method=RequestMethod.POST)
	public ResponseEntity<?> createDepartment(@PathVariable("divisionId") Long divisionId,@RequestBody DepartmentDto departmentDto) {
		logger.info("OrganisationController createDepartment() method initiated...");
		try{
			departmentDto.setDivisionId(divisionId);
			Department department =organisationService.createDepartment(departmentDto);
			return new ResponseEntity<>(DepartmentDtoMapper.toDepartmentDto(department),HttpStatus.CREATED);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/department/{departmentId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDepartment(@PathVariable("departmentId") Long departmentId) {
		logger.info("OrganisationController deleteDepartment() method initiated...");
		try{
			Department department = organisationService.deleteDepartment(departmentId);
			if(department == null){
				return new ResponseEntity<>("Department not found",HttpStatus.OK);
			}else{
				return new ResponseEntity<>(DepartmentDtoMapper.toDepartmentDto(department),HttpStatus.OK);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
