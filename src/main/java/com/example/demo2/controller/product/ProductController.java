package com.example.demo2.controller.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.demo2.entity.ProductImage;
import com.example.demo2.entity.ShopMeUserDetails;
import com.example.demo2.helper.product.ProductNotFoundException;
import com.example.demo2.helper.product.ProductSaveHelper;
import com.example.demo2.paging.PagingAndSortingHelper;
import com.example.demo2.paging.PagingAndSortingParam;
import com.example.demo2.service.BrandService;
import com.example.demo2.service.CategoryService;
import com.example.demo2.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("/list_products")
	public String listFirstPage(Model model) {
		return "redirect:/products/list_products/page/1?sortField=name&sortDir=asc&categoryId=0";
	}
	
	@RequestMapping("/list_products/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listProducts", modelURL = "/list_products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,Model model,
			@Param("categoryId") Integer categoryId) {

		productService.listByPage(pageNum, helper, categoryId);
//		List<Product> listProducts = page.getContent();
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

//		long startCount = (pageNum -1) * ProductService.PRODUCTS_PER_PAGE + 1;
//		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
//		if(endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
		
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if (categoryId != null ) {
			model.addAttribute("categoryId", categoryId);
		}
//		model.addAttribute("currentPage",pageNum);
//		model.addAttribute("totalPages",page.getTotalPages());	
//		model.addAttribute("startCount",startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//		model.addAttribute("sortField",sortField);
//		model.addAttribute("sortDir",sortDir);
//		model.addAttribute("reverseSortDir",reverseSortDir);
//		model.addAttribute("keyword",keyword);
//		model.addAttribute("listProducts",listProducts);
		model.addAttribute("listCategories",listCategories);

		return "products/products";
	}
	
	@RequestMapping("/new-product")
	public String newBrand(Model model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		Integer numberOfExistingExtraImages = product.getImages().size();

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "New Product");
		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
		
		return "products/form_product";
	}


	@PostMapping("/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
							@RequestParam(value = "imgupload", required = false) MultipartFile mainImageMultipart,
							@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
							@RequestParam(name = "detailIds", required = false) String[] detailIds,
							@RequestParam(name = "detailNames", required = false) String[] detailNames,
							@RequestParam(name = "detailValues", required = false) String[] detailValues,
							@RequestParam(name = "imageIds", required = false) String[] imageIds,
							@RequestParam(name = "imageNames", required = false) String[] imageNames,
							@AuthenticationPrincipal ShopMeUserDetails loggedUser
							) throws IOException {
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if (loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				
				redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");
				
				return "redirect:/products/list_products";
			}
		}
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIds, imageNames, product);
		ProductSaveHelper.setNewExtraImageName(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIds, detailNames, detailValues, product);
		
		Product saveProduct = productService.save(product);
		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, saveProduct);
		
		ProductSaveHelper.deleteExtraImagesWereRemoveOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");

		return "redirect:/products/list_products";
	}
	
	

	@RequestMapping("/edit/{id}")
	public String editProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product ( ID: " + id + " )");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/form_product";

		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products/list_products";
		}
	}
	
	@RequestMapping("/detail/{id}")
	public String viewProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product", product);
			
			return "products/product_detail_modal";
			
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products/list_products";
		}
	}

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
