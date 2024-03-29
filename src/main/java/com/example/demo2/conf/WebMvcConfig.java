package com.example.demo2.conf;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo2.paging.PagingAndSortingArgumentResolver;
import com.example.demo2.paging.PagingAndSortingHelper;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		exposeDirectory("fileupload/users", registry);
		exposeDirectory("fileupload/category-images", registry);
		exposeDirectory("fileupload/brands-logos", registry);
		exposeDirectory("fileupload/product-images", registry);
		exposeDirectory("fileupload/site-logo", registry);

	}
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern + "/**";

		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");

	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		// TODO Auto-generated method stub
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
}
