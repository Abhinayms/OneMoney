package com.sevya.launchpad.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.sevya.launchpad.jpa.interceptor.BaseModelInterceptor;

@Configuration
public class HibernateConfiguration extends HibernateJpaAutoConfiguration{


    public HibernateConfiguration(DataSource dataSource, JpaProperties jpaProperties,
			ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
		super(dataSource, jpaProperties, jtaTransactionManagerProvider);
		// TODO Auto-generated constructor stub
	}

	@Autowired
    BaseModelInterceptor baseModelInterceptor;


    @Override
    protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
        vendorProperties.put("hibernate.ejb.interceptor",baseModelInterceptor);
    }
}