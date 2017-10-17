package com.sevya.launchpad.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sevya.launchpad.dto.PaginationDto;
import com.sevya.launchpad.model.Group;
import com.sevya.launchpad.model.Permission;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.repository.PermissionRepository;
import com.sevya.launchpad.service.PermissionService;
import com.sevya.launchpad.util.PaginationUtility;

@Service
public class PermissionServiceImpl implements PermissionService {
	
	
	@Autowired
	public PermissionRepository permissionRepository; 
	
	
	@Override
	public Permission getPermission(Long id) {
		return permissionRepository.getPermission(id);
	}

	@Override
	public Page<Permission> getAllPermissions(PaginationDto paginationDto) {
		
		Pageable pageable = PaginationUtility.createPageRequest(paginationDto);
		
		if(pageable == null ) {
			List<Permission> permissions =permissionRepository.getAllPermissions();
			Page<Permission> page = new PageImpl<>(permissions);
			
			return page;
		}
		
		if (paginationDto.getCriteria() !=null && !paginationDto.getCriteria().equals("")) {
			return permissionRepository.getAllPermissionsByCriteria(paginationDto.getCriteria(),pageable);
		} else {
			return permissionRepository.getAllPermissions(pageable);
		}
		
	}

	@Override
	public Permission createPermission(Permission permission) {
		return permissionRepository.save(permission);
	}

	@Override
	public Permission deletePermission(Long id) {
		Permission permission = permissionRepository.findOne(id);
		if(permission!=null){
			permission.setIsActive(false);
			//permission.setModifiedDate(new Date());
			return permissionRepository.save(permission);	
		}
		return null;
	}

	@Override
	public Boolean isUserHavingPermission(long userId, long permissionId) {
		User user = permissionRepository.getUserByPermission(userId, permissionId);
		return user!=null?true:false;
	}

	@Override
	public Boolean isGroupHavingPermission(long groupId, long permissionId) {
		Group group= permissionRepository.getGroupByPermission(groupId, permissionId);
		return group!=null?true:false;
	}

	@Override
	public Iterable<Permission> getPermissionsForUser(Long userId) {
		Iterable<Permission> permissions =permissionRepository.getPermissinosByUser(userId);		
		return permissions!=null?permissions:null;
	}

	@Override
	public Iterable<Permission> getPermissionsForGroup(Long groupId) {
		Iterable<Permission> permissions =permissionRepository.getPermissinosByGroup(groupId);
		return permissions!=null?permissions:null;
	}

	@Override
	public Permission getPermissionByUUIDCode(String uuidCode) {
		return permissionRepository.getPermissionByUUIDCode(uuidCode.trim());
	}

	@Override
	public Permission deletePermission(String uuidCode) {
		Permission permission = permissionRepository.getPermissionByUUIDCode(uuidCode.trim());
		if(permission!=null){
			permission.setIsActive(false);
			//permission.setModifiedDate(new Date());
			return permissionRepository.save(permission);	
		}
		return null;
	}


	
	
}
