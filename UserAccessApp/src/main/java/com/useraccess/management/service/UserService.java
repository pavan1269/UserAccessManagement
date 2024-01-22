package com.useraccess.management.service;

import com.useraccess.management.dto.UserDto;
import com.useraccess.management.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	Mono<User> createUser(UserDto userDto);

	Mono<User> getUserById(Integer id);

	String createSampleUsers();

	String deleteUserById(Integer id);
	
	Flux<User> getAllUsers();

}
