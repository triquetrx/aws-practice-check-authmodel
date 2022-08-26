package com.cognizant.practice.model;

import java.io.Serializable;

import lombok.Getter;

public @Getter class AuthenticationResponse implements Serializable{

	private static final long serialVersionUID = 2505905724259940597L;
	
	private final String token;
	private final String username;
	private final String userRole;
	
	public AuthenticationResponse(String token, String username, String userRole) {
		super();
		this.token = token;
		this.username = username;
		this.userRole = userRole;
	}
	
	

}
