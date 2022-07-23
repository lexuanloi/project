package com.example.demo2.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo2.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
	
	public Long countById(Integer id);

}
