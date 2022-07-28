package com.example.demo2.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo2.dao.BrandRepository;
import com.example.demo2.dao.ProductRepository;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.Product;
import com.example.demo2.entity.User;
import com.example.demo2.helper.product.ProductNotFoundException;

@Service
@Transactional
public class ProductService {
	
	public static final int PRODUCTS_PER_PAGE = 2 ;

	@Autowired
	private ProductRepository repo;

	public List<Product> listAll(){
		return (List<Product>) repo.findAll();
	}
	
//	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
//		Sort sort = Sort.by(sortField);
//		
//		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//		
//		Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE ,sort);
//		
//		if (keyword != null) {
//			return repo.findAll(keyword, pageable);
//		}
//		
//		return repo.findAll(pageable);
//	}

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		}else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		
		product.setUpdateTime(new Date());;
		
		return repo.save(product);
	}
	
//	public Brand get(Integer id) throws BrandNotFoundException {
//		try {
//			return repo.findById(id).get();
//		} catch (NoSuchElementException ex) {
//			throw new BrandNotFoundException("Không tìm thấy thương hiệu nào với id : "+id);
//		}
//		
//	}
	
	public void delete(Integer id) throws ProductNotFoundException{
		Long countById = repo.countById(id);
		if(countById == null || countById == 0) {
			throw new ProductNotFoundException("Không tìm thấy product nào với id : "+id);
		}
		repo.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		
		Product productByName = repo.findByName(name);
		
		if (isCreatingNew) {
			if (productByName != null) return "Duplicate";
		}else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "OK";
	}

	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("could not find nay product with id " + id);
		}
	}
}
