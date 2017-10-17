package com.sevya.launchpad.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends BaseModel{
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	@Column( nullable = false, unique = true)
	private String mobile;
	private String email;
	@Column( nullable = false)
	private String password;
	private String avtar;
	private Integer logInFailureAttempts;
	@Column( nullable = false)
	private String countryCode;
	
	public User() {

	}
	public User(User user) {
		  this.firstName = user.firstName;
		  this.middleName = user.middleName;
		  this.lastName =  user.lastName;
		  this.gender = user.gender;
		  this.mobile = user.mobile;
		  this.email = user.email;
		  this.password = user.password;
		  this.avtar = user.avtar;
		  this.logInFailureAttempts = user.logInFailureAttempts;
		  this.countryCode = user.countryCode;
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Set<UserRoleMapper> userRoleMappers;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Set<UserGroupMapper> userGroupMappers;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAvtar() {
		return avtar;
	}
	public void setAvtar(String avtar) {
		this.avtar = avtar;
	}
	
	public Set<UserRoleMapper> getUserRoleMappers() {
		return userRoleMappers;
	}
	public void setUserRoleMappers(Set<UserRoleMapper> userRoleMappers) {
		this.userRoleMappers = userRoleMappers;
	}
	
	public Set<UserGroupMapper> getUserGroupMappers() {
		return userGroupMappers;
	}
	public void setUserGroupMappers(Set<UserGroupMapper> userGroupMappers) {
		this.userGroupMappers = userGroupMappers;
	}
	public Integer getLogInFailureAttempts() {
		return logInFailureAttempts;
	}
	public void setLogInFailureAttempts(Integer logInFailureAttempts) {
		this.logInFailureAttempts = logInFailureAttempts;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
}
