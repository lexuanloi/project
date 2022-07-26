package com.example.demo2.controller.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.CategoryDTO;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;
import com.example.demo2.helper.brand.BrandNotFoundException;
import com.example.demo2.helper.brand.BrandNotFoundRestException;
import com.example.demo2.service.BrandService;
import com.example.demo2.service.CategoryService;

@RestController
public class BrandRestController {
	
	@Autowired
	private BrandService service;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return service.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categoties")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException{
		List<CategoryDTO> listCategories = new ArrayList<>();
		try {
			Brand brand = service.get(brandId);
			Set<Category> categories = brand.getCategories();
			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}

			return listCategories;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
