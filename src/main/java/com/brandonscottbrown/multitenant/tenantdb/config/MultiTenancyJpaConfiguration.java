package com.brandonscottbrown.multitenant.tenantdb.config;

import com.brandonscottbrown.multitenant.tenantdb.domain.Character;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        basePackages = { "com.brandonscottbrown.multitenant.tenantdb.repository" }
)
public class MultiTenancyJpaConfiguration {

     private static Logger logger = LoggerFactory.getLogger(MultiTenancyJpaConfiguration.class);

     @Autowired
     @Qualifier("primaryTenantDataSource")
     private DataSource dataSource;
    
     @Autowired
     private JpaProperties jpaProperties;
    
     @Autowired
     private MultiTenantConnectionProvider multiTenantConnectionProvider;
    
     @Autowired
     private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

     @Bean(name = "tenantEntityManagerFactory")
     public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
          Map<String, Object> hibernateProps = new LinkedHashMap<>();
          hibernateProps.putAll(jpaProperties.getHibernateProperties(dataSource));

          hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
          hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
          hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
          hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
          hibernateProps.put(Environment.HBM2DDL_AUTO, "");
          hibernateProps.put(Environment.SHOW_SQL, true);

          logger.info("Character package scanned = {}", Character.class.getPackage().getName());
          return builder.
                  dataSource(dataSource)
                  .packages(Character.class.getPackage().getName())
                  .persistenceUnit("multiTenant")
                  .properties(hibernateProps)
                  .jta(false)
                  .build();
     }

     @Bean(name = "tenantTransactionManager")
     public PlatformTransactionManager transactionManager(
             @Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
          return new JpaTransactionManager(entityManagerFactory);
     }
}