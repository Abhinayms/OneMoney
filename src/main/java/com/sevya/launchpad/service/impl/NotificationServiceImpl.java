package com.sevya.launchpad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sevya.launchpad.dto.MailDto;
import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.User;
import com.sevya.launchpad.service.NotificationService;
import com.sevya.launchpad.service.UserService;
import com.sevya.launchpad.util.Mailer;
import com.sevya.launchpad.util.SendInBlueMailer;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserService userService;
	
	@Override
	public boolean sendNotification(MailDto mailDto) throws ResourceNotFoundException {
		boolean flag = false;
		try {
			if(mailDto.getUserId() != null){
				User user = userService.getUser(mailDto.getUserId());
				if(user == null)
					throw new ResourceNotFoundException("User doesn't exist");
				mailDto.setEmail(user.getEmail());
				Mailer email = new SendInBlueMailer();
				flag = email.send(mailDto);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
