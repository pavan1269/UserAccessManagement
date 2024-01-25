package com.useraccess.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class UserAccessManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAccessManagementApplication.class, args);
	}
	
}
