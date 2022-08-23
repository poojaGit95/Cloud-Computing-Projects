package com.cloudassignment.program5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Program5ApplicationStarter extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Program5ApplicationStarter.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Program5ApplicationStarter.class, args);
	}
}