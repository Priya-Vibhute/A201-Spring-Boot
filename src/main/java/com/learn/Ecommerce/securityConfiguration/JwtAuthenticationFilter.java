package com.learn.Ecommerce.securityConfiguration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtHelper jwtHelper;
	
	@Autowired
	UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String requestHeader = request.getHeader("Authorization");
		String username=null;
		String token=null;
		
		
		if(requestHeader!=null && requestHeader.startsWith("Bearer"))
		{
			token=requestHeader.substring(7);
			try
			{
				username=jwtHelper.getUsernameFromToken(token);
				
			}catch(IllegalArgumentException e)
			{
				System.out.println("Illegal Argument");
			}
			
			catch(ExpiredJwtException e)
			{
				System.out.println(e.getMessage());
			}
			catch (MalformedJwtException e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		else
		{
			System.out.println("Invalid Header");
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails user = userDetailsService.loadUserByUsername(username);
			if(jwtHelper.validateToken(token, user))
			{
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			
		}
		
		filterChain.doFilter(request, response);
	}

}
