package com.sevya.launchpad.config;

import java.beans.PropertyVetoException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class ConnectionPoolingConfiguration {

    @Value("${c3p0.max_size}")
    private int maxSize;
 
    @Value("${c3p0.min_size}")
    private int minSize;
 
    @Value("${c3p0.acquire_increment}")
    private int acquireIncrement;
 
    //@Value("${c3p0.idle_test_period}")
    //private int idleTestPeriod;
 
    //@Value("${c3p0.max_statements}")
    //private int maxStatements;
 
    @Value("${c3p0.max_idle_time}")
    private int maxIdleTime;
 
    @Value("${spring.datasource.url}")
    private String url;
 
    @Value("${spring.datasource.username}")
    private String username;
 
    @Value("${spring.datasource.password}")
    private String password;
 
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    
    @Value("${c3p0.unreturned_connection_timeout}")
    private int unreturnedConnectionTimeout;
 
    @Bean
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setMaxPoolSize(maxSize);
        dataSource.setMinPoolSize(minSize);
        dataSource.setAcquireIncrement(acquireIncrement);
        //dataSource.setIdleConnectionTestPeriod(idleTestPeriod);
        //dataSource.setMaxStatements(maxStatements);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setJdbcUrl(url);
        dataSource.setPassword(password);
        dataSource.setUser(username);
        dataSource.setDriverClass(driverClassName);
        dataSource.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
        return dataSource;
    }
	
	
}
