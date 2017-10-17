package com.sevya.onemoney.service;

import com.sevya.onemoney.model.UserDetails;

public interface UserDetailsService {
	
	public UserDetails createUserDetails(UserDetails userDetails);

	public UserDetails getUserDetailsByUserId(Long id);

}
