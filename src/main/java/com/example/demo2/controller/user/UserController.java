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

import com.example.demo2.common.FileUploadUtil;
import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import com.example.demo2.helper.user.UserCsvExporter;
import com.example.demo2.helper.user.UserExcelExporter;
import com.example.demo2.helper.user.UserNotFoundException;
import com.example.demo2.helper.user.UserPdfExporter;
import com.example.demo2.paging.PagingAndSortingHelper;
import com.example.demo2.paging.PagingAndSortingParam;
import com.example.demo2.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@RequestMapping("/list_users")
	public String listFirstPage() {
//		return listByPage(helper ,1 , model, "id", "asc", null);
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	}
	
	@RequestMapping("/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listUsers", modelURL = "/list_users") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {

		service.listByPage(pageNum, helper);
		
		return"/users/users";
	}
	
	@RequestMapping("/new-user" )
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle", "New User");
		return "/users/form-user";
	}
	
	@PostMapping("/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhoto(fileName);
			User saveUser = service.save(user);
			
			String uploadDir = "fileupload/users/" + saveUser.getId();
			
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
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@RequestMapping("/edit/{id}")
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
			return "redirect:/users/list_users";
		}
	}

	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "Xoá user id " +id+ " thành công!");
			return "redirect:/users/list_users";

		} catch (UserNotFoundException ex) {		
			redirectAttributes.addFlashAttribute("message", ex.getMessage());	
		}
		return "redirect:/users/list_users";
	}
	
	@RequestMapping("/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
					@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enable" : "disable";
		String message = "User id " + id +" đã được đổi sang trạng thái " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users/list_users";
	}
	
	@RequestMapping("/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);		
	}
	
	@RequestMapping("/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);		
	}
	@RequestMapping("/export/pdf")
	public void exportPdf(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);		
	}
}
