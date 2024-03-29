package com.example.demo2.controller.category;

import java.io.IOException;
import java.util.List;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo2.common.FileUploadUtil;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.CategoryPageInfo;
import com.example.demo2.helper.category.CategoryCsvExporter;
import com.example.demo2.helper.category.CategoryExcelExporter;
import com.example.demo2.helper.category.CategoryNotFoundException;
import com.example.demo2.helper.category.CategoryPdfExporter;
import com.example.demo2.service.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@RequestMapping("/list_categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}
	
	@RequestMapping("/list_categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum -1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if(endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);

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
			@RequestParam("imgupload") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category saveCategory = service.save(category);
			String uploadDir = "fileupload/category-images/"+ saveCategory.getId();
			
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
	public String deleteCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String categoryDir = "fileupload/category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			
			redirectAttributes.addFlashAttribute("message", "Xoá danh mục id " + id + " thành công!");
			return "redirect:/categories/list_categories";

		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/categories/list_categories";
	}


	@RequestMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "Danh mục id " + id + " đã được đổi sang trạng thái " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/categories/list_categories";
	}
	
	@RequestMapping("/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);		
	}

	@RequestMapping("/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		
		
		CategoryExcelExporter exporter = new CategoryExcelExporter();
		exporter.export(listCategories, response);		
	}
	
	@RequestMapping("/export/pdf")
	public void exportPdf(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		
		CategoryPdfExporter exporter = new CategoryPdfExporter();
		exporter.export(listCategories, response);		
	}
}
