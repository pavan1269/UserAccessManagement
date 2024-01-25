package com.useraccess.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails admin = User.withUsername("pavan").password("pavan").roles("ADMIN").build();

		UserDetails user = User.withUsername("pa1").password("pa1").roles("USER").build();
		return new MapReactiveUserDetailsService(admin, user);
	}

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http, AuthConverter jwthAuthConverter,
			AuthManager jwtAuthManager) throws Exception {

		AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthManager);
		jwtFilter.setServerAuthenticationConverter(jwthAuthConverter);
		http.authorizeExchange(
				perm -> perm.pathMatchers("/user-access/authenticate").permitAll())
				.authorizeExchange(req -> req.pathMatchers("/user-access/**").authenticated())
				.addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
		http.formLogin().disable();
		http.httpBasic().disable();
		return http.csrf(csrf -> csrf.disable()).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
