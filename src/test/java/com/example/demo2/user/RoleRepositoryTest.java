package com.example.demo2.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo2.dao.RoleReponsitory;
import com.example.demo2.entity.Role;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false) 
public class RoleRepositoryTest {
	
	@Autowired
	private RoleReponsitory repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin","manage everything");
		Role saveRole =repo.save(roleAdmin);
		assertThat(saveRole.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson","manage product price"
				+"customer,shipping,order and sales and report");
		Role roleEditor = new Role("Salesperson1","manage categories,brands"
				+"product ,articles and menus");
		Role roleShipper = new Role("Salesperson2","manage product price"
				+"and update order status");
		Role roleAssistant = new Role("Salesperson3","manage question and reviews");
		
		repo.saveAll(List.of(roleSalesperson,roleEditor,roleShipper,roleAssistant));
	}
}
