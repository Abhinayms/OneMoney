package com.sevya.launchpad.util;

import com.sevya.launchpad.dto.MailDto;

public class NativeMailer implements Mailer {

	@Override
	public boolean send(MailDto mailDto) {
		
		return false;
	}

}
