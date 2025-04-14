package com.learn.Ecommerce.securityConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
//	Configure security
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		
		httpSecurity.authorizeHttpRequests(request->{
			
			request.requestMatchers(HttpMethod.POST, "/users","/login","products/image/**").permitAll();
			request.requestMatchers(HttpMethod.GET, "/users","/products/image/**","/products/**","/products").permitAll();
			request.requestMatchers(HttpMethod.POST,"/products").permitAll();
			request.anyRequest().authenticated();
			

            			
		}).csrf(csrf->csrf.disable())
		.cors(cors->Customizer.withDefaults());
		
//		httpSecurity.httpBasic(Customizer.withDefaults());
//		httpSecurity.formLogin(Customizer.withDefaults());
		
		httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint));
		
		httpSecurity.addFilterBefore(authenticationFilter,UsernamePasswordAuthenticationFilter.class);
		
		httpSecurity.sessionManagement(session->
		session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return httpSecurity.build();
	}
	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		return  daoAuthenticationProvider;
	}
	
	
	
	 @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
	        return builder.getAuthenticationManager();
	    }
	 
//	// CORS configuration
//	    @Bean
//	    public CorsFilter corsFilter() {
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration config = new CorsConfiguration();
//	        config.setAllowCredentials(true);
//	        config.addAllowedOrigin("http://localhost:3000"); // Frontend origin
//	        config.addAllowedHeader("*"); // Allow all headers
//	        config.addAllowedMethod("*"); // Allow all methods (GET, POST, etc.)
//	        source.registerCorsConfiguration("/**", config); // Apply to all endpoints
//	        return new CorsFilter(source);
//	    }

}
