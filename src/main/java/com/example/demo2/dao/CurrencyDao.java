package com.example.demo2.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo2.entity.Currency;

public interface CurrencyDao extends CrudRepository<Currency, Integer> {
	
	public List<Currency> findAllByOrderByNameAsc();
}
