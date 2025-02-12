package com.property.dealwithme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.property.dealwithme.requests.LoginRequest;
import com.property.dealwithme.requests.UserRequest;
import com.property.dealwithme.response.LoginResponse;
import com.property.dealwithme.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid UserRequest userRequest) {
		String message = userService.signup(userRequest);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
		String message = userService.verifyOtp(email, otp);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		LoginResponse response = userService.login(loginRequest);
		return ResponseEntity.ok(response);
	}
}