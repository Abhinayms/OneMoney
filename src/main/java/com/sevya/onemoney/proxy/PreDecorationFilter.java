package com.sevya.onemoney.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sevya.launchpad.security.jwt.JwtAuthenticatedUserDto;
import com.sevya.launchpad.security.jwt.JwtService;

public class PreDecorationFilter extends ZuulFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JwtAuthenticatedUserDto authenticateDto;
		try {
			authenticateDto = jwtService.verify(auth.getCredentials().toString());
			if(authenticateDto != null){
				ctx.addZuulRequestHeader("X-eWise-User",authenticateDto.getMobile());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
		
		/*RequestContext ctx = RequestContext.getCurrentContext();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JwtAuthenticatedUserDto authenticateDto;
		try {
			authenticateDto = jwtService.verify(auth.getCredentials().toString());
			User user = userService.getUserByMobile(authenticateDto.getMobile());
			if(user != null){
				ctx.addZuulRequestHeader("X-eWise-User",user.getMobile());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;*/
		
		
	}

}
