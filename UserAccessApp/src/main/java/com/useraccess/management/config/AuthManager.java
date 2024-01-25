package com.useraccess.management.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.useraccess.management.service.JWTService;

import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager{

	private JWTService jwtService;
	
	private ReactiveUserDetailsService users;
	
	public AuthManager(JWTService jwtService, ReactiveUserDetailsService users) {
		super();
		this.jwtService = jwtService;
		this.users = users;
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.justOrEmpty(authentication)
				.cast(BearerToken.class)
				.flatMap(auth -> {
					String username = jwtService.getUserName(auth.getCredentials());
					Mono<UserDetails> userMono = users.findByUsername(username);
					Mono<Authentication> authUser = userMono.flatMap(u->{
						if(jwtService.validate(u, auth.getCredentials())) {
							return Mono.just(new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword(),u.getAuthorities()));
						}
						Mono.error(new IllegalArgumentException("Invalid Token"));
						return Mono.just(new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword(),u.getAuthorities()));
					});
					return authUser;
				});
	}

}
