# E-website

## Technology stack
- Java
- HTML/CSS/JS
- Maven
- SpringBoot framework (with Thymeleaf)
- SQLite database
- Junit 5
- Spring security
- Selenium
- GitHub
- Trello

<br>

Before Running application you need to uncomment logic in /configuration/database/SchemaInitializer.java 
to run schema for data insertion in database and then comment them out for second time (because of insertion conflicts)

    executeSchemaScript(userDataSource, "schema-user.sql");
    executeSchemaScript(baseDataSource, "schema-base-product-and-categories.sql");
    executeSchemaScript(baseDataSource, "schema-base-attributes.sql");

For images you need to download https://drive.google.com/file/d/1sWDwwdvQc2grtsvA9qvfC1Ddn3kmlBZF/view and put them in static folder and it would look like static/images/products/...

Then you should be able to access it under http://localhost:8080/ and see homepage

For login you can use admin "admin@gmail.com" and regular "user@gmail.com" user, both have the password "nimda"

Note:
If you want to be able to pay with stripe and see your results, add your stripe public and secret key.

People Working on this project:
- Zigmunds, Nikolajs, Jekatarina, Amnander 
