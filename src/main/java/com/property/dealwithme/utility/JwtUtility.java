package com.property.dealwithme.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtility {
	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY;

	@Value("${security.jwt.expiration-time}")
	private long EXPIRATION_TIME;

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<String, Object>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName()),
						SignatureAlgorithm.HS512)
				.compact();
	}

	public boolean validateToken(String token, String email) {
		String tokenEmail = extractEmail(token);
		return email.equals(tokenEmail) && !isTokenExpired(token);
	}

	public String extractEmail(String token) {
		return this.getClaimsFromoken(token).getSubject();
	}

	public boolean isTokenExpired(String token) {
		Date expiration = this.getClaimsFromoken(token).getExpiration();
		return expiration.before(new Date());
	}

	public Claims getClaimsFromoken(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();
	}

}
