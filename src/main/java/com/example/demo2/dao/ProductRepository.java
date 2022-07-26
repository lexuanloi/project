package com.example.demo2.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo2.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	public Product findByName(String name);
	
	@Query("Update Product p set p.enabled =?2 where p.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public Long countById(Integer id);
}
