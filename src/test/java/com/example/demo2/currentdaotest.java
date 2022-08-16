package com.example.demo2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo2.dao.BrandRepository;
import com.example.demo2.dao.CurrencyDao;
import com.example.demo2.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class currentdaotest {
	@Autowired
	private CurrencyDao dao;
	
//	@Test
//	public void testCreateCurencies() {
//		List<Currency> list = Arrays.asList(
//				new Currency("United States dollar", "$", "USD"),
//				new Currency("British pound", "£", "GBP"),
//				new Currency("Japanese yen", "¥", "JPY"),
//				new Currency("Euro", "€", "EUR"),
//				new Currency("South Korean won", "₩", "KRW"),
//				new Currency("Vietnamese đồng", "₫", "VND")
//				);
//		dao.saveAll(list);
//		
//		Iterable<Currency> iterable =dao.findAll();
//		assertThat(iterable).size().isEqualTo(6);
//	}
}
