package com.example.demo2.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo2.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
	
	public Long countById(Integer id);

	public Brand findByName(String name);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);
}
