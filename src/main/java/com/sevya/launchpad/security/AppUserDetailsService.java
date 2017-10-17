package com.sevya.launchpad.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.repository.RoleRepository;
import com.sevya.launchpad.repository.UserRepository;

@Service("appUserDetailsService")
public class AppUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public AppUserDetailsService(UserRepository userRepository,
			RoleRepository roleRepository) {

		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {

		User user = userRepository.getUserByEmail(email);

		if (null == user) {

			throw new UsernameNotFoundException(
					"No user present with username: " + email);

		} else {

			List<String> roles = roleRepository.getRoleByEmail(email);

			return new AppUserDetails(user, roles);

		}
	}
}