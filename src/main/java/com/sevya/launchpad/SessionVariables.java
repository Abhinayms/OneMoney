package com.sevya.launchpad;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sevya.launchpad.model.User;

public class SessionVariables {
	
	private SessionVariables() {}

	public static Integer getLoginFail() {
		Object sessionValue = getSession().getAttribute("loginFail");
		return sessionValue != null ? (Integer)sessionValue : null;
	}

	public static void setLoginFail(int loginFail) {
		getSession().setAttribute("loginFail", loginFail);
	}
	
    
	public static Long getUserId() {
		Object sessionValue = getSession().getAttribute("userId");
		return sessionValue != null ? (Long)sessionValue : null;
	}

	public static void setUserId(Long userId) {
		getSession().setAttribute("userId", userId);
	}
	
	
	public static void setUser(User user) {
		getSession().setAttribute("user", user);
	}

	public static User getUser() {
		Object sessionValue = getSession().getAttribute("user");
		return sessionValue != null ? (User)sessionValue : null;
	}

	public static String getUserName() {
		Object sessionValue = getSession().getAttribute("userName");
		return sessionValue != null ? sessionValue.toString() : null;
	}

	public static void setUserName(String userName) {
		 getSession().setAttribute("userName", userName);
	}
	
	public static Boolean getIsAdmin(){
		Object sessionValue = getSession().getAttribute("isAdmin");
		return sessionValue != null ? new Boolean(sessionValue.toString()) : null;
	}
	public static void setIsAdmin(boolean isAdmin) {
		getSession().setAttribute("isAdmin", isAdmin);
	}
	
	public static String getEmail() {
		Object sessionValue = getSession().getAttribute("email");
		return sessionValue != null ? sessionValue.toString() : null;
	}

	public static void setEmail(String userName) {
		 getSession().setAttribute("email", userName);
	}

	public static String getUserActive() {
		Object sessionValue = getSession().getAttribute("userActive");
		return sessionValue != null ? sessionValue.toString() : null;
	}

	public static void setUserActive(String userActive) {
		 getSession().setAttribute("userActive",userActive);
	}

	public static Integer getCheckSuccess() {
		Object sessionValue = getSession().getAttribute("checkSuccess");
		return sessionValue != null ? (Integer)sessionValue : null;
	}

	public static void setCheckSuccess(int checkSuccess) {
		 getSession().setAttribute("checkSuccess", checkSuccess);
	}
	
	
	public static Integer getSurveyID() {
		Object sessionValue = getSession().getAttribute("surveyID");
		return sessionValue != null ? (Integer)sessionValue : null;
	}
	
	public static void setSurveyID(int surveyID) {
		getSession().setAttribute("surveyID", surveyID);
	}
	
	
	public static void setSessionToNull()
	{
		HttpSession httpSession = getSession();
		httpSession.invalidate();
	}
	
	
	public static HttpSession getSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		return attr.getRequest().getSession();
	}

	public static Integer getPasswordRetries() {
		Object sessionValue = getSession().getAttribute("passwordRetries");
		 return sessionValue != null ? (Integer)sessionValue : null;
	}

	public static void setPasswordRetries(int passwordRetries) {
		getSession().setAttribute("passwordRetries", passwordRetries);
		
	}

	public static String getSurveyTitle() {
		Object sessionValue = getSession().getAttribute("surveyTitle");
		return sessionValue != null ? sessionValue.toString() : null;
	}

	public static void setSurveyTitle(String surveyTitle) {
		getSession().setAttribute("surveyTitle", surveyTitle);
	}
	

	public static String getSurveyCode() {
		Object sessionValue = getSession().getAttribute("surveyCode");
		return sessionValue != null ? sessionValue.toString() : null;
	}

	public static void setSurveyCode(String surveyCode) {
		getSession().setAttribute("surveyCode", surveyCode);
	}
	
	public static String getTimeZone(){
		Object sessionValue = getSession().getAttribute("timeZone");
		return sessionValue != null ? sessionValue.toString() : null; 
	}
	
	public static void setTimeZone(String zone) {
		getSession().setAttribute("timeZone", zone);
	}
	
	public static String getSurveyUrl(){
		Object sessionValue = getSession().getAttribute("surveyUrl");
		return sessionValue != null ? sessionValue.toString() : null; 
	}
	
	public static void setSurveyUrl(String surveyUrl) {
		getSession().setAttribute("surveyUrl", surveyUrl);
	}
	
	public static String[] getSurveyCodeList(){
		Object sessionValue = getSession().getAttribute("surveyCodeList");
		return sessionValue != null ? (String[])sessionValue : null; 
	}
	
	public static void setSurveyCodeList(String[] surveyCodeList) {
		getSession().setAttribute("surveyCodeList", surveyCodeList);
	}
	
	public static Integer getTotalRecords() {
		Object sessionValue = getSession().getAttribute("totalRecords");
		return sessionValue != null ? (Integer)sessionValue : null;
	}

	public static void setTotalRecords(int totalRecords) {
		getSession().setAttribute("totalRecords", totalRecords);
	}
}
