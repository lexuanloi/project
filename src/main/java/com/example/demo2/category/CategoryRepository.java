package com.example.demo2.category;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo2.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

}
