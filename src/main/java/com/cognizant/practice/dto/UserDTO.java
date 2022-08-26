package com.cognizant.practice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @NoArgsConstructor class UserDTO {

	private String username;
	private String password;

	public UserDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
