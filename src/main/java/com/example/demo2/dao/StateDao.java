package com.example.demo2.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo2.entity.Country;
import com.example.demo2.entity.State;

public interface StateDao extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
