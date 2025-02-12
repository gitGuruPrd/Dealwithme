package com.property.dealwithme.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.property.dealwithme.dtos.PropertyDTO;
import com.property.dealwithme.dtos.UserDTO;
import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.managers.PropertyManager;
import com.property.dealwithme.managers.UserManager;
import com.property.dealwithme.mapper.PropertyMapper;
import com.property.dealwithme.requests.LoginRequest;
import com.property.dealwithme.requests.UserRequest;
import com.property.dealwithme.response.LoginResponse;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.services.UserService;
import com.property.dealwithme.utility.EmailUtility;
import com.property.dealwithme.utility.JwtUtility;

@SpringBootTest
@DisplayName("Testing UserService")
public class UserServiceTests {

	@InjectMocks
	private UserService userService;
	
	@Mock
	UserManager userManager;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private EmailUtility emailUtility;
	
	@Mock
	private JwtUtility jwtUtility;
	
	@Mock
	private UserDetailsService userDetailsService;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private PropertyManager propertyManager;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Testing method signUp()")
	public void doTestSignup() {
		UserRequest userRequest = new UserRequest();
        userRequest.setEmail("testemail@gmail.com");
        userRequest.setPassword("testPassword");
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedpassword");
        userRequest.setName("TestName");

        UserDTO expectedDto = new UserDTO();
        expectedDto.setEmail("testemail@gmail.com");
        expectedDto.setPassword("encodedpassword");

        String result = "Signup successful! Please verify your email.";
        when(userService.signup(userRequest)).thenReturn(result);  
        assertEquals(result, userService.signup(userRequest)); 
        verify(userManager).save(expectedDto); 
		
	}
	
	@Test
	public void testLogin() {
//		Arrange
		LoginRequest loginRequest=new LoginRequest("testemail@gmail.com","password");
		String token="token";
		UserDetails userDetails=Mockito.mock(UserDetails.class);
//		Act
		when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
		when(jwtUtility.generateToken(userDetails)).thenReturn(token);
		LoginResponse loginResponse= userService.login(loginRequest);
//		Assert
		assertNotNull(loginResponse);
		assertEquals(token,loginResponse.getToken());
		
		
	}
	
	@Test
	public void shouldTestForInvalidLogin() {
//		Arrange
		LoginRequest loginRequest=new LoginRequest("testemail@gmail.com","password");
		UserDetails userDetails=Mockito.mock(UserDetails.class);
//		Act
		when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Invalid Username or Password"));
//		userService.login(loginRequest);
//		Assert
		assertThrows(BadCredentialsException.class, ()->userService.login(loginRequest));
	}
	
	@Test
	public void testGetUserProperties() {
//		Arrange
		String email="testemail";
		UserDTO userDTO=new UserDTO();
		List<PropertyDTO> propertiesList=new ArrayList<PropertyDTO>();
		propertiesList.add(new PropertyDTO(1L,"testTitle" , "testDescription",(double)40,"testLocation" , PropertyTypeEnum.APARTMENT, email, userDTO));
		List<PropertyResponse> propertyResponses=new ArrayList<PropertyResponse>();
//		Act
		when(userManager.getByEmail(email)).thenReturn(userDTO);
		when(propertyManager.getPropertiesByOwner(anyLong())).thenReturn(propertiesList);
		when(PropertyMapper.INSTANCE.toPropertyResponseList(anyList())).thenReturn(propertyResponses);
		List<PropertyResponse> resultPropertyResponses=userService.getUserProperties(email);
//		Assert
		assertNotNull(propertyResponses);
		assertNotNull(resultPropertyResponses);
		assertEquals(propertyResponses.size(),resultPropertyResponses.size());
		
		
	}
	
	
}
