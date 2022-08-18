package com.example.demo2.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo2.entity.Country;


public interface CountryDao extends CrudRepository<Country, Integer>{

	public List<Country> findAllByOrderByNameAsc();
}
