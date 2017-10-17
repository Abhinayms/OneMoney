package com.sevya.launchpad.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.sevya.launchpad.model.Role;
import com.sevya.launchpad.model.User;

public class AppUserDetails extends User implements UserDetails {

	private static final long serialVersionUID = 1L;
	private List<String> userRoles;

	// Spring Security fields
	//private List<Role> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	public AppUserDetails(User user, List<String> userRoles) {

		super(user);
		this.userRoles = userRoles;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		String roles = StringUtils.collectionToCommaDelimitedString(userRoles);
		return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

	}

	@Override
	public String getUsername() {
		return super.getEmail();
	}

	// Spring Security fields
	@Override
/*	public List<Role> getAuthorities() {

		return this.authorities;
	}*/

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}