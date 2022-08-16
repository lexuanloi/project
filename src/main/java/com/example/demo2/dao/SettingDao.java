package com.example.demo2.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo2.entity.Setting;
import com.example.demo2.entity.SettingCategory;

public interface SettingDao extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);
}
