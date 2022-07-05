package com.example.demo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@SpringBootApplication
//(exclude = {SecurityAutoConfiguration.class })
@ComponentScan({"com.example.demo2.entity","com.example.demo2.dao"})
@ComponentScan("com.example.demo2")
public class Demo2Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Demo2Application.class, args);
	}
}

