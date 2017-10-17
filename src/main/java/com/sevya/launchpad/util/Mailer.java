package com.sevya.launchpad.util;

import com.sevya.launchpad.dto.MailDto;

public interface Mailer {
	
	public boolean send(MailDto mailDto) throws Exception;

}
