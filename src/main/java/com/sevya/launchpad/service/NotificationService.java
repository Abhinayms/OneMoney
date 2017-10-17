package com.sevya.launchpad.service;

import com.sevya.launchpad.dto.MailDto;
import com.sevya.launchpad.error.ResourceNotFoundException;

public interface NotificationService {

	public boolean sendNotification(MailDto mailDto) throws ResourceNotFoundException;
}
