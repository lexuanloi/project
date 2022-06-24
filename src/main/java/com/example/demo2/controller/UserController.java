package com.example.demo2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import com.example.demo2.service.UserNotFoundException;
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
	
	@RequestMapping("/new-user" )
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnable(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle", "New User");
		return "new-user";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);
		service.save(user);
		
		redirectAttributes.addFlashAttribute("message", "Add New User Successfully");
		
		return "redirect:/users";
	}
	
	@RequestMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("listRoles",listRoles);
			model.addAttribute("pageTitle", "Edit User ( ID: "+id+" )");
			
			return "/new-user";

		} catch (UserNotFoundException ex) {
				
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
}
