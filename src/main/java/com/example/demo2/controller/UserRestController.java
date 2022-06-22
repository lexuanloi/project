package com.example.demo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	private UserService service;
	
	
}
