package com.learn.Ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.learn.Ecommerce.entity.User;

@CrossOrigin
public interface UserRepository extends  JpaRepository<User, String> {
	
	Optional<User> findByEmailId(String emailId);

}
