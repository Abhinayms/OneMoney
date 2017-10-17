package com.sevya.launchpad.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.sevya.launchpad.dto.PaginationDto;
import com.sevya.launchpad.model.Group;
import com.sevya.launchpad.model.GroupRoleMapper;
import com.sevya.launchpad.model.Role;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.model.UserRequests.RequestType;

public interface UserService {
	
	public User getUserByAuthenticationToken() throws Exception;
	
	public void generateApiCredentialsForUser(User user);
	
	public UserRequests createUserRequest(UserRequests userRequests);
	
	public UserRequests generateRequestToken(User user, String ipAddress, RequestType requestType);
	
	public UserRequests generateRequestOTP(User user, String ipAddress, RequestType requestType)throws Exception;
	
	public UserRequests getUserRequestsByEmailAndActivationKey(String email, String activationKey);
	
	public UserRequests getUserRequestsByMobileAndCountryCodeAndActivationKey(String mobile, String countryCode, String activationKey);
	
	public User activateUserByUserToken(String activateToken);
	
	public UserRequests validateToken(UserRequests userRequests);
	
	public String getAuthTokenByUser(User user) throws Exception;
	
	public User getUser(Long userId);
	
	public User createUser(User user);
	
	public Page<User> getAllUsers(PaginationDto paginationDto);

	public User updateUser(User user);
	
	public void deleteUser(Long userId);
	
	public User deleteUser(User user);
	
	public void addUsersToRole(List<Long> userIds, Long roleId);
	
	public void addUsersToGroup(List<Long> userIds, Long groupId);
	
	public GroupRoleMapper addRoleToGroup(Long roleId,Long groupId);
		
	public Group getGroupInRole(Long roleId);
	
	public Iterable<User> getUsersInGroup(Long groupId);
	
	/**
	 * Returns a list of user by group uuid code
	 * @param groupUuidCode UUID code
	 * */
	public Iterable<User> getUsersInGroup(String groupUuidCode);
	
	
	public Iterable<User> getUsersInRole(Long roleId);
	
	public Iterable<Role> getRolesForUser(Long userId);
	
	/**
	 * Returns a list of roles by user uuid code
	 * @param groupUuidCode UUID code
	 * */
	public Iterable<Role> getRolesForUser(String userUuid);
	
	
	
	public Iterable<Role> getRolesInGroup(Long groupId);
	
	public boolean isUserInRole(Long userId,Long roleId);
	
	public boolean isUserInGroup(Long userId, Long groupId);
	
	public boolean isRoleInGroup(Long roleId, Long groupId);
	
	public boolean isGroupInRole(Long groupId, Long roleId);
	
	
	
	/**
	 * Returns the User by UUID code
	 * @param uuidCode UUID code
	 * */
	public User getUserByUUIDCode(String uuidCode);

	/**
	 *@param email 
	 *@return User 
	 * */
	public User getUserByEmail(String email);
	
	
	/**
	 *@param mobile 
	 *@return User 
	 * */
	public User getUserByMobile(String mobile);
	
	/**
	 *@param mobile 
	 *@return User 
	 * */
	public User getExistedUserByMobileAndCountryCode(String mobile,String countryCode);
	
	/**
	 * @param id  roleId
	 * @return Role
	 * */
	public Role getRole	(Long id);
	
	/**
	 * @return Iterable<Roles> List of Roles
	 * */
	public Iterable<Role> getAllRoles();
	
	
	/**
	 * @param role Role 
	 * @return Role
	 * */
	public Role createRole(Role role);
	
	
	/**
	 * @param id  roleId
	 * @return Role
	 * */
	public Role deleteRole(Long id);

	public Group getGroupByGroupUUID(String uuid);
	
	public Group getGroupById(Long groupId);

	public User getUserByMobileAndCountryCode(String mobile, String countryCode);

		
}
