package com.example.demo2.controller.settting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo2.common.FileUploadUtil;
import com.example.demo2.dao.CurrencyDao;
import com.example.demo2.dao.GeneralSettingBag;
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
	public String saveGeneralSettings(@RequestParam("imgupload") MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		GeneralSettingBag settingBag = service.getGeneralSettings();
		
		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);
		
		updateSettingValuesFromForm(request, settingBag.list());
		
		redirectAttributes.addFlashAttribute("message", "General setting have been saved.");
		
		return "redirect:/settings";
	}

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "site-logo/" + fileName;
			settingBag.updateSiteLogo(value);
			
			String uploadDir = "site-logo/";
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyDao.findById(currencyId);
		
		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		
		service.saveAll(listSettings);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailServerSetting = service.getMailServerSettings();
		updateSettingValuesFromForm(request, mailServerSetting);
		
		redirectAttributes.addFlashAttribute("message", "Mail server setting have been saved.");
		
		return "redirect:/settings";

	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailTemplateSetting = service.getMailTemplateSettings();
		updateSettingValuesFromForm(request, mailTemplateSetting);
		
		redirectAttributes.addFlashAttribute("message", "Mail template setting have been saved.");
		
		return "redirect:/settings";
		
	}
	
}
