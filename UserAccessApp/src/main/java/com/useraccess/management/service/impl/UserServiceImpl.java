package com.useraccess.management.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.useraccess.management.dto.UserDto;
import com.useraccess.management.entity.User;
import com.useraccess.management.repository.UserRepo;
import com.useraccess.management.service.UserService;

import ch.qos.logback.core.encoder.Encoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepo userRepo;
	
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepo userRepo,PasswordEncoder encoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = encoder;
	}
	
	@Override
	public Mono<User> createUser(UserDto userDto) {
		return userRepo.save(new User(userDto.getUsername(),passwordEncoder.encode(userDto.getPassword()),userDto.getRoles()));
	}

	@Override
	public Mono<User> getUserById(Integer id) {
		System.out.println("went to db.....");
		return userRepo.findById(id);
	}

	@Override
	public String createSampleUsers() {
		Mono<User> u;
		for(int i=1;i<=50;i++) {
			u = userRepo.save(new User("user"+String.valueOf(i),passwordEncoder.encode("user"+String.valueOf(i)),"ROLE_USER"));
			u.subscribe();
		}
		return "sample users added into db...";
	}
	
	@Override
	public String deleteUserById(Integer id) {
		userRepo.deleteById(id).subscribe();
		return "user with id "+ id + " is deleted";
	}

	@Override
	public Flux<User> getAllUsers() {
		return userRepo.findAll();
	}

}
