package com.useraccess.management.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "users")
public class User implements Serializable{

	@Id
	private int id;
	private String username;
	private String password;
	private String roles;

	public User(String username, String password, String roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

}
