package com.example.demo2.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo2.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	@Query("select c from Category c where c.parent.id is NULL")
	public List<Category> findRootCategories();
	
	public Long countById(Integer id);
	
	@Query("Update Category cat set cat.enabled =?2 where cat.id =?1")
	@Modifying
	public void updateCategoryEnableStatus(Integer id, boolean enabled);
}
