package com.example.demo2.dao;

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
	
	@Query("Update User u set u.enable =?2 where u.id =?1")
	@Modifying
	public void updateEnableStatus(Integer id, boolean enable);
}
