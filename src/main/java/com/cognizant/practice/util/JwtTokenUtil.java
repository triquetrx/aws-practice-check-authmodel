package com.cognizant.practice.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 592901128428782964L;
	// Token validation time
	public static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60;
	@Value("${jwt.secretkey}")
	private String secret;

	// retrieve user name from JSON Web token
	public String getUsernamefromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// Expiration Date Resolver
	public Date getExpirationFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// Validate the token i.e. check if the userDetails & token are correct
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernamefromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// Generate the token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return generateNewToken(claims, userDetails.getUsername());
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationFromToken(token);
		return expiration.before(new Date());
	}

	private String generateNewToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

}
