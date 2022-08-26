package com.cognizant.practice.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public @Data @NoArgsConstructor @AllArgsConstructor class Users {

	@Id
	private Integer userId;
	private String username;
	private String password;
	private String roles;
	
}
