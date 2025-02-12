package com.property.dealwithme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.requests.CreatePropertyRequest;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.services.PropertyService;
import com.property.dealwithme.utility.JwtUtility;

import jakarta.el.PropertyNotFoundException;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private JwtUtility jwtUtility;

	@PostMapping
	public ResponseEntity<?> createProperty(@RequestPart("property") CreatePropertyRequest request,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestHeader("Authorization") String token) {
		try {

			String ownerEmail = jwtUtility.extractEmail(token);

			PropertyResponse response = propertyService.createProperty(request, image, ownerEmail);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editProperty(@PathVariable Long id, @RequestBody CreatePropertyRequest request,
			@RequestHeader("Authorization") String token) {
		try {
			String ownerEmail = jwtUtility.extractEmail(token);

			PropertyResponse response = propertyService.editProperty(id, request, ownerEmail);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllProperties(@RequestParam(required = false) String location, 
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, 
			@RequestParam(required = false) PropertyTypeEnum type, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size 
	) {
		try {
			Page<PropertyResponse> properties = propertyService.getAllProperties(location, minPrice, maxPrice, type,
					page, size);
			return ResponseEntity.ok(properties);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{propertyId}")
	public ResponseEntity<?> getPropertyDetails(@PathVariable Long propertyId) {
		try {
			PropertyResponse property = propertyService.getProperty(propertyId);
			return ResponseEntity.ok(property);
		} catch (PropertyNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}


	@PostMapping("/{propertyId}/interest")
	public ResponseEntity<?> expressInterest(@PathVariable Long propertyId,
			@RequestHeader("Authorization") String token) {
		try {
			String userEmail = jwtUtility.extractEmail(token);
			propertyService.sendInterestEmail(propertyId, userEmail);
			return ResponseEntity.ok("Interest email sent successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}