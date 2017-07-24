package com.brandonscottbrown.multitenant.tenantdb.config;

import com.brandonscottbrown.multitenant.primarydb.domain.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(MultitenancyConfigurationProperties.class)
public class MultitenancyConfiguration {
    
    @Autowired
    private MultitenancyConfigurationProperties multitenancyProperties;

    @Bean(name = "multitenantProvider")
    public DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProvider() {
        HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();
        
        multitenancyProperties.getTenants().stream().filter(Tenant::getSeparateDatabase).forEach(tc -> dataSources.put(tc.getName(), DataSourceBuilder
                .create()
                .driverClassName(tc.getDriverClassName())
                .username(tc.getUsername())
                .password(tc.getPassword())
                .url(tc.getUrl()).build()));
        
        return new DataSourceBasedMultiTenantConnectionProviderImpl(multitenancyProperties.getDefaultTenant().getName(), dataSources);
    }

    @Bean(name = "primaryTenantDataSource")
    @DependsOn("multitenantProvider")
    public DataSource dataSource() {
        return dataSourceBasedMultiTenantConnectionProvider().getDefaultDataSource();
    }

}
