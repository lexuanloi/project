package com.example.demo2.user;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.example.demo2.dao.UserRepository;
import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin =entityManager.find(Role.class, 1);
		User userLoi = new User("loi@gmail.com","loi","le","loi123");
		userLoi.addRole(roleAdmin);
		
		User saveUser = repo.save(userLoi);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRole() {
		User userNghia = new User("nghia11@gmail.com","nghia","nguyen","nghia123");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userNghia.addRole(roleEditor);
		userNghia.addRole(roleAssistant);
		
		User saveUser = repo.save(userNghia);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUers() {
		Iterable<User> listUser = repo.findAll();
		listUser.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserId() {
		User userLoi = repo.findById(1).get();
		System.out.println(userLoi);
		assertThat(userLoi).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userLoi = repo.findById(1).get();
		userLoi.setEnable(true);
		userLoi.setEmail("loi11111@gmail.com");
		repo.save(userLoi);
		
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userLoi = repo.findById(3).get();
		Role roleEdit = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userLoi.getRoles().remove(userLoi);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
;	}
}
	