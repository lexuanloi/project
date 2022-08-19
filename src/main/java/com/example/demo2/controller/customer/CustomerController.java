package com.example.demo2.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.demo2.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired private CustomerService service;
	
}
