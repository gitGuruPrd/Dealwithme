package com.property.dealwithme.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends BaseResponse {

	private String token;

	public LoginResponse(int statusCode, String message, String token) {
		super(statusCode, message);
		this.token = token;
	}
}
