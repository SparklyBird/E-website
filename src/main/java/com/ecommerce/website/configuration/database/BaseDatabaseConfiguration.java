package com.ecommerce.website.configuration.database;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ecommerce.website.dao.base",
        entityManagerFactoryRef = "baseEntityManager",
        transactionManagerRef = "baseTransactionManager")
public class BaseDatabaseConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.base-datasource")
    DataSource baseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean baseEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("baseDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ecommerce.website.model.base")
                .persistenceUnit("base")
                .build();
    }

    @Bean
    PlatformTransactionManager baseTransactionManager(
            @Qualifier("baseEntityManager") EntityManagerFactory localEntityManagerFactory) {
        return new JpaTransactionManager(localEntityManagerFactory);
    }
}
