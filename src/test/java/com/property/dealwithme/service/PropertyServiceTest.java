package com.property.dealwithme.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import com.property.dealwithme.dtos.PropertyDTO;
import com.property.dealwithme.dtos.UserDTO;
import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.managers.PropertyManager;
import com.property.dealwithme.managers.UserManager;
import com.property.dealwithme.requests.CreatePropertyRequest;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.services.PropertyService;
import com.property.dealwithme.utility.EmailUtility;

@SpringBootTest
public class PropertyServiceTest {
	 	@Mock
	    private PropertyManager propertyManager;

	    @Mock
	    private UserManager userManager;

	    @Mock
	    private EmailUtility emailUtility;

	    @Mock
	    private MultipartFile image;

	    @InjectMocks
	    private PropertyService propertyService;

	    private UserDTO user;
	    private PropertyDTO property;
	    private CreatePropertyRequest request;

	    @BeforeEach
	    void setUp() {
	        user = new UserDTO();
	        user.setEmail("owner@example.com");

	        property = new PropertyDTO();
	        property.setId(1L);
	        property.setOwner(user);

	        request = new CreatePropertyRequest();
	        request.setTitle("Test Property");
	        request.setDescription("Test Description");
	        request.setPrice(100000.0);
	        request.setLocation("Test Location");
	        request.setType(PropertyTypeEnum.APARTMENT);
	    }

	    @Test
	    void testCreateProperty_Success() throws IOException {
	        when(userManager.getByEmail(anyString())).thenReturn(user);
	        when(propertyManager.save(any(PropertyDTO.class))).thenReturn(property);

	        PropertyResponse response = propertyService.createProperty(request, image, "owner@example.com");

	        assertNotNull(response);
	        verify(propertyManager).save(any(PropertyDTO.class));
	    }

	    @Test
	    void testCreateProperty_UserNotFound() {
	        when(userManager.getByEmail(anyString())).thenReturn(null);

	        assertThrows(IllegalArgumentException.class, () -> 
	            propertyService.createProperty(request, image, "owner@example.com"));
	    }

	    @Test
	    void testGetProperty_Success() {
	        when(propertyManager.findById(anyLong())).thenReturn(property);

	        PropertyResponse response = propertyService.getProperty(1L);

	        assertNotNull(response);
	        assertEquals(1L, response.getId());
	    }

	    @Test
	    void testGetProperty_NotFound() {
	        when(propertyManager.findById(anyLong())).thenReturn(null);

	        assertThrows(IllegalArgumentException.class, () -> propertyService.getProperty(1L));
	    }
}
