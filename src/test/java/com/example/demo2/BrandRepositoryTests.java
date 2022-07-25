package com.example.demo2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo2.dao.BrandRepository;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	
	@Autowired
	private BrandRepository repo;
	
	@Test
	public void testCreateBrand1() {
		Category laptops = new Category(4);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand saveBrand = repo.save(acer);
		
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2() {
		Category cellphone = new Category(17);
		Category tablet = new Category(19);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellphone);
		apple.getCategories().add(tablet);
		
		Brand saveBrand = repo.save(apple);
		
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
		

	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repo.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
}
