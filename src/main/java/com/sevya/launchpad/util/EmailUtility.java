package com.sevya.launchpad.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.sevya.launchpad.dto.MailDto;

public class EmailUtility {
	
	public static MailDto sendMail(MailDto mailDto) throws Exception{		
		
		Mailin http = new Mailin(mailDto.getBaseUrl(),mailDto.getAccessKey());
		Map < String, String > headers = new HashMap < String, String > ();
        headers.put("Content-Type", "text/html; charset=iso-8859-1");

        Map < String, Object > data = new HashMap < String, Object > ();
        data.put("id", mailDto.getTemplateId());
        data.put("to", mailDto.getEmail());
        data.put("replyto", mailDto.getReplyTo() != null ? mailDto.getReplyTo() : "");
        data.put("attr", mailDto.getVariables());
        data.put("headers", headers);
        String status = http.send_transactional_template(data);
        return new Gson().fromJson(status, MailDto.class);
	}
	
	public static MailDto sendMailWithCC(MailDto mailDto){		
		
		Mailin http = new Mailin(mailDto.getBaseUrl(),mailDto.getAccessKey());
        Map < String, String > headers = new HashMap < String, String > ();
        headers.put("Content-Type", "text/html; charset=iso-8859-1");
      
        Map < String, Object > data = new HashMap < String, Object > ();
        data.put("id", mailDto.getTemplateId());
        data.put("to", mailDto.getEmail());
        data.put("cc", mailDto.getCcMails());
        data.put("replyto", mailDto.getReplyTo());
        data.put("attr", mailDto.getVariables());
        data.put("headers", headers);
        String status = http.send_transactional_template(data);
        return new Gson().fromJson(status, MailDto.class);
	}
}
