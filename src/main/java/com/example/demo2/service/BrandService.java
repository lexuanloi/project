package com.example.demo2.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo2.dao.BrandRepository;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.User;
import com.example.demo2.helper.brand.BrandNotFoundException;
import com.example.demo2.paging.PagingAndSortingHelper;

@Service
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 5 ;

	@Autowired
	private BrandRepository repo;

	public List<Brand> listAll(){
		return (List<Brand>) repo.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
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
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		
		Brand brandByName = repo.findByName(name);
		
		if (isCreatingNew) {
			if (brandByName != null) return "Duplicate";
		}else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "OK";
	}
}
