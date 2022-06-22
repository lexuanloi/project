package com.example.demo2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo2.dao.ProductRepository;
import com.example.demo2.entity.Product;

import java.util.List;

@Service
public class ProductService {
	@Autowired
	private ProductRepository repo;
	
	public List<Product> listAll() {
		return repo.findAll();
	}
	
	public void save(Product pro) {
		repo.save(pro);
	}
	
	public Product get(long id) {
		return repo.findById(id).get();
	}
	
	public void delete(long id) {
		repo.deleteById(id);
	}
//	
//	public List<Product> search(String keyword){
//		return repo.search(keyword);
//	}
}
