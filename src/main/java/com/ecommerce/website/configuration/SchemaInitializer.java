package com.ecommerce.website.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SchemaInitializer {
    private final DataSource baseDataSource;

    public SchemaInitializer(@Qualifier("baseDataSource") DataSource baseDataSource) {
        this.baseDataSource = baseDataSource;
    }

    @PostConstruct
    public void initialize() {
        executeSchemaScript(baseDataSource, "schema-base.sql");
    }

    private void executeSchemaScript(DataSource dataSource, String script) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(script));
        populator.execute(dataSource);
    }
}
