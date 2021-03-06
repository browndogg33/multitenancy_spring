package com.brandonscottbrown.multitenant.primarydb.config;

import com.brandonscottbrown.multitenant.primarydb.domain.Tenant;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = { "com.brandonscottbrown.multitenant.primarydb.repository" }
)
public class PrimaryDatabaseConfiguration {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.primary.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("primaryDataSource") DataSource dataSource) {
        Map<String, Object> hibernateProps = new HashMap<>();
        hibernateProps.put(Environment.DIALECT, H2Dialect.class.getName());
        hibernateProps.put(Environment.HBM2DDL_AUTO, "create-drop");
        hibernateProps.put(Environment.SHOW_SQL, true);
        hibernateProps.put(Environment.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
        hibernateProps.put(Environment.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());

        return builder
                .dataSource(dataSource)
                .packages(Tenant.class.getPackage().getName())
                .persistenceUnit("primary")
                .properties(hibernateProps)
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
