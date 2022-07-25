package com.example.demo2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.example.demo2.dao.BrandRepository;
import com.example.demo2.dao.ProductRepository;
import com.example.demo2.entity.Brand;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;
	
	@Autowired TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 6);
		Category category = entityManager.find(Category.class, 8);

		Product product = new Product();
		product.setName("Dell inspiron 3000");
		product.setAlias("dell_inspiron_3000");
		product.setShortDescription("short description");
		product.setFullDescription("full description");
		
		product.setBrand(brand);	
		product.setCategory(category);
		
		product.setPrice(123);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreatedTime(new Date());
		product.setUpdateTdime(new Date());;
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

}
