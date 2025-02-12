package com.property.dealwithme.dtos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.property.dealwithme.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	private String name;
	private String email;
	private String password;
	private String phone;
	private boolean verified;
	private String otp;
	private LocalDateTime otpGeneratedTime;
	private Set<Role> roles=new HashSet<Role>();
}
