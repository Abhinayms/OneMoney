package com.sevya.launchpad.security;

import org.springframework.security.core.GrantedAuthority;

import com.amazonaws.services.identitymanagement.model.Role;

public class AppRoleDetails extends Role  implements GrantedAuthority {
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return super.getRoleName();
	}
	
}
