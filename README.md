# E-website

## Technology stack
- HTML/CSS/JS
- Maven
- SpringBoot framework (with Thymeleaf)
- Sqlite database

<br>

Before Running application you need to uncomment logic in /configuration/database/SchemaInitializer.java to run schema for data insertion in database

    executeSchemaScript(userDataSource, "schema-user.sql");
    executeSchemaScript(baseDataSource, "schema-base-product-and-categories.sql");
    executeSchemaScript(baseDataSource, "schema-base-attributes.sql");

Then you should be able to access it under http://localhost:8080/ and see homepage

For login you can use admin "admin@gmail.com" and regular "user@gmail.com" user, both have the password "nimda"

Working on this project:
- Zigmunds, Nikolajs, Jekatarina, Amnander 
