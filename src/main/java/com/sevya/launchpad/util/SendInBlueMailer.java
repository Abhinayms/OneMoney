package com.sevya.launchpad.util;

import com.sevya.launchpad.dto.MailDto;
import com.sevya.launchpad.error.ResourceNotFoundException;

public class SendInBlueMailer implements Mailer {
	
	@Override
	public boolean send(MailDto mailDto) throws ResourceNotFoundException {
		try{
			if((mailDto.getBaseUrl() == null || mailDto.getAccessKey() == null)){
				throw new ResourceNotFoundException("AccessUrl and AccessKey should not be null.");
			}else if(mailDto.getTemplateId() == null){
				throw new ResourceNotFoundException("TemplateId should not be null.");
			}else if(mailDto.getVariables() == null){
				throw new ResourceNotFoundException("Variables should not be null.");
			}
			mailDto = EmailUtility.sendMail(mailDto);
			if(mailDto.getCode().equals("success")){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return false;
	}

}
