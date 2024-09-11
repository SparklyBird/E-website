package com.ecommerce.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:${user.dir}/.env")
public class EWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWebsiteApplication.class, args);
	}

}