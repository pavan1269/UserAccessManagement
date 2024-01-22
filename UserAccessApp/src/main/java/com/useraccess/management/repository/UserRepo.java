package com.useraccess.management.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.useraccess.management.entity.User;

@Repository
public interface UserRepo extends ReactiveCrudRepository<User, Integer> {

}
