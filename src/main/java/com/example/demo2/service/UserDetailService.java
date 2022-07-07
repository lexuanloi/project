package com.example.demo2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo2.dao.UserRepository;
import com.example.demo2.entity.User;
import com.example.demo2.conf.ShopMeUserDetails;

public class UserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		if (user != null) {
			return new ShopMeUserDetails(user);
		}
		throw new UsernameNotFoundException("Không tìm thấy user với email : " +email);
	}

}
