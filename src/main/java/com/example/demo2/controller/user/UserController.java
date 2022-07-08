package com.example.demo2.controller.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
//@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@RequestMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}
	
	@RequestMapping("/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {

		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();
		

		long startCount = (pageNum -1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",page.getTotalPages());	
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("listUsers",listUsers);
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword",keyword);

		
		return"/users/users";
	}
	
	@RequestMapping("/new-user" )
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnable(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle", "New User");
		return "/users/form-user";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
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
		redirectAttributes.addFlashAttribute("message", "Lưu user thành công");
		
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@RequestMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("listRoles",listRoles);
			model.addAttribute("pageTitle", "Edit User ( ID: "+id+" )");
			
			return "/users/form-user";

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
	
	@RequestMapping("/users/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);		
	}
	
	@RequestMapping("/users/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);		
	}
	@RequestMapping("/users/export/pdf")
	public void exportPdf(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);		
	}
}
