package com.example.demo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//(exclude = {SecurityAutoConfiguration.class })
@ComponentScan({"com.example.demo2.entity","com.example.demo2.dao"})
@ComponentScan("com.example.demo2")
public class Demo2Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Demo2Application.class, args);
	}
}

