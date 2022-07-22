package com.example.demo2.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo2.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	@Query("Select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email") String email);

	public Long countById(Integer id);
	
//	@Query("Select u from User u where u.firstName like %?1% or u.lastName like %?1% or u.email like %?1%")
	@Query("Select u from User u where concat(u.id, ' ' , u.email, ' ' , u.firstName, ' ' ," + " u.lastName) like %?1%")
	public Page<User> findAll(String ketword, Pageable pageable);
	
	@Query("Update User u set u.enabled =?2 where u.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
