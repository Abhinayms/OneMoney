package com.sevya.launchpad.commons;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:constants.properties")
@ConfigurationProperties(prefix = "constans")
public class ConstantProperties {

	private String mail;
	private String mailFrom;
	
	public String getMail() {
		return mail;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	
}
