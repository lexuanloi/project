package com.example.demo2.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "users";
		Path userPhotoDir = Paths.get(dirName);
		String userPhotoPath = userPhotoDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/"+ dirName + "/**").addResourceLocations("file:/" + userPhotoPath + "/");

		
		String categoryImagesName = "category-images";
		Path categoryImagesDir = Paths.get(categoryImagesName);
		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/"+ categoryImagesName + "/**").addResourceLocations("file:/" + categoryImagesPath + "/");
	}
}
