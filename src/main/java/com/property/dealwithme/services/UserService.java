package com.property.dealwithme.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.property.dealwithme.dtos.PropertyDTO;
import com.property.dealwithme.dtos.UserDTO;
import com.property.dealwithme.managers.PropertyManager;
import com.property.dealwithme.managers.UserManager;
import com.property.dealwithme.mapper.PropertyMapper;
import com.property.dealwithme.mapper.UserMapper;
import com.property.dealwithme.model.Role;
import com.property.dealwithme.requests.LoginRequest;
import com.property.dealwithme.requests.UserRequest;
import com.property.dealwithme.response.LoginResponse;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.response.UserResponse;
import com.property.dealwithme.utility.EmailUtility;
import com.property.dealwithme.utility.JwtUtility;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class UserService {
	@Autowired
	private UserManager userManager;

	@Autowired
	private PropertyManager propertyManager;

	@Autowired
	private EmailUtility emailUtility;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtility jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public String signup(UserRequest userRequest) {
		UserDTO userDTO = userManager.getByEmail(userRequest.getEmail());
		if (userDTO != null) {
			throw new IllegalArgumentException("Email is already registered.");
		}

		UserDTO user = new UserDTO();
		user.setName(userRequest.getName());
		user.setEmail(userRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setPhone(userRequest.getPhone());
		user.setVerified(false);
		Role role=new Role();
		if(userRequest.getRole().equalsIgnoreCase("ADMIN")) {
			role.setRoleName("ADMIN");
		}else {
			role.setRoleName("USER");
		}
		Set<Role> roleSet=new HashSet<Role>();
		roleSet.add(role);
		user.setRoles(roleSet);

		String otp = String.valueOf(new Random().nextInt(9000) + 1000);
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());

		userManager.save(user);
		
		emailUtility.sendEmail(user.getEmail(), "Email Verification", "Your OTP is: " + otp);

		return "Signup successful! Please verify your email.";
	}

	public String verifyOtp(String email, String otp) {
		UserDTO userDTO = userManager.getByEmail(email);
		if (userDTO == null) {
			throw new UsernameNotFoundException("User not found");
		}

		// Check OTP and expiration
		if (userDTO.getOtp().equals(otp)) {
			if (userDTO.getOtpGeneratedTime().isBefore(LocalDateTime.now().minusMinutes(10))) {
				throw new IllegalArgumentException("OTP has expired.");
			}
			userDTO.setVerified(true);
			userDTO.setOtp(null); // Clear OTP after successful verification
			userDTO.setOtpGeneratedTime(null);
			userManager.save(userDTO);
			return "Email verified successfully!";
		} else {
			throw new IllegalArgumentException("Invalid OTP.");
		}
	}

	public LoginResponse login(LoginRequest loginRequest) {
		this.doAuthenticate(loginRequest.getEmail(), loginRequest.getPassword());
		UserDTO userDTO = userManager.getByEmail(loginRequest.getEmail());
		if (userDTO == null) {
			return new LoginResponse(404, "User Not Found", null);
		}
		if (!passwordEncoder.matches(loginRequest.getPassword(), userDTO.getPassword())) {
			return new LoginResponse(401, "Invalid Password", null);
		}
		UserDetails userDetails=userDetailsService.loadUserByUsername(loginRequest.getEmail());
		String token = jwtTokenUtil.generateToken(userDetails);
		return new LoginResponse(200, "Login Successful", token);
	}

	public UserResponse getUserProfile(String email) {
		UserDTO user = userManager.getByEmail(email);
		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}
		return UserMapper.INSTANCE.toUserResponse(user);
	}

	public void updateUserProfile(String email, UserRequest updateRequest) {
		UserDTO user = userManager.getByEmail(email);
		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}

		user.setName(updateRequest.getName());
		user.setPhone(updateRequest.getPhone());

		userManager.save(user);
	}

	public List<PropertyResponse> getUserProperties(String email) {
		UserDTO user = userManager.getByEmail(email);
		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}
		List<PropertyDTO> properties = propertyManager.getPropertiesByOwner(user.getId());
		return PropertyMapper.INSTANCE.toPropertyResponseList(properties);
	}
	
	private void doAuthenticate(String username,String password) {
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username, password);
		try {
			authenticationManager.authenticate(authenticationToken);
		}catch(BadCredentialsException e){
			throw new BadCredentialsException("Invalid username or password");
		}
	}
	
	

}
