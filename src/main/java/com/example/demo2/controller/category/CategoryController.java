package com.example.demo2.controller.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo2.entity.Category;
import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import com.example.demo2.service.CategoryNotFoundException;
import com.example.demo2.service.CategoryService;
import com.example.demo2.service.UserNotFoundException;
import com.example.demo2.util.FileUploadUtil;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@RequestMapping("/list_categories")
	public String listAll(Model model) {
		List<Category> listCategories = service.listAll();
		model.addAttribute("listCategories", listCategories);

		return "/categories/categories";
	}

	@RequestMapping("/new-category")
	public String newCategory(Model model) {
		List<Category> listCategories = service.listCategoriesUsedInForm();

		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "New Category");
		return "/categories/form_category";
	}

	@PostMapping("/save")
	public String saveCategory(Category category, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category saveCategory = service.save(category);
			String uploadDir = "category-images/"+ saveCategory.getId();
			
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			service.save(category);
		}
		
		redirectAttributes.addFlashAttribute("message", "Lưu danh mục thành công");

		return "redirect:/categories/list_categories";
	}

	@RequestMapping("/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Category category = service.get(id);
			List<Category> listCategories = service.listCategoriesUsedInForm();

			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);			
			model.addAttribute("pageTitle", "Edit Category ( ID: " + id + " )");

			return "/categories/form_category";

		} catch (CategoryNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories/list_categories";
		}
	}

	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "Xoá danh mục id " + id + " thành công!");
			return "redirect:/categories/list_categories";

		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/categories/list_categories";
	}


	@RequestMapping("/{id}/enabled/{status}")
	public String updateCategoryEnableStatus(@PathVariable("id") Integer id,

			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateCategoryEnableStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "Danh mục id " + id + " đã được đổi sang trạng thái " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/categories/list_categories";
	}

}
