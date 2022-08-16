package com.example.demo2.dao;

import java.util.List;

import com.example.demo2.entity.Setting;
import com.example.demo2.entity.SettingBag;

public class GeneralSettingBag extends SettingBag{

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	
	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}
	
	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
}
