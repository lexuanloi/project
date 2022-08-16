package com.example.demo2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo2.dao.GeneralSettingBag;
import com.example.demo2.dao.SettingDao;
import com.example.demo2.entity.Setting;
import com.example.demo2.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired private SettingDao dao;
	
	public List<Setting> listAllSettings(){
		return (List<Setting>) dao.findAll();
	}
	
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<>();
		List<Setting> generalSettings = dao.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = dao.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(settings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		dao.saveAll(settings);
	}
}
