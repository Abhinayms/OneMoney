package com.sevya.launchpad.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sevya.launchpad.dto.PaginationDto;
import com.sevya.launchpad.dto.mapper.UserRequestsDtoMapper;
import com.sevya.launchpad.model.Group;
import com.sevya.launchpad.model.GroupRoleMapper;
import com.sevya.launchpad.model.Role;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserGroupMapper;
import com.sevya.launchpad.model.UserRequests;
import com.sevya.launchpad.model.UserRequests.RequestType;
import com.sevya.launchpad.model.UserRoleMapper;
import com.sevya.launchpad.repository.GroupRepository;
import com.sevya.launchpad.repository.RoleRepository;
import com.sevya.launchpad.repository.UserRepository;
import com.sevya.launchpad.repository.UserRequestsRepository;
import com.sevya.launchpad.security.jwt.JwtAuthenticatedUserDto;
import com.sevya.launchpad.security.jwt.JwtService;
import com.sevya.launchpad.service.AuthenticationService;
import com.sevya.launchpad.service.UserService;
import com.sevya.launchpad.util.LaunchpadUtility;
import com.sevya.launchpad.util.PaginationUtility;
import com.sevya.onemoney.utility.HttpClientFactory;

@Service
@PropertySource(value = { "classpath:constants.properties" })
public class UserServiceImpl implements UserService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public RoleRepository roleRepository;
	
	@Autowired
	public GroupRepository groupRepository;	

	@Autowired
	private UserRequestsRepository userRequestsRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public User getUserByAuthenticationToken() throws Exception {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JwtAuthenticatedUserDto authenticateDto = jwtService.verify(auth.getCredentials().toString());
		return getUserByMobile(authenticateDto.getMobile());

	}

	
	@Override
	public void generateApiCredentialsForUser(User user){
		authenticationService.generateApiCredentialsForUser(user);
	}

	
	@Override
	public UserRequests createUserRequest(UserRequests userRequests) {
		return userRequestsRepository.save(userRequests);
	}

	
	@Override
	public UserRequests generateRequestToken(User user, String ipAddress, RequestType requestType) {
		
		UserRequests userRequests = UserRequestsDtoMapper.toUserRequestsForToken(user, ipAddress, requestType);
		return userRequestsRepository.save(userRequests);
	
	}
	
	
	@Override
	public UserRequests generateRequestOTP(User user, String ipAddress, RequestType requestType) throws Exception {
		
		
		List<UserRequests> existedUserRequests = userRequestsRepository.getUserRequestsByMobileAndCountryCodeAndRequestType(user.getMobile(),user.getCountryCode(),requestType);
		for(UserRequests userRequest : existedUserRequests) {
			userRequest.setIsActive(false);
			userRequest.setIsComplete(true);
			userRequestsRepository.save(userRequest);
		}
		
		UserRequests userRequests = UserRequestsDtoMapper.toUserRequestsForOTP(user, ipAddress, requestType);
		userRequestsRepository.save(userRequests);
		String messageWithOTP = "Hi "+user.getFirstName()+ " OTP : "+userRequests.getRequestKey()+" for "  +requestType+ " .";
		sendOTP(user.getCountryCode()+user.getMobile(), messageWithOTP);
		return userRequests;
		
	}
	
	
	public void sendOTP(String mobile,String messageWithOTP) throws Exception {
		
			String key   = env.getProperty("constans.SMSCOUNTRY_AUTH_KEY");
			String token = env.getProperty("constans.SMSCOUNTRY_AUTH_TOKEN");
			String url	 = env.getProperty("constans.SMSCOUNTRY_URL");
			String keyToken  = key+":"+token;
			String authToken = DatatypeConverter.printBase64Binary(keyToken.getBytes());
			
			HttpClient httpClient = HttpClientFactory.getHttpsClient();
			HttpPost postRequest = new HttpPost(url);
			postRequest.addHeader("Authorization","Basic "+authToken);
			postRequest.addHeader("Content-Type","application/json");
			String data = "{\"Text\": \""+messageWithOTP+"\",\"Number\": \""+mobile+"\"}";
			
			StringEntity input = new StringEntity(data);
			input.setContentType("application/json");
			postRequest.setEntity(input);	
			httpClient.execute(postRequest);
			
			/* HttpResponse response = httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 202) {
				throw new NullPointerException("Failed : HTTP error code : "+ response.getStatusLine().getStatusCode());
			}*/
			
			postRequest.releaseConnection();
		
	}
	
	
	@Override
	public String getAuthTokenByUser(User user) throws Exception {
		
		JwtAuthenticatedUserDto jwtAuthenticatedUserDto = new  JwtAuthenticatedUserDto();
		jwtAuthenticatedUserDto.setMobile(user.getMobile());
		jwtAuthenticatedUserDto.setUserId(user.getId());
		jwtAuthenticatedUserDto.setUuid(user.getUuid());
		return jwtService.tokenFor(jwtAuthenticatedUserDto);
		
	}
	
	
	@Override
	public UserRequests validateToken(UserRequests userRequests) {
		
		User user = userRequests.getUser();
		
		if(userRequests.getRequestType().equals(RequestType.activation)){
			
			user.setIsActive(true);
			updateUser(user);
			userRequests.setIsComplete(true);
			
		} else if(userRequests.getRequestType().equals(RequestType.forgotPassword)){
			userRequests.setIsComplete(true);
		}
		userRequests.setIsActive(false);
		userRequestsRepository.save(userRequests);
		return userRequests;
		
	}
	
	@Override
	@Transactional
	public User activateUserByUserToken(String activateToken) {
		
		UserRequests userRequests = userRequestsRepository.getUserRequestsByActivateToken(activateToken);
	
		if( userRequests != null ) {	
				
			User user = userRequests.getUser();
			user.setIsActive(true);
			updateUser(user);
			userRequests.setIsComplete(true);
			userRequestsRepository.save(userRequests);
			return user;
			
		} else {
			return null;
		}
		
	}
	
	@Override
	public User getUser(Long userId) {
		return userRepository.getUserById(userId);
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Page<User> getAllUsers(PaginationDto paginationDto) {

		Pageable pageable = PaginationUtility.createPageRequest(paginationDto);
		
		if(pageable == null ) {
			Page<User> page = new PageImpl<User>(userRepository.getAllUsers());
			return page;
		}
		
		if (paginationDto.getCriteria() !=null && !paginationDto.getCriteria().equals("")) {
			return userRepository.getAllUsersBySearchTerm(paginationDto.getCriteria(),pageable);
		} else {
			return userRepository.getAllUsersByPagination(pageable);
		}
	
	}

	@Override
	public User updateUser(User user) {
		user.setModifiedDate(new Date());
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.deleteUserById(userId);
	}


	@Override
	public User deleteUser(User user) {
		user.setIsActive(false);
		user.setModifiedDate(new Date());
		return userRepository.save(user);
	}
	
	
	
	
	@Override
	public void addUsersToRole(List<Long> userIds, Long roleId) {
		
		Role role = roleRepository.findOne(roleId);
		
		for(Long userId : userIds) {
			
			User user = userRepository.findOne(userId);
			UserRoleMapper userRoleMapper = new UserRoleMapper();
			userRoleMapper.setUser(user);
			userRoleMapper.setRole(role);		
			user.getUserRoleMappers().add(userRoleMapper);
			userRepository.save(user);
		
		}
		
	}

	
	@Override
	public void addUsersToGroup(List<Long> userIds, Long groupId) {
		
		Group group = groupRepository.findOne(groupId);
		
		for(Long userId : userIds) {
			
			User user = userRepository.findOne(userId);
			UserGroupMapper userGroupMapper =new UserGroupMapper();
			userGroupMapper.setUser(user);
			userGroupMapper.setGroup(group);	
			user.getUserGroupMappers().add(userGroupMapper);
			userRepository.save(user);
		
		}
		
	}

	
	@Override
	public GroupRoleMapper addRoleToGroup(Long roleId,Long groupId) {
		
		Role role = roleRepository.findOne(roleId);
		Group group = groupRepository.findOne(groupId);
		GroupRoleMapper groupRoleMapper = new GroupRoleMapper();
		groupRoleMapper.setRole(role);
		groupRoleMapper.setGroup(group);
		groupRoleMapper.setUuid(LaunchpadUtility.generateUUIDCode());
		role.getGroupRoleMappers().add(groupRoleMapper);
		roleRepository.save(role);
		return groupRoleMapper;
		
	}

	
	@Override
	public Iterable<User> getUsersInGroup(Long groupId) {
		return userRepository.getUsersByGroupId(groupId);
	}
	
	
	@Override
	public Iterable<User> getUsersInGroup(String groupUuidCode) {
		return userRepository.getUsersByGroupUUID(groupUuidCode);
	}
	

	@Override
	public Iterable<Role> getRolesForUser(Long userId) {
		return roleRepository.getRolesByUserId(userId);
	}

	@Override
	public Iterable<User> getUsersInRole(Long roleId) {
		return userRepository.getUsersByRoleId(roleId);
	}

	@Override
	public Iterable<Role> getRolesInGroup(Long groupId) {
		return roleRepository.getRolesByGroupId(groupId);
	}

	@Override
	public Group getGroupInRole(Long roleId) {
		return groupRepository.getGroupByRoleId(roleId);
	}

	
	@Override
	public boolean isUserInRole(Long userId, Long roleId) {
		User user = userRepository.getUserByUserIdAndRoleId(userId,roleId);
		return user!=null?true:false;
	}
	
	@Override
	public boolean isUserInGroup(Long userId, Long groupId) {
		User user = userRepository.getUserByUserIdGroupId(userId,groupId);
		return user!=null?true:false;
	}
	
	
	@Override
	public boolean isRoleInGroup(Long roleId, Long groupId) {
		Role role = roleRepository.getRoleByRoleIdAndGroupId(roleId,groupId);
		return role!=null?true:false;
	}

	@Override
	public boolean isGroupInRole(Long groupId, Long roleId) {
		Group group = groupRepository.getGroupByGroupIdAndRoleId(groupId,roleId);
		return group!=null?true:false;
	}

	@Override
	public User getUserByMobile(String mobile) {
		return userRepository.getUserByMobile(mobile);
	}
	
	@Override
	public User getExistedUserByMobileAndCountryCode(String mobile,String countryCode) {
		return userRepository.getExistedUserByMobileAndCountryCode(mobile,countryCode);
	}
	
	@Override
	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	@Override
	public Role getRole(Long id) {
		return roleRepository.getRoleByRoleId(id);
	}

	@Override
	public Iterable<Role> getAllRoles() {
		return roleRepository.getAllRoles();
	}

	@Override
	public Role createRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role deleteRole(Long id) {
		Role role = roleRepository.getRoleByRoleId(id);
		if(role!=null){
			role.setIsActive(false);
			role.setModifiedDate(new Date());
			return roleRepository.save(role);	
		}
		return null;
	}

	@Override
	public UserRequests getUserRequestsByEmailAndActivationKey(String email, String activationKey) {
		return userRequestsRepository.getUserByEmailAndActivationKey(email,activationKey);
		
	}
	
	@Override
	public UserRequests getUserRequestsByMobileAndCountryCodeAndActivationKey(String mobile, String countryCode, String activationKey) {
		return userRequestsRepository.getUserRequestsByMobileAndCountryCodeAndActivationKey(mobile, countryCode, activationKey);
		
	}

	@Override
	public User getUserByUUIDCode(String uuidCode) {
		return userRepository.getUserByUUIDCode(uuidCode);
	}

	@Override
	public Iterable<Role> getRolesForUser(String userUuid) {
		return roleRepository.getRolesByUserUUIDCode(userUuid);
	}

	@Override
	public Group getGroupByGroupUUID(String uuid) {
		return groupRepository.getGroupByUUID(uuid);
	}

	@Override
	public Group getGroupById(Long groupId) {
		return groupRepository.getGroupById(groupId);
	}


	@Override
	public User getUserByMobileAndCountryCode(String mobile, String countryCode) {
		return userRepository.getUserByMobileAndCountryCode(mobile, countryCode);
	}

}
