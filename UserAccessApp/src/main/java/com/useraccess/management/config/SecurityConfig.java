package com.useraccess.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails admin = User.withUsername("pavan").password(passwordEncoder().encode("pavan")).roles("ADMIN").build();

		UserDetails user = User.withUsername("pa1").password(passwordEncoder().encode("pa1")).roles("USER").build();
		return new MapReactiveUserDetailsService(admin, user);
	}

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange(perm->perm.pathMatchers("/user-access/add/user", "/user-access/authenticate").permitAll())
		.authorizeExchange(req -> req.pathMatchers("/user-access/users").authenticated())
				.authorizeExchange(auth -> auth.pathMatchers("/user-access/**").hasAuthority("ROLE_ADMIN"))
				.formLogin(Customizer.withDefaults());
		return http.csrf(csrf->csrf.disable()).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ReactiveAuthenticationManager authenticationManager() throws Exception {
		UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
	}

}
