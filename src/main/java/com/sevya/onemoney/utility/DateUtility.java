package com.sevya.onemoney.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtility {
	
	public static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	public static final DateFormat DATE_FORMATTER_FOR_SQL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final DateFormat DATE_FORMATTER_FROM_STRING_TO_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	private DateUtility() { }
	
	public static Integer getCurrentMonth(){
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static Integer getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static String getLastDateOfMonthAndFirstDateOfNextMonth(Integer year,Integer month,Integer day){
		
		Calendar calendar = Calendar.getInstance();  
        if(day == 0){
        	calendar.set(year,month,day);
	        calendar.set(Calendar.HOUR_OF_DAY, 23);
	        calendar.set(Calendar.MINUTE, 59);
	        calendar.set(Calendar.SECOND, 59);
        }else if(day == 1){
        	calendar.set(year,month,day);
	        calendar.set(Calendar.HOUR_OF_DAY, 00);
	        calendar.set(Calendar.MINUTE, 00);
	        calendar.set(Calendar.SECOND, 00);
        }
        
        return DATE_FORMATTER_FOR_SQL_FORMAT.format(calendar.getTime());
   }
	
	public static String getLast3rdMonthFirstDateAndPreviousMonthLastDate(Integer year,Integer month,Integer day,Integer noOfMonths){
		
		Calendar pastMonths = Calendar.getInstance();
		Calendar previousMonth = Calendar.getInstance();
		Date date = null;
		if(day == 1){
			pastMonths.set(year,month-1,day,0,0,0);
			pastMonths.set(Calendar.MONTH, pastMonths.get(Calendar.MONTH) - noOfMonths);
			date = pastMonths.getTime();
		}else if(day == 0){
			previousMonth.set(year,month-1,day,23,59,59);
			date = previousMonth.getTime();
		}
		
		if(date != null){
			return DATE_FORMATTER_FOR_SQL_FORMAT.format(date);
		}else{
			return null;
		}
		
	}
	
	public static Map<String,String> getDates(Integer noOfMonths){
		
		Map<String,String> dates = new HashMap<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		String previousMonthLastDate = DateUtility.DATE_FORMATTER_FOR_SQL_FORMAT.format(calendar.getTime());
    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-noOfMonths);
    	calendar.set(Calendar.SECOND,calendar.get(Calendar.SECOND)+1);
    	String last3rdMonthDate = DateUtility.DATE_FORMATTER_FOR_SQL_FORMAT.format(calendar.getTime());
		
		dates.put("previousMonthLastDate", previousMonthLastDate);
		dates.put("last3rdMonthFirstDate", last3rdMonthDate);
		return dates;
	}
	
	public static Long getMonthDifferenceBetween2Dates(Date presentDate,Date futureDate) throws Exception{
		
		Calendar futureCalendar = Calendar.getInstance();
		futureCalendar.setTime(futureDate);
		int futureDay = futureCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
		int futureMonth = futureCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
		int futureYear = futureCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
		LocalDate future = LocalDate.of(futureYear,futureMonth,futureDay);
		
		Calendar presentCalendar = Calendar.getInstance();
		presentCalendar.setTime(presentDate);
		LocalDate present = presentCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return ChronoUnit.MONTHS.between(present,future);
	}
	
	public static Integer getYearByDate(Date date) throws Exception {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
		
	}
	
	
	public static Integer getMonthsDifference(Date startDate,Date endDate) throws Exception {
		
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		return  diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		
	}

	public static Date getEndDate(Date date) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, 23);
		c.add(Calendar.MINUTE, 59);
		c.add(Calendar.SECOND, 59);
		date = c.getTime();
		
		return date;
	}
	
	
	
	
	
	
}
