package com.sevya.launchpad.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sevya.launchpad.dto.PaginationDto;
import com.sevya.launchpad.dto.PermissionDto;
import com.sevya.launchpad.dto.mapper.PaginationDtoMapper;
import com.sevya.launchpad.dto.mapper.PermissionDtoMapper;
import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.Permission;
import com.sevya.launchpad.service.PermissionService;

@RestController
@RequestMapping("/api/v1/permission")

public class PermissionController extends BaseController{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired	
	private PermissionService permissionService;
	
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<PaginationDto> getAllPermissions(PaginationDto paginationDto) {
		logger.info("getAllPermissions() controller initiated");
		try{
			Page<Permission> permissions =permissionService.getAllPermissions(paginationDto);
			paginationDto = PaginationDtoMapper.toPaginationDto(permissions, paginationDto);
			paginationDto.setContent(PermissionDtoMapper.toPermissionDtos(permissions));
			
			return new ResponseEntity<PaginationDto>(paginationDto, HttpStatus.OK);
			/*return new ResponseEntity<>(new PaginationDto<>(permissions,PermissionDtoMapper.toPermissionDtos(permissions),paginationDto), HttpStatus.OK);*/	
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value="/{uuid}", method=RequestMethod.GET)
	public ResponseEntity<PermissionDto> getPermission(@PathVariable String uuid){
		logger.info("getAllPermission()  by uuidCode controller initiated");
		try{
			Permission permission =permissionService.getPermissionByUUIDCode(uuid);
			if(permission==null){
				throw new ResourceNotFoundException("Permission not found");
			}
			return new ResponseEntity<PermissionDto>(PermissionDtoMapper.toPermissionDto(permission), HttpStatus.OK);
			/*return new ResponseEntity<>((PermissionDto)ObjectMapperUtiliity.convertToDto(permission, PermissionDto.class), HttpStatus.OK);*/	
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
		
	}	
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<PermissionDto> getPermission(@PathVariable Long id){
		logger.info("getAllPermission()  by id controller initiated");
		try{
			Permission permission =permissionService.getPermission(id);
			if(permission==null){
				throw new ResourceNotFoundException("Permission not found");
			}
			return new ResponseEntity<PermissionDto>(PermissionDtoMapper.toPermissionDto(permission), HttpStatus.OK);
			/*return new ResponseEntity<>((PermissionDto)ObjectMapperUtiliity.convertToDto(permission, PermissionDto.class), HttpStatus.OK);*/	
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
		
	}	
	

	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDto) {
		logger.info("createPermission() controller initiated");
		try{
			
			Permission permission = PermissionDtoMapper.toPermission(permissionDto);
			if(permission!=null){
				permission =permissionService.createPermission(permission);
				return new ResponseEntity<PermissionDto>(PermissionDtoMapper.toPermissionDto(permission),HttpStatus.CREATED);
			}else{
				return new ResponseEntity<PermissionDto>(HttpStatus.BAD_REQUEST);
			}
			
			
			/*Permission permission = (Permission) ObjectMapperUtiliity.convertDtoToEntity(permissionDto,Permission.class);
			permission.setCreatedDate(new Date());
			permission.setModifiedDate(new Date());
			permission.setIsActive(true);
			permission =permissionService.createPermission(permission);
			return new ResponseEntity<>((PermissionDto)ObjectMapperUtiliity.convertToDto(permission, PermissionDto.class),HttpStatus.CREATED);*/
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	


	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<PermissionDto> deletePermission(@PathVariable Long id){
		logger.info("deletePermission()  by id code controller initiated");
		try{
			Permission permission = permissionService.deletePermission(id);
			if(permission==null){
				throw new ResourceNotFoundException("Permission not found");
			}
			return new ResponseEntity<PermissionDto>(HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	

	@RequestMapping(value="/code/{uuid}", method=RequestMethod.DELETE)
	public ResponseEntity<PermissionDto> deletePermission(@PathVariable String uuid){
		logger.info("deletePermission()  by uuid code controller initiated");
		try{
			Permission permission = permissionService.deletePermission(uuid);
			if(permission==null){
				throw new ResourceNotFoundException("Permission not found");
			}
			return new ResponseEntity<PermissionDto>(HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

}
