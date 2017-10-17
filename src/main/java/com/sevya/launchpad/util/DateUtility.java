package com.sevya.launchpad.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	
	public static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	
	
	
	public static Date addDaysToDate(Date date,int days) {
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, days);
	    return cal.getTime();
	    
	}	
	
	
	public static  Date getDate(String date) throws Exception{
		if(date!=null){
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			return dateFormat.parse(date);
		}else{
			return null;
		}
	}
	
	public static String convertFromSQLDateToJAVADateString(Date sqlDate) {
        Date javaDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return simpleDateFormat.format(javaDate).toString();
    }
	
}
