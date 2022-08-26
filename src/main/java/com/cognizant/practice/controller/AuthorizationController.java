package com.cognizant.practice.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.practice.dto.ChangePasswordDTO;
import com.cognizant.practice.dto.NewUserDTO;
import com.cognizant.practice.dto.ValidatingDTO;
import com.cognizant.practice.exception.InvalidPasswordException;
import com.cognizant.practice.model.AuthenticationRequest;
import com.cognizant.practice.model.AuthenticationResponse;
import com.cognizant.practice.service.JwtUserDetailsService;
import com.cognizant.practice.service.NewUserService;
import com.cognizant.practice.service.UserRequestService;
import com.cognizant.practice.util.JwtTokenUtil;

@RestController
public class AuthorizationController {
	
	@Autowired
	AuthenticationManager authentication;
	
	@Autowired
	JwtTokenUtil util;
	
	@Autowired
	JwtUserDetailsService userDetailsService;
	
	@Autowired
	NewUserService newUserService;
	
	@Autowired
	UserRequestService userRequestService;
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest request) throws Exception {
		try {			
			authenticate(request.getUsername(),request.getPassword());
			final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
			final String token = util.generateToken(userDetails);
			String userRole = userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(","));
			return ResponseEntity.ok(new AuthenticationResponse(token, request.getUsername(), userRole));
		} catch (DisabledException e) {
			return new ResponseEntity<>("USER_DISABLED",HttpStatus.FORBIDDEN);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>("INVALID_CREDENTIALS",HttpStatus.FORBIDDEN);
		}
	}
	
	@GetMapping("/get-user-id")
	public ResponseEntity<?> getUserId(@RequestHeader(name="Authorization") String token){
		String tokenDup = token.substring(7);
		try {
			UserDetails user = userDetailsService.loadUserByUsername(util.getUsernamefromToken(tokenDup));
			if(util.validateToken(tokenDup, user)) {
				return new ResponseEntity<>(userRequestService.getUserId(user.getUsername()),HttpStatus.OK);
			}
			return new ResponseEntity<>("Token expired or invalid",HttpStatus.GATEWAY_TIMEOUT);
		} catch(Exception e) {
			return new ResponseEntity<>("Invalid Token or Token Expired",HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/validate")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<?> validatingToken(@RequestHeader(name="Authorization") String token){
		String tokenDup = token.substring(7);
		try {
			UserDetails user = userDetailsService.loadUserByUsername(util.getUsernamefromToken(tokenDup));
			String userRole = user.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(","));
			if(util.validateToken(tokenDup, user)) {
				return ResponseEntity
						.status(HttpStatus.OK)
						.body(new ValidatingDTO(true,userRole));
			}
			return new ResponseEntity<>("Token expired or invalid",HttpStatus.GATEWAY_TIMEOUT);
		} catch(Exception e) {
			return new ResponseEntity<>("Invalid Token or Token Expired",HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/create-new-user")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<?> createNewUser(@RequestHeader(name="Authorization")String token,@RequestBody NewUserDTO newUserDTO) {
		String tokenDup = token.substring(7);
		try {
			UserDetails user = userDetailsService.loadUserByUsername(util.getUsernamefromToken(tokenDup));
			if(util.validateToken(tokenDup, user)) {
				newUserService.addUser(newUserDTO.getUsername(), newUserDTO.getPassword());
				return ResponseEntity.ok("New User Created");
			}
			return new ResponseEntity<>("INVALID_TOKEN",HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			return new ResponseEntity<>("INVALID_TOKEN",HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/change-password")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<?> changePassword(@RequestHeader(name = "Authorization")String token, @RequestBody ChangePasswordDTO changePasswordDTO){
		String tokenDup = token.substring(7);
		try {
			UserDetails user = userDetailsService.loadUserByUsername(util.getUsernamefromToken(tokenDup));
			if(util.validateToken(tokenDup, user)) {
				String result = userRequestService.changePassword(user.getUsername(), changePasswordDTO);
				return new ResponseEntity<>(result,HttpStatus.OK);
			}			
			return new ResponseEntity<>("INVALID_TOKEN",HttpStatus.UNAUTHORIZED);
		} catch(InvalidPasswordException e) {
			return new ResponseEntity<>("OLD_PASSWORD_NOT_A_MATCH",HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<>("INVALID_TOKEN",HttpStatus.UNAUTHORIZED);
		}
	}
	
	private void authenticate(String username,String password) throws Exception {
		try {
			authentication.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException("USER_DISABLED",e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS",e);
		}
	}

}
