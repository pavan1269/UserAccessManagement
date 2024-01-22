package com.useraccess.management.controller;

import java.time.Duration;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.useraccess.management.dto.CredRequest;
import com.useraccess.management.dto.UserDto;
import com.useraccess.management.entity.User;
import com.useraccess.management.service.JWTService;
import com.useraccess.management.service.UserService;
import com.useraccess.management.service.impl.UserServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-access")
public class UserController {
	
	private UserService userService;
	
	private JWTService jwtService;
	
	private ReactiveUserDetailsService reactiveUserDetailsService;
	
	private PasswordEncoder encoder;
	
	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
	
	public UserController(UserServiceImpl userServiceImpl, JWTService jwtService, ReactiveUserDetailsService reactiveUserDetailsService, PasswordEncoder encoder) {
		this.userService = userServiceImpl;
		this.jwtService = jwtService;
		this.reactiveUserDetailsService = reactiveUserDetailsService;
		this.encoder = encoder;
	}
	
	@PostMapping(value = "/add/user",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<User> createUser(@RequestBody UserDto userDto ){
		return userService.createUser(userDto);
	}
	
	@PostMapping(value = "/add/sample-users")
	public String createSampleUsers(){
		return userService.createSampleUsers();
	}

	@GetMapping(value = "/user/{userId}")
	public Mono<User> getUserById(@PathVariable("userId") Integer id ){
		return userService.getUserById(id).log();
	}
	
	@GetMapping(value = "/users")
	public Flux<User> getUsers(){
		return userService.getAllUsers() .delayElements(Duration.ofSeconds(1));
	}
	
	@DeleteMapping(value = "/user/{userId}")
	public String deleteUserById(@PathVariable("userId") Integer id ){
		return userService.deleteUserById(id);
	}
	
	@PostMapping("/authenticate")
	public Mono<String> authenticateAndGetToken(@RequestBody CredRequest request) {
		Mono<UserDetails> user = reactiveUserDetailsService.findByUsername(request.getUsername())
									.defaultIfEmpty(null);
		
		return user.flatMap(u -> {
				if( u!=null && u.getPassword()==(encoder.encode(request.getPassword()))) {
					return Mono.just(jwtService.generateToken(request.getUsername()));
				}else {
					return null;
				}
		});
	}
}
