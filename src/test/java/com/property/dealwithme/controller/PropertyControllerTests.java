package com.property.dealwithme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.dealwithme.requests.CreatePropertyRequest;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.services.PropertyService;
import com.property.dealwithme.utility.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PropertyController.class)
public class PropertyControllerTests {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private JwtUtility jwtUtility;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Test API: POST '/api/properties'")
    void testCreateProperty() throws Exception {
        CreatePropertyRequest request = new CreatePropertyRequest();
        request.setTitle("Test Property");
        request.setDescription("Test Description");
        request.setPrice(100000.0);
        request.setLocation("Test Location");

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile propertyPart = new MockMultipartFile("property", "", "application/json", requestJson.getBytes());
        MockMultipartFile imagePart = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

        Mockito.when(jwtUtility.extractEmail("Bearer token")).thenReturn("test@example.com");
        Mockito.when(propertyService.createProperty(any(CreatePropertyRequest.class), any(), eq("test@example.com")))
                .thenReturn(new PropertyResponse());

        mockMvc.perform(multipart("/api/properties")
                .file(propertyPart)
                .file(imagePart)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test API: PUT '/api/properties/{id}'")
    void testEditProperty() throws Exception {
        CreatePropertyRequest request = new CreatePropertyRequest();
        request.setTitle("Updated Property");
        request.setDescription("Updated Description");
        request.setPrice(150000.0);
        request.setLocation("Updated Location");

        String requestJson = objectMapper.writeValueAsString(request);
        Mockito.when(jwtUtility.extractEmail("Bearer token")).thenReturn("test@example.com");
        Mockito.when(propertyService.editProperty(eq(1L), any(CreatePropertyRequest.class), eq("test@example.com")))
                .thenReturn(new PropertyResponse());

        mockMvc.perform(put("/api/properties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test API: GET '/api/properties/{propertyId}'")
    void testGetPropertyDetails() throws Exception {
        Mockito.when(propertyService.getProperty(1L)).thenReturn(new PropertyResponse());

        mockMvc.perform(get("/api/properties/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test API: POST '/api/properties/{propertyId}/interest'")
    void testExpressInterest() throws Exception {
        Mockito.when(jwtUtility.extractEmail("Bearer token")).thenReturn("test@example.com");

        mockMvc.perform(post("/api/properties/1/interest")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }
}
