package com.example.demo2.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;

@Repository
public interface RoleReponsitory extends CrudRepository<Role, Integer> {


}
