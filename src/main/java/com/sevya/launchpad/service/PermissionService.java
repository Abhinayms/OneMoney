package com.sevya.launchpad.service;

import org.springframework.data.domain.Page;

import com.sevya.launchpad.dto.PaginationDto;
import com.sevya.launchpad.model.Permission;

public interface PermissionService {
	
	/**
	 * @param id  permission id
	 * @return Permission
	 * */
	public Permission getPermission	(Long id);
	
	
	/**
	 * Returns the permission by UUID code
	 * @param uuid  UUID code
	 * @return Permission
	 * */
	public Permission getPermissionByUUIDCode	(String uuidCode);
	
	
	/**
	 * @return Page<Permission> List of Permissions
	 * */
	public Page<Permission> getAllPermissions(PaginationDto paginationDto);
	
	
	/**
	 * @param permission
	 * @return Permission
	 * */
	public Permission createPermission(Permission permission);
	
	
	/**
	 * @param id  permission id
	 * @return Permission
	 * */
	public Permission deletePermission(Long id);
	
	
	
	/**
	 * @param uuidCode UUIDCode
	 * @return Permission
	 * */
	public Permission deletePermission(String uuidCode);
	
	
	/**
	 * @param user User
	 * @param permission Permission
	 * @return boolean true/false
	 * */
	public Boolean isUserHavingPermission(long userId, long permissionId);
	
	/**
	 * @param group Group
	 * @param permission Permission
	 * @return boolean true/false
	 * */
	public Boolean isGroupHavingPermission(long groupId, long permissionId);
	
	/**
	 * @param userId UserId
	 * @return Iterable<Permission> list of permissions
	 * */
	public Iterable<Permission> getPermissionsForUser(Long userId);
	
	/**
	 * @param group Group
	 * @return Iterable<Permission> list of permissions
	 * */
	public Iterable<Permission> getPermissionsForGroup(Long groupId);
	
	
}
