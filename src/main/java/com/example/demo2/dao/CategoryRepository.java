package com.example.demo2.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo2.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	@Query("select c from Category c where c.parent.id is NULL")
	public List<Category> findRootCategories(Sort sort);

	@Query("select c from Category c where c.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageable);
	
	@Query("select c from Category c where c.name LIKE %?1%")
	public Page<Category> searchCategories(String keyword, Pageable pageable);
	
	public Category findByName(String name);

	public Category findByAlias(String alias);
	
	public Long countById(Integer id);
	
	@Query("Update Category c set c.enabled =?2 where c.id =?1")
	@Modifying
	public void updateEnableStatus(Integer id, boolean enabled);
}
