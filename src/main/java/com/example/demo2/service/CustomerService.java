package com.example.demo2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo2.dao.CountryDao;
import com.example.demo2.dao.CustomerDao;
import com.example.demo2.dao.StateDao;
import com.example.demo2.entity.Country;

@Service
public class CustomerService {
	
	@Autowired private CountryDao countryDao;
	@Autowired private CustomerDao customerDao;
	
	public List<Country> listAllCountries(){
		return countryDao.findAllByOrderByNameAsc();
	}
}
