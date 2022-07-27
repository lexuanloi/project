package com.example.demo2.controller.product;

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

import com.example.demo2.common.FileUploadUtil;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Product;
import com.example.demo2.helper.product.ProductNotFoundException;
import com.example.demo2.service.BrandService;
import com.example.demo2.service.ProductService;

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
	public String saveProduct(Product product, RedirectAttributes redirectAttributes, @RequestParam("imgupload") MultipartFile mainImageMultipart,
							@RequestParam("extraImage") MultipartFile[] extraImageMultiparts) throws IOException {
		setMainImageName(mainImageMultipart, product);
		setExtraImageName(extraImageMultiparts, product);
		
		Product saveProduct = productService.save(product);
		saveUploadedImages(mainImageMultipart, extraImageMultiparts, saveProduct);
		
		redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");

		return "redirect:/products/list_products";
	}
	
	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product saveProduct) throws IOException {
		
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "product-images/"+ saveProduct.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		if (extraImageMultiparts.length > 0) {
			String uploadDir = "product-images/"+ saveProduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

			}
		}
	}

	private void setExtraImageName(MultipartFile[] extraMultipartFiles, Product product) {
		if (extraMultipartFiles.length > 0) {
			for (MultipartFile multipartFile : extraMultipartFiles) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					product.addExtraImage(fileName);
				}
			}
			
		}
		
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
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
			String productExtraImagesDir = "product-images/"+ id + "/extras";
			String productImagesDir = "product-images/"+ id;
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

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
