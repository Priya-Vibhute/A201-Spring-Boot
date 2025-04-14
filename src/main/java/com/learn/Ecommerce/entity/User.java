package com.learn.Ecommerce.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
	
	@Id
	private String id;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false,unique = true)
	private String emailId;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private int age;
	@ManyToOne
	private Role role;
 	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> list=new ArrayList<SimpleGrantedAuthority>();
		
		list.add(new SimpleGrantedAuthority(this.role.getRoleName()));
 		return list;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.emailId;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}
	
}
