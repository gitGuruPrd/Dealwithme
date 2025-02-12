package com.property.dealwithme.requests;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserRequest {
	private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean verified;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private String role;
}
