package com.cognizant.practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.practice.dto.ChangePasswordDTO;
import com.cognizant.practice.exception.InvalidPasswordException;
import com.cognizant.practice.model.Users;
import com.cognizant.practice.repository.UserRepository;

@Service
public class UserRequestService {

	@Autowired
	UserRepository userRepository;
	
	public String changePassword(String username,ChangePasswordDTO changePasswordDTO) throws InvalidPasswordException {
		 Users user = userRepository.findByUsername(username).get();
		if(encoder().matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
			user.setPassword(encoder().encode(changePasswordDTO.getNewPassword()));
			userRepository.save(user);
			return "PASSWORD_CHANGED_SUCCESSFULLY";
		} else {
			throw new InvalidPasswordException("OLD_PASSWORD_NOT_A_MATCH");
		}
	}
	
	public int getUserId(String username) {
		return userRepository.findByUsername(username).get().getUserId();
	}
	
	private PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
}
