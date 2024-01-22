package com.useraccess.management.dto;

import lombok.Data;

@Data
public class UserDto {

	private String username;
	private String password;
	private String roles;
	
	public UserDto(String username, String password, String roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
}
