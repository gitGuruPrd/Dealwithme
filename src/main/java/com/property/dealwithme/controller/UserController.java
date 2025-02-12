package com.property.dealwithme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.property.dealwithme.requests.UserRequest;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.response.UserResponse;
import com.property.dealwithme.services.UserService;
import com.property.dealwithme.utility.JwtUtility;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtility jwtUtility;

	@GetMapping("/profile")
	public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
		try {
			String email = jwtUtility.extractEmail(token);
			UserResponse userResponse = userService.getUserProfile(email);
			return ResponseEntity.ok(userResponse);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	
	@PutMapping("/profile")
	public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token,
			@RequestBody UserRequest updateRequest) {
		try {
			String email = jwtUtility.extractEmail(token);
			userService.updateUserProfile(email, updateRequest);
			return ResponseEntity.ok("Profile updated successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/properties")
	public ResponseEntity<?> getUserProperties(@RequestHeader("Authorization") String token) {
		try {
			String email = jwtUtility.extractEmail(token);
			List<PropertyResponse> properties = userService.getUserProperties(email);
			return ResponseEntity.ok(properties);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
