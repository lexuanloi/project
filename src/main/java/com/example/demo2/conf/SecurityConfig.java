package com.example.demo2.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo2.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/users/**").hasAuthority("Admin")
			.antMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editor")
			
			.antMatchers("/products/new-product","/products/delete/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/products/edit/**","/products/save", "/products/check_unique").hasAnyAuthority("Admin","Editor", "Salesperson")
			.antMatchers("/products/list_products", "/products/detail/**", "/products/list_products/page/**").hasAnyAuthority("Admin","Editor", "Salesperson", "Shipper")
			.antMatchers("/products/**").hasAnyAuthority("Admin","Editor")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.usernameParameter("email")
				.permitAll()
			.and().logout().permitAll()
			.and()
				.rememberMe()
					.key("AbcDefgHijKlmnOpqrs_1234567890")
					.tokenValiditySeconds(1 * 24 *60 *60); // 24h

//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/css/**");
	}
	
	
	
}
