package com.example.demo2.controller.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.User;
import com.example.demo2.helper.brand.BrandNotFoundException;
import com.example.demo2.helper.category.CategoryNotFoundException;
import com.example.demo2.paging.PagingAndSortingHelper;
import com.example.demo2.paging.PagingAndSortingParam;
import com.example.demo2.service.BrandService;
import com.example.demo2.service.CategoryService;
import com.example.demo2.service.UserService;

@Controller
@RequestMapping("/brands")
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("/list_brands")
	public String listAll() {
		return "redirect:/brands/list_brands/page/1?sortField=name&sortDir=asc";
	}
	
	@RequestMapping("/list_brands/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listBrands", modelURL = "/list_brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {

		brandService.listByPage(pageNum, helper);

		return "brands/brands";
	}
	
	@RequestMapping("/new-brand")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "New Brand");
		return "/brands/form_brand";
	}

	@PostMapping("/save")
	public String saveBrand(Brand brand, RedirectAttributes redirectAttributes,
			@RequestParam("imgupload") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand saveBrand = brandService.save(brand);
			String uploadDir = "brands-logos/"+ saveBrand.getId();
			
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			brandService.save(brand);
		}
		
		redirectAttributes.addFlashAttribute("message", "Lưu thương hiệu thành công");

		return "redirect:/brands/list_brands";
	}

	@RequestMapping("/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);			
			model.addAttribute("pageTitle", "Edit Brand ( ID: " + id + " )");

			return "/brands/form_brand";

		} catch (BrandNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands/list_brands";
		}
	}

	@RequestMapping("/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "brands-logos/" + id;
			FileUploadUtil.removeDir(brandDir);
			
			redirectAttributes.addFlashAttribute("message", "Xoá thương hiệu id " + id + " thành công!");
			return "redirect:/brands/list_brands";

		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/brands/list_brands";
	}
}
