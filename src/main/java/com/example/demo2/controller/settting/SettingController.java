package com.example.demo2.controller.settting;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo2.dao.CurrencyDao;
import com.example.demo2.entity.Currency;
import com.example.demo2.entity.Setting;
import com.example.demo2.service.SettingService;

@Controller
public class SettingController {

	@Autowired private SettingService service;
	
	@Autowired private CurrencyDao currencyDao;
	
	@RequestMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyDao.findAllByOrderByNameAsc();
		
		model.addAttribute("listCurrencies", listCurrencies);
		
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		return "settings/settings";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("imgupload") MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		return "redirect:/settings";
	}
}
