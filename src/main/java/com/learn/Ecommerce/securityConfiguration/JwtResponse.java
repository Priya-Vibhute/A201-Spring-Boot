package com.learn.Ecommerce.securityConfiguration;

import com.learn.Ecommerce.Dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	
	
	private String token;
	private UserDto userDto;
	
	

}
