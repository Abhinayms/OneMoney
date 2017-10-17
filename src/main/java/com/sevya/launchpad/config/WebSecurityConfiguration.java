package com.sevya.launchpad.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.sevya.launchpad.security.jwt.JwtAuthFilter;
import com.sevya.launchpad.security.jwt.JwtAuthenticationEntryPoint;
import com.sevya.launchpad.security.jwt.JwtAuthenticationProvider;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


		@Autowired
		private JwtAuthFilter jwtAuthFilter;

		@Autowired
		private JwtAuthenticationProvider jwtAuthenticationProvider;

		@Autowired
		private JwtAuthenticationEntryPoint jwtAuthEndPoint;

		public void configure(AuthenticationManagerBuilder auth)
				throws Exception {
			auth.authenticationProvider(jwtAuthenticationProvider);
		}

		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.antMatcher("/api/**").authorizeRequests()
				.antMatchers("/api/authenticate","/api/v1/signup","/api/v1/password/forgot","/api/v1/validateotp","/bin/**","/controller/**")
				.permitAll()
				.antMatchers("/api/**/*")
				.hasAuthority("ROLE_USER")
				.and()
				.addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthEndPoint);
		}


	@Override
	public void configure(WebSecurity web) throws Exception {
		// web.ignoring().antMatchers("/authenticate","/assets/**","/*.js","/","/index.html");
	}
}
