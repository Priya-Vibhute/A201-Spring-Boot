package com.learn.Ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.Ecommerce.Dto.UserDto;
import com.learn.Ecommerce.entity.User;
import com.learn.Ecommerce.securityConfiguration.JwtHelper;
import com.learn.Ecommerce.securityConfiguration.JwtRequest;
import com.learn.Ecommerce.securityConfiguration.JwtResponse;
import com.learn.Ecommerce.service.UserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	 private void doAuthenticate(String email, String password) {

	        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	        try {
	            authenticationManager.authenticate(authentication);
	        } catch (BadCredentialsException e) {
	            throw new BadCredentialsException(" Invalid Username or Password  !!");
	        }

	    }

	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest)
	{
		this.doAuthenticate(jwtRequest.getEmailId(),jwtRequest.getPassword());
		
	
				
				UserDetails userDetails=userDetailsService.loadUserByUsername(jwtRequest.getEmailId());
				
				String token = jwtHelper.generateToken(userDetails);
				
				UserDto dto = this.userService.entityToDto((User)userDetails);
		return new ResponseEntity<JwtResponse>(new JwtResponse(token, dto),HttpStatus.OK);
	}

}
