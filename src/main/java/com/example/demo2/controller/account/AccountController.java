package com.example.demo2.controller.account;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo2.entity.ShopMeUserDetails;
import com.example.demo2.entity.User;
import com.example.demo2.service.UserDetailService;
import com.example.demo2.service.UserService;
import com.example.demo2.util.FileUploadUtil;

@Controller
public class AccountController extends UserDetailService{
	
	@Autowired
	private UserService service;
	
	@RequestMapping("/account")
	public String viewDetais(@AuthenticationPrincipal ShopMeUserDetails loggedUser,
			Model model) {
		String email =  loggedUser.getUsername();
		User user =  service.getByEmail(email);
		model.addAttribute("user", user);
		
		return "/users/form_account";
	}
	
	@PostMapping("/account/update")
	public String saveDetail(User user,RedirectAttributes redirectAttributes,@AuthenticationPrincipal ShopMeUserDetails loggedUser, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhoto(fileName);
			User saveUser = service.updateAccount(user);
			
			String uploadDir = "users/" + saveUser.getId();
			
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if (user.getPhoto().isEmpty()) user.setPhoto(null);
				service.updateAccount(user);	
		}	
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		redirectAttributes.addFlashAttribute("message", "Update user thành công");
		
		return "redirect:/account";
	}
}
