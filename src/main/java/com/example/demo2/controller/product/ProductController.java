package com.example.demo2.controller.product;

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
import com.example.demo2.entity.Product;
import com.example.demo2.entity.User;
import com.example.demo2.helper.brand.BrandNotFoundException;
import com.example.demo2.helper.category.CategoryNotFoundException;
import com.example.demo2.helper.product.ProductNotFoundException;
import com.example.demo2.service.BrandService;
import com.example.demo2.service.CategoryService;
import com.example.demo2.service.ProductService;
import com.example.demo2.service.UserService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@RequestMapping("/list_products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
	
	@RequestMapping("/new-product")
	public String newBrand(Model model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "New Product");
		
		return "products/form_product";
	}

	@PostMapping("/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
		
		productService.save(product);
		redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");

		return "redirect:/products/list_products";
	}
//
//	@RequestMapping("/edit/{id}")
//	public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
//		try {
//			Brand brand = brandService.get(id);
//			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//
//			model.addAttribute("brand", brand);
//			model.addAttribute("listCategories", listCategories);			
//			model.addAttribute("pageTitle", "Edit Brand ( ID: " + id + " )");
//
//			return "/brands/form_brand";
//
//		} catch (BrandNotFoundException ex) {
//
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//			return "redirect:/brands/list_brands";
//		}
//	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			
			redirectAttributes.addFlashAttribute("message", "Delete product id " + id + " successful!");
			return "redirect:/products/list_products";

		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/products/list_products";
	}
	
	@RequestMapping("/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "Product id " + id + " đã được đổi sang trạng thái " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/products/list_products";
	}
}
