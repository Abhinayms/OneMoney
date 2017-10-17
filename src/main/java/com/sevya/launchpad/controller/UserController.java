package com.sevya.launchpad.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.sevya.launchpad.dto.GroupRoleDto;
import com.sevya.launchpad.dto.PaginationDto;
import com.sevya.launchpad.dto.RoleDto;
import com.sevya.launchpad.dto.UserDto;
import com.sevya.launchpad.dto.UserGroupDto;
import com.sevya.launchpad.dto.UserRoleDto;
import com.sevya.launchpad.dto.mapper.GroupDtoMapper;
import com.sevya.launchpad.dto.mapper.PaginationDtoMapper;
import com.sevya.launchpad.dto.mapper.RoleDtoMapper;
import com.sevya.launchpad.dto.mapper.UserDtoMapper;
import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.Group;
import com.sevya.launchpad.model.Role;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.model.UserRequests.RequestType;
import com.sevya.launchpad.service.UserService;
import com.sevya.launchpad.util.LaunchpadUtility;

@RestController
@RequestMapping("api/user")
public class UserController extends BaseController {
	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDto> getUser(@PathVariable(value="id") Long id) {
		
		logger.info("UserController getUser()");
		try {
			User user = userService.getUser(id);
			if(user!=null){
				return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(user),HttpStatus.OK);	
			}else{
				throw new ResourceNotFoundException("User doesn't exists.");
			}		
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	
	@RequestMapping(value = "/code/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<UserDto> getUser(@PathVariable(value="uuid") String uuid) {
		
		logger.info("UserController getUser()");
		try {
			User user = userService.getUserByUUIDCode(uuid);
			if(user!=null){
				return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(user),HttpStatus.OK);	
			}else{
				throw new ResourceNotFoundException("User doesn't exists.");
			}		
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PaginationDto> getAllUsers(PaginationDto paginationDto) {
		
		logger.info("UserController getAllUsers()");
		try {
	
			Page<User> users = userService.getAllUsers(paginationDto);
			paginationDto = PaginationDtoMapper.toPaginationDto(users, paginationDto);
			paginationDto.setContent(UserDtoMapper.toUserDtos(users));
			return new ResponseEntity<PaginationDto>(paginationDto,HttpStatus.OK);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<PaginationDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto,HttpServletRequest request){
		
		logger.info("UserController createUser()");
		
		try {
			
			User user = UserDtoMapper.toUser(userDto);
			user = userService.createUser(user);
			userService.generateRequestOTP(user, LaunchpadUtility.getIP(request), RequestType.activation);
			return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(user),HttpStatus.CREATED);
		
		} catch(Exception e) {
			
			return new ResponseEntity<UserDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
	
	
	@RequestMapping(value = "/{activateToken}", method = RequestMethod.POST)
	public ResponseEntity<?> activateUser(@PathVariable(value="activateToken") String activateToken){
		
		logger.info("UserController activateUser()");
		
		try {
			
			User user = userService.activateUserByUserToken(activateToken);
			if (user != null) {
				userService.generateApiCredentialsForUser(user);
				return new ResponseEntity<>(HttpStatus.OK); 
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
			}
		} catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
	

	
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto){
		
		logger.info("UserController updateUser()");
		
		try {
			User user = userService.getUser(userDto.getId());
			if(user!=null){
				user = userService.updateUser(UserDtoMapper.toUser(userDto, user));
				return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(user),HttpStatus.OK);	
			}else{
				throw new ResourceNotFoundException("User doesn't exists.");
			}
		}catch(Exception e) {
			return new ResponseEntity<UserDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	
	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable(value="id") Long id){
		
		logger.info("UserController deleteUser()");
		try {
				User user = userService.getUser(id);
				if(user!=null){					
					user = userService.deleteUser(user);
					return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(user),HttpStatus.OK);	
				}else{
					throw new ResourceNotFoundException("User doesn't exists.");
				}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/code/{uuid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable(value="uuid") String uuid){
		logger.info("UserController deleteUser()");
		try {
				User user = userService.getUserByUUIDCode(uuid);
				if(user!=null){					
					user = userService.deleteUser(user);
					return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(user),HttpStatus.OK);	
				}else{
					throw new ResourceNotFoundException("User doesn't exists.");
				}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/addrole", method = RequestMethod.POST)
	public ResponseEntity<?> addUserToRole(@RequestBody UserRoleDto userRoleDto) {
		
		logger.info("UserController addUserToRole()");
		try {
			userService.addUsersToRole(userRoleDto.getUserIds(), userRoleDto.getRoleId());
			return new ResponseEntity<>(userRoleDto,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/addgroup", method = RequestMethod.POST)
	public ResponseEntity<?> addUserToGroup(@RequestBody UserGroupDto userGroupDto) {
		
		logger.info("UserController addUserToGroup()");
		
		try {
			
			userService.addUsersToGroup(userGroupDto.getUserIds(),userGroupDto.getGroupId());
			return new ResponseEntity<>(userGroupDto,HttpStatus.OK);
		
		}catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	
	}
	
	@RequestMapping(value = "/group/{groupId}/users", method = RequestMethod.GET)
	public ResponseEntity<?> getUsersInGroup(@PathVariable(value="groupId") Long groupId) {
		logger.info("UserController getUsersInGroup()");
		
		try {
			
			Iterable<User> users= userService.getUsersInGroup(groupId);
			return new ResponseEntity<>(UserDtoMapper.toUserDtos(users),HttpStatus.OK);
		
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/group/code/{uuid}/users", method = RequestMethod.GET)
	public ResponseEntity<List<UserDto>> getUsersInGroup(@PathVariable(value="uuid") String uuid) {
		logger.info("getUsersInGroup() by uuid code controller initiated...");
		try {
			Iterable<User> users= userService.getUsersInGroup(uuid.trim());
			return new ResponseEntity<List<UserDto>>(UserDtoMapper.toUserDtos(users),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	@RequestMapping(value = "/{userId}/roles", method = RequestMethod.GET)
	public ResponseEntity<?> getRolesForUser(@PathVariable(value="userId") Long userId) {
	
		logger.info("UserController getRolesForUser()");
		
		try {
			
			Iterable<Role> roles= userService.getRolesForUser(userId);
			return new ResponseEntity<>(RoleDtoMapper.toRoleDtos(roles),HttpStatus.OK);
		
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
	}
	
	
	
	@RequestMapping(value = "/code/{userUuid}/roles", method = RequestMethod.GET)
	public ResponseEntity<List<RoleDto>> getRolesForUser(@PathVariable(value="userUuid") String userUuid) {
		logger.info("getRolesForUser() by user UUID code controller initiated...");
		try {
			Iterable<Role> roles= userService.getRolesForUser(userUuid.trim());
			return new ResponseEntity<List<RoleDto>>(RoleDtoMapper.toRoleDtos(roles),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/group/{groupId}/roles", method = RequestMethod.GET)
	public ResponseEntity<?> getRolesInGroup(@PathVariable(value="groupId") Long groupId) {
		
		logger.info("UserController getRolesInGroup()");
		
		try {
			
			Iterable<Role> roles= userService.getRolesInGroup(groupId);
			return new ResponseEntity<>(RoleDtoMapper.toRoleDtos(roles),HttpStatus.OK);
		
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}
	
	
	@RequestMapping(value = "/roles/{roleId}/group", method = RequestMethod.GET)
	public ResponseEntity<?> getGroupInRole(@PathVariable(value="roleId") Long roleId) {
		
		logger.info("UserController getGroupInRole()");
		
		try {
			
			Group group = userService.getGroupInRole(roleId);
			return new ResponseEntity<>(GroupDtoMapper.toGroupDto(group),HttpStatus.OK);
		
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}
	
	
	@RequestMapping(value = "/group/addrole", method = RequestMethod.POST)
	public ResponseEntity<?> addRoleToGroup(GroupRoleDto groupRoleDto) {
		
		logger.info("UserController addUserToGroup()");
		
		try {
			
			userService.addRoleToGroup(groupRoleDto.getGroupId(),groupRoleDto.getRoleId());
			return new ResponseEntity<>(groupRoleDto,HttpStatus.OK);
		
		}catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}

	}
	

}