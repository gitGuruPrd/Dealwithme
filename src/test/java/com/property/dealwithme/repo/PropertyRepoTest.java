
package com.property.dealwithme.repo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.model.Property;
import com.property.dealwithme.model.UserEntity;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PropertyRepoTest {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PropertyRepo propertyRep;
	private UserEntity savedUserEntity;
	private Property savedProperty;
	
	private Property property1;
    private Property property2;
    private Property property3;
	
	
	private final static MySQLContainer<?> My_SQL_CONTAINER=new MySQLContainer<>("mysql:latest").withDatabaseName("testdatabasename").withUsername("testuser").withPassword("password");
	
	@BeforeEach
	void setup() {
		My_SQL_CONTAINER.start();
		Property property=new Property();
		property.setId(1L);
		property.setDescription("testDescriprtion");
		property.setImageUrl("testUrl");
		property.setLocation("testLocation");
		UserEntity userEntity=new UserEntity();
		property.setOwner(userEntity);
		property.setPrice(1D);
		property.setTitle("testTitle");
		savedProperty=propertyRep.save(property);
		UserEntity newUser=new UserEntity();
		newUser.setEmail("testemail");
		newUser.setId(1L);
		savedUserEntity=userRepo.save(newUser);
		property1 = new Property(2L, "Awwas", "testDescription", 100000.0, "New York", PropertyTypeEnum.APARTMENT, newUser, "testImageUrl");
        property2 = new Property(3L, "Grah", "testDescription", 200000.0, "Mumbai", PropertyTypeEnum.VILLA, newUser, "testImageUrl");
        
        
        

		
	}
	
	@DynamicPropertySource
	static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",My_SQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username",My_SQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password",My_SQL_CONTAINER::getPassword);
		
	}
	
	

	@Test
	public void testGetPropertyByOwnerId() {
		List<Property> propertyList=propertyRep.findByOwnerId(1L);
		assertNotNull(propertyList);
		assertEquals(3, propertyList.size());
		assertEquals(1L, propertyList.get(1).getId());
		
	}
	
	 @Test
	    void testFindAllWithFiltersNoResults() {
	        Page<Property> result = propertyRep.findAllWithFilters("San Francisco", 100000.0, 200000.0, PropertyTypeEnum.HOUSE, PageRequest.of(0, 10));
	        assertNotNull(result);
	        assertEquals(0, result.getTotalElements());
	    }
	 
	 @AfterEach
	 void tearDown() {
		 userRepo.deleteAll();
		 propertyRep.deleteAll();
	 }
	
}
