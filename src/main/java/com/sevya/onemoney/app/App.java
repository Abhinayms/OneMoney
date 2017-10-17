package com.sevya.onemoney.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sevya.onemoney.proxy.PreDecorationFilter;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.sevya.launchpad.*","com.sevya.onemoney.*"})
@EnableJpaRepositories({"com.sevya.launchpad.repository","com.sevya.onemoney.repository"})
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan({"com.sevya.launchpad.model","com.sevya.onemoney.model"})
//@EnableScheduling
@SpringBootApplication
@EnableZuulProxy
public class App  extends SpringBootServletInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }	
	
	public static void main(String[] args) {
		logger.info("App controller main method");
		SpringApplication.run(App.class,args);
	}
	
	@Bean
	public PreDecorationFilter myFilter() {
		return new PreDecorationFilter();
	}
	
}
	
	


