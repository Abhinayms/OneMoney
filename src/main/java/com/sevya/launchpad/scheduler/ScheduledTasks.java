package com.sevya.launchpad.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /*@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        System.out.println(dateFormat.format(new Date()));
    }
    
    @Scheduled(fixedRate = 10000)
    public void dbJob() {

    	System.out.println("DB Job");
    
    }*/
}
