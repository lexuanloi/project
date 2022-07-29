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
import com.example.demo2.entity.ProductImage;
import com.example.demo2.helper.product.ProductNotFoundException;
import com.example.demo2.service.BrandService;
import com.example.demo2.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@RequestMapping("/list_products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}
	
	@RequestMapping("/list_products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {

		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Product> listProducts = page.getContent();

		long startCount = (pageNum -1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",page.getTotalPages());	
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword",keyword);
		model.addAttribute("listProducts",listProducts);

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
							@RequestParam("imgupload") MultipartFile mainImageMultipart,
							@RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
							@RequestParam(name = "detailIds", required = false) String[] detailIds,
							@RequestParam(name = "detailNames", required = false) String[] detailNames,
							@RequestParam(name = "detailValues", required = false) String[] detailValues,
							@RequestParam(name = "imageIds", required = false) String[] imageIds,
							@RequestParam(name = "imageNames", required = false) String[] imageNames
							) throws IOException {
		setMainImageName(mainImageMultipart, product);
		setExistingExtraImageNames(imageIds, imageNames, product);
		setNewExtraImageName(extraImageMultiparts, product);
		setProductDetails(detailIds, detailNames, detailValues, product);
		
		Product saveProduct = productService.save(product);
		saveUploadedImages(mainImageMultipart, extraImageMultiparts, saveProduct);
		
		deleteExtraImagesWereRemoveOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");

		return "redirect:/products/list_products";
	}
	
	private void deleteExtraImagesWereRemoveOnForm(Product product) {
		String extraImageDir = "product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();
				
				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Delete extra image : " + fileName);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
		if (imageIds == null || imageIds.length ==0) {
			return;
		}
		Set<ProductImage> images = new HashSet<>();
		
		for (int count = 0; count < imageIds.length; count++) {
			Integer id = Integer.parseInt(imageIds[count]);
			String name = imageNames[count];
			
			images.add(new ProductImage(id, name, product));
		}
		product.setImages(images);
	}

	private void setProductDetails(String[] detailIds, String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length ==0) {
			return;
		}
		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIds[count]);
			
			if(id !=0) {
				product.addDetail(id, name, value);
			}else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
		
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

	private void setNewExtraImageName(MultipartFile[] extraMultipartFiles, Product product) {
		if (extraMultipartFiles.length > 0) {
			for (MultipartFile multipartFile : extraMultipartFiles) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
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
