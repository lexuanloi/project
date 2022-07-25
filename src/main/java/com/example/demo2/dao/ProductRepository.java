package com.example.demo2.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo2.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

}
