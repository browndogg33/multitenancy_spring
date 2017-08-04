package com.brandonscottbrown.multitenant.tenantdb.config;

import com.brandonscottbrown.multitenant.primarydb.domain.Tenant;
import com.brandonscottbrown.multitenant.primarydb.repository.TenantRepository;
import com.brandonscottbrown.multitenant.tenantdb.domain.Character;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager"
        basePackages = { "com.brandonscottbrown.multitenant.tenantdb.repository" }
)
public class MultiTenancyDatabaseConfiguration {

     private static final Logger logger = LoggerFactory.getLogger(MultiTenancyDatabaseConfiguration.class);
    
     @Autowired
     private JpaProperties jpaProperties;

     @Autowired
     private TenantRepository tenantRepository;
    
     @Autowired
     private MultiTenantConnectionProvider multiTenantConnectionProvider;
    
     @Autowired
     private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

     @Bean(name = "tenantEntityManagerFactory")
     public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                        @Qualifier("primaryTenantDataSource") DataSource dataSource) {
          Map<String, Object> hibernateProps = new LinkedHashMap<>();
          hibernateProps.putAll(jpaProperties.getHibernateProperties(dataSource));

          hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
          hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
          hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
          hibernateProps.put(Environment.DIALECT, H2Dialect.class.getName());
          hibernateProps.put(Environment.HBM2DDL_AUTO, "");
          hibernateProps.put(Environment.SHOW_SQL, true);

          return builder.
                  dataSource(dataSource)
                  .packages(Character.class.getPackage().getName())
                  .persistenceUnit("multiTenant")
                  .properties(hibernateProps)
                  .build();
     }

     @Bean(name = "multitenantProvider")
     public DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProvider() {
          HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();
          List<Tenant> tenants = tenantRepository.findAll();

          tenants.stream().filter(Tenant::getSeparateDatabase).forEach(tc -> dataSources.put(tc.getName(), DataSourceBuilder
                  .create()
                  .driverClassName(tc.getDriverClassName())
                  .username(tc.getUsername())
                  .password(tc.getPassword())
                  .url(tc.getUrl()).build()));

          initializeTenantDatabases(dataSources);

          return new DataSourceBasedMultiTenantConnectionProviderImpl(getDefaultTenant(tenants).getName(), dataSources);
     }

     private void initializeTenantDatabases(HashMap<String, DataSource> dataSources) {
          for (Map.Entry<String, DataSource> entry: dataSources.entrySet()){
               Resource initSchema = new ClassPathResource("scripts/" + entry.getKey() + "-tenant-schema-h2.sql");
               Resource initData = new ClassPathResource("scripts/" + entry.getKey() + "-tenant-data-h2.sql");
               DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
               DatabasePopulatorUtils.execute(databasePopulator, dataSources.get(entry.getKey()));
          }
     }

     private Tenant getDefaultTenant(List<Tenant> tenants) {
          for (Tenant t : tenants){
               if (t.getDefaultTenant()){
                    return t;
               }
          }
          throw new IllegalStateException("A default tenant dataSource must be setup for the application to startup properly.");
     }

     @Bean(name = "primaryTenantDataSource")
     @DependsOn("multitenantProvider")
     public DataSource dataSource() {
          return dataSourceBasedMultiTenantConnectionProvider().getDefaultDataSource();
     }

     @Bean(name = "tenantTransactionManager")
     public PlatformTransactionManager transactionManager(
             @Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
          return new JpaTransactionManager(entityManagerFactory);
     }
}
