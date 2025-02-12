package com.property.dealwithme.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.dealwithme.requests.LoginRequest;
import com.property.dealwithme.requests.UserRequest;
import com.property.dealwithme.response.LoginResponse;
import com.property.dealwithme.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests {
    
    MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test API: POST '/auth/signup'")
    void testSignup() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setPhone("1234567890");
        userRequest.setRole("USER");
        
        String request = objectMapper.writeValueAsString(userRequest);
        
        Mockito.when(userService.signup(any(UserRequest.class))).thenReturn("Signup successful! Please verify your email.");
        
        mockMvc.perform(post("/auth/signup")
                .contentType("application/json")
                .content(request))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test API: POST '/auth/verify'")
    void testVerifyOtp() throws Exception {
        Mockito.when(userService.verifyOtp("test@example.com", "1234")).thenReturn("Email verified successfully!");
        
        mockMvc.perform(post("/auth/verify")
                .param("email", "test@example.com")
                .param("otp", "1234"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test API: POST '/auth/login'")
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        
        String request = objectMapper.writeValueAsString(loginRequest);
        
        LoginResponse loginResponse = new LoginResponse(200, "Login Successful", "token");
        
        Mockito.when(userService.login(any(LoginRequest.class))).thenReturn(loginResponse);
        
        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content(request))
                .andExpect(status().isOk());
    }
}

