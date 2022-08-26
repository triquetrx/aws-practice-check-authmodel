package com.cognizant.practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.practice.model.Users;
import com.cognizant.practice.repository.UserRepository;

@Service
public class NewUserService {
	
	@Autowired
	UserRepository repository;
	
	public void addUser(String username, String password) {
		int id = (int)repository.count()+1;
		repository.save(new Users(id,username,encoder().encode(password),"ROLE_USER"));	
	}
	
	private PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
