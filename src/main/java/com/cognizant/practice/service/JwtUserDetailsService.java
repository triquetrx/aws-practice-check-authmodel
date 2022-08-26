package com.cognizant.practice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cognizant.practice.model.Users;
import com.cognizant.practice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> users = repository.findByUsername(username);
		log.info(username+" tried logging");
		users.orElseThrow(()->new UsernameNotFoundException("user not found"));
		return users.map(LocalUserDetails::new).get();
	}
	
	public boolean doesUserExists(String username) {
		Optional<Users> users = repository.findByUsername(username);
		return !users.isEmpty();
	}
	
	
}
