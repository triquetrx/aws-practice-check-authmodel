package com.cognizant.practice.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cognizant.practice.model.Users;

public class LocalUserDetails implements UserDetails {

	private static final long serialVersionUID = -8494426466906137900L;
	private String username;
	private String password;
	private List<GrantedAuthority> roles;

	public LocalUserDetails(Users user) {
		super();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.roles = Arrays.asList(user.getRoles().split(",")).stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
