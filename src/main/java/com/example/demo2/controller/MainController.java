package com.example.demo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String viewHomePage() {
		
		return"index";
	}

	@RequestMapping("/login")
	public String viewLoginPage() {
		
		return"/auth/auth-login";
	}
}
