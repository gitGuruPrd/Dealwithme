package com.property.dealwithme.integeration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.dealwithme.requests.CreatePropertyRequest;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.repo.PropertyRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PropertyControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropertyRepo propertyRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db1")
            .withUsername("testUser1")
            .withPassword("password1");

    @BeforeAll
    static void beforeAll() {
        POSTGRE_SQL_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        POSTGRE_SQL_CONTAINER.stop();
    }

    @BeforeEach
    void setup() {
        propertyRepo.deleteAll();
    }

    @Test
    @Order(1)
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

        mockMvc.perform(multipart("/api/properties")
                        .file(propertyPart)
                        .file(imagePart)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("Test API: PUT '/api/properties/{id}'")
    void testEditProperty() throws Exception {
        CreatePropertyRequest request = new CreatePropertyRequest();
        request.setTitle("Updated Property");
        request.setDescription("Updated Description");
        request.setPrice(150000.0);
        request.setLocation("Updated Location");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token")
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("Test API: GET '/api/properties/{propertyId}'")
    void testGetPropertyDetails() throws Exception {
        mockMvc.perform(get("/api/properties/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Test API: POST '/api/properties/{propertyId}/interest'")
    void testExpressInterest() throws Exception {
        mockMvc.perform(post("/api/properties/1/interest")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }
}
