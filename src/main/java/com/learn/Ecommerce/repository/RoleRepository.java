package com.learn.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.learn.Ecommerce.entity.Role;

@CrossOrigin
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	

}
