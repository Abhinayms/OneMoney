package com.sevya.onemoney.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sevya.onemoney.model.UserDetails;
import com.sevya.onemoney.repository.UserDetailsRepository;
import com.sevya.onemoney.service.UserDetailsService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Override
	public UserDetails createUserDetails(UserDetails userDetails) {
		return userDetailsRepository.save(userDetails);
	}

	@Override
	public UserDetails getUserDetailsByUserId(Long userId) {
		return userDetailsRepository.getUserDetailsByUserId(userId);
	}

}
