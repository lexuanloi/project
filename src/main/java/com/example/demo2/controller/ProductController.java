package com.example.demo2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo2.entity.Product;
import com.example.demo2.service.ProductService;


@Controller
public class ProductController {
	@Autowired
	private ProductService service;
	

	
	@RequestMapping("/new")
	public String newProduct(Model model) {
		Product pro = new Product();
		model.addAttribute("product",pro);
		return "new_product";
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product pro) {
		service.save(pro);
		return "redirect:/";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable(name = "id") long id) {
		ModelAndView mav = new ModelAndView("edit_product");
		Product pro = service.get(id);
		mav.addObject("product",pro);
		return mav;
	}
	
	@RequestMapping("/view/{id}")
	public ModelAndView view(@PathVariable(name = "id") long id) {
		ModelAndView mav = new ModelAndView("view_product");
		Product pro = service.get(id);
		mav.addObject("product",pro);
		return mav;
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") long id) {
		service.delete(id);
		return "redirect:/";
	}
	
//	@RequestMapping("/search")
//	public ModelAndView search(@RequestParam String keyword) {
//		ModelAndView mav = new ModelAndView("search_product");
//		List<Product> result = service.search(keyword);
//		mav.addObject("result",result);
//		return mav;
//	}
}


