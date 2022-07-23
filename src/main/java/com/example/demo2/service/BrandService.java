package com.example.demo2.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo2.dao.BrandRepository;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;

@Service
public class BrandService {
	@Autowired
	private BrandRepository repo;

	public List<Brand> listAll(){
		return (List<Brand>) repo.findAll();
	}

	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Không tìm thấy thương hiệu nào với id : "+id);
		}
		
	}
	
	public void delete(Integer id) throws BrandNotFoundException{
		Long countById = repo.countById(id);
		if(countById == null || countById == 0) {
			throw new BrandNotFoundException("Không tìm thấy thương hiệu nào với id : "+id);
		}
		repo.deleteById(id);
	}
}
