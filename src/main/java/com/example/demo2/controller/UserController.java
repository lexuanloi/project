package com.example.demo2.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import com.example.demo2.service.UserNotFoundException;
import com.example.demo2.service.UserService;
import com.example.demo2.util.FileUploadUtil;

@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	
	@RequestMapping("/")
	public String viewHome() {
		
		return"index";
	}
	
	@RequestMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model);
	}
	
	@RequestMapping("/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<User> page = service.listByPage(pageNum);
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE +1 ;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",page.getTotalPages());	
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers",listUsers);
		
		return"/projects/projects-users";
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
	public String saveUser(User user,RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
//		System.out.println(multipartFile.getOriginalFilename());
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhoto(fileName);
			User saveUser = service.save(user);
			
			String uploadDir = "users/" + saveUser.getId();
			
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if (user.getPhoto().isEmpty()) user.setPhoto(null);
				service.save(user);	
		}	
		redirectAttributes.addFlashAttribute("message", "Thêm user mới thành công");
		
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

	@RequestMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
				redirectAttributes.addFlashAttribute("message", "Xoá user id " +id+ " thành công!");
			return "redirect:/users";

		} catch (UserNotFoundException ex) {		
			redirectAttributes.addFlashAttribute("message", ex.getMessage());	
		}
		return "redirect:/users";
	}
	
	@RequestMapping("/users/{id}/enable/{status}")
	public String updateUserEnableStatus(@PathVariable("id") Integer id,
					@PathVariable("status") boolean enable, RedirectAttributes redirectAttributes) {
		service.updateUserEnableStatus(id, enable);
		String status = enable ? "enable" : "disable";
		String message = "User id " + id +" đã được đổi sang trạng thái " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
}
