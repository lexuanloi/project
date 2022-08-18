package com.example.demo2.controller.settting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dao.CountryDao;
import com.example.demo2.entity.Country;

@RestController
public class CountryRestController {
	
	@Autowired private CountryDao countryDao;
	
	@RequestMapping("/countries/list")
	public List<Country> listAll(){
		return countryDao.findAllByOrderByNameAsc();
	}
	
	@PostMapping("/countries/save")
	public String save(@RequestBody Country country) {
		Country savedCountry = countryDao.save(country);
		return String.valueOf(savedCountry.getId());
	}
	
	@DeleteMapping("/countries/delete/{id}")
	public void delete(@PathVariable("id") Integer id){
		countryDao.deleteById(id);
	}
}
