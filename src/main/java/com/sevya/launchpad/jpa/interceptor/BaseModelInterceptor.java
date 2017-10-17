package com.sevya.launchpad.jpa.interceptor;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sevya.launchpad.model.User;
import com.sevya.launchpad.security.jwt.JwtService;

@SuppressWarnings("serial")
@Component
public class BaseModelInterceptor extends EmptyInterceptor {

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private JwtService jwtService;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		String token = "";
		Long userId = null;
		User user = new User();
		
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if(credentials != null){
			token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		}
		
		if(!token.isEmpty()){
			
			try {
				userId = jwtService.verify(token).getUserId();
			} catch (IOException | URISyntaxException e) {
				logger.error(e.getMessage(), e);
			}
			
			user.setId(userId);
			setValue(state, propertyNames, "createdBy", user);
		}

		setValue(state, propertyNames, "createdDate", new java.util.Date());
		
		return true;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] state, Object[] previousState,
			String[] propertyNames, Type[] types) {

		String token = "";
		Long userId = null;
		User user = new User();
		
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if(credentials != null){
			token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		}
		
		if(!token.isEmpty()){
		
			try {
				userId = jwtService.verify(token).getUserId();
			} catch (IOException | URISyntaxException e) {
				logger.error(e.getMessage(), e);
			}
	
			user.setId(userId);
			setValue(state, propertyNames, "modifiedBy", user);
			
		}
		
		setValue(state, propertyNames, "modifiedDate", new java.util.Date());

		return true;
	}

	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		String token = "";
		Long userId = null;
		User user = new User();
		
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if(credentials != null){
			token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		}
		
		if(!token.isEmpty()){
			
			try {
				userId = jwtService.verify(token).getUserId();
			} catch (IOException | URISyntaxException e) {
				logger.error(e.getMessage(), e);
			}
	
			user.setId(userId);
			setValue(state, propertyNames, "modifiedBy", user);
			
		}
		
		setValue(state, propertyNames, "modifiedDate", new java.util.Date());
		
	}

	private void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Object value) {
		int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
		if (index >= 0) {
			currentState[index] = value;
		}
	}

}