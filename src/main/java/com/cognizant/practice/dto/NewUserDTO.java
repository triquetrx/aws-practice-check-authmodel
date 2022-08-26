package com.cognizant.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @NoArgsConstructor @AllArgsConstructor class NewUserDTO {
	
	private String username;
	private String password;
	private String userRole;

}
