package com.ecommerce.website.configuration.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SchemaInitializer {
    private final DataSource baseDataSource;
    private final DataSource userDataSource;

    public SchemaInitializer(@Qualifier("baseDataSource") DataSource baseDataSource,
                             @Qualifier("userDataSource") DataSource userDataSource) {
        this.baseDataSource = baseDataSource;
        this.userDataSource = userDataSource;
    }

    @PostConstruct
    public void initialize() {
//        executeSchemaScript(userDataSource, "schema-user.sql");
//        executeSchemaScript(baseDataSource, "schema-base-product-and-categories.sql");
//        executeSchemaScript(baseDataSource, "schema-base-attributes.sql");
    }

    private void executeSchemaScript(DataSource dataSource, String script) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(script));
        populator.execute(dataSource);
    }
}
