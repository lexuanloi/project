package com.example.demo2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo2.entity.Product;
import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import com.example.demo2.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	
	@RequestMapping("/")
	public String viewHome() {
		
		return"index";
	}
	
	@RequestMapping("/users")
	public String ListAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers",listUsers);
		return "/projects/projects-users";
	}
	
	@RequestMapping("/new-user")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnable(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		return "new-user";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);
		service.save(user);
		redirectAttributes.addFlashAttribute("mess", "Add New User Successfully");
		return "redirect:/users";
	}
	
}
