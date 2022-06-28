package com.example.demo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	private UserService service;
	
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id ,@Param("email") String email) {
		return service.isEmailUnique(id,email) ? "ok" : "Duplicated";
	}

}
