package com.example.demo2.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo2.conf.ShopMeUserDetails;
import com.example.demo2.entity.User;
import com.example.demo2.service.UserDetailService;
import com.example.demo2.service.UserService;

public class AccountController extends UserDetailService{
	
	@Autowired
	private UserService service;
	
	@RequestMapping("/account")
	public String viewDetais(@AuthenticationPrincipal ShopMeUserDetails loggedUser,
			Model model) {
		String email =  loggedUser.getUsername();
		User user =  service.getByEmail(email);
		model.addAttribute("user", user);
		
		return "account_form";
	}
}
