package com.example.demo2.helper.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo2.common.FileUploadUtil;
import com.example.demo2.entity.Product;
import com.example.demo2.entity.ProductImage;

public class ProductSaveHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	
	public static void deleteExtraImagesWereRemoveOnForm(Product product) {
		String extraImageDir = "fileupload/product-images/" + product.getId() + "/extras";
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

	public static void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
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

	public static void setProductDetails(String[] detailIds, String[] detailNames, String[] detailValues, Product product) {
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

	public static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product saveProduct) throws IOException {
		
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "fileupload/product-images/"+ saveProduct.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		if (extraImageMultiparts.length > 0) {
			String uploadDir = "fileupload/product-images/"+ saveProduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
	}

	public static void setNewExtraImageName(MultipartFile[] extraMultipartFiles, Product product) {
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

	public static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
}
