package com.property.dealwithme.services;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.property.dealwithme.dtos.PropertyDTO;
import com.property.dealwithme.dtos.UserDTO;
import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.managers.PropertyManager;
import com.property.dealwithme.managers.UserManager;
import com.property.dealwithme.mapper.PropertyMapper;
import com.property.dealwithme.requests.CreatePropertyRequest;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.utility.EmailUtility;

@Service
public class PropertyService {
    @Autowired
    private PropertyManager propertyManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private EmailUtility emailUtility;

    
    private final String UPLOAD_DIR = "/Users/Pritish/Documents/coding-ninjas/cn-awaas-vishwa-spring-be/uploads/";

    public PropertyResponse createProperty(CreatePropertyRequest request, MultipartFile image, String ownerEmail) throws IOException {
        
        UserDTO owner = userManager.getByEmail(ownerEmail);
        if (owner == null) {
            throw new IllegalArgumentException("Owner not found");
        }

        
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            String originalFileName = image.getOriginalFilename();
            String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String uniqueFileName = timestamp + "_" + sanitizedFileName;

            
            String filePath = UPLOAD_DIR + uniqueFileName;

            image.transferTo(new File(filePath));
            imagePath = filePath;
        }

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setTitle(request.getTitle());
        propertyDTO.setDescription(request.getDescription());
        propertyDTO.setPrice(request.getPrice());
        propertyDTO.setLocation(request.getLocation());
        propertyDTO.setType(request.getType());
        propertyDTO.setOwner(owner);
        propertyDTO.setImageUrl(imagePath);

        return PropertyMapper.INSTANCE.toPropertyResponse(propertyManager.save(propertyDTO));
    }


    public PropertyResponse editProperty(Long propertyId, CreatePropertyRequest request, String ownerEmail) {

        PropertyDTO property = propertyManager.findById(propertyId);

        if (property == null) {
            throw new IllegalArgumentException("Property not found");
        }
        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("You are not authorized to edit this property");
        }
        
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setLocation(request.getLocation());
        property.setType(request.getType());

        return PropertyMapper.INSTANCE.toPropertyResponse(propertyManager.save(property));
    }

    public Page<PropertyResponse> getAllProperties(String location, Double minPrice, Double maxPrice, PropertyTypeEnum type, int page, int size) {
        return propertyManager.getAllProperties(location, minPrice, maxPrice, type, page, size);
    }

    public PropertyResponse getProperty(Long propertyId) {
        PropertyDTO property = propertyManager.findById(propertyId);
        if (property == null) {
            throw new IllegalArgumentException("Property not found");
        }
        return PropertyMapper.INSTANCE.toPropertyResponse(property);
    }

    public void sendInterestEmail(Long propertyId, String userEmail) {
        PropertyDTO property = propertyManager.findById(propertyId);

        if (property == null) {
            throw new IllegalArgumentException("Property not found");
        }

        UserDTO userDTO = userManager.getByEmail(userEmail);

        if (userDTO == null) {
            throw new IllegalArgumentException("User not found");
        }

        UserDTO ownerDTO = userManager.getByEmail(property.getOwner().getEmail());

        if (ownerDTO == null) {
            throw new IllegalArgumentException("Owner not found");
        }

        if (ownerDTO.getEmail().equals(userDTO.getEmail())) {
            throw new SecurityException("Cannot send interest email");
        }

        emailUtility.sendEmail(ownerDTO.getEmail(),
                "Interest in Your Property: " + property.getTitle(),
                "Dear " + ownerDTO.getName() + ",\n\n" +
                        "You have received interest for your property \"" + property.getTitle() + "\".\n\n" +
                        "Details:\n" +
                        "Name: " + userDTO.getName() + "\n" +
                        "Email: " + userDTO.getEmail() + "\n" +
                        "Best regards,\nYour Property Listing Platform");
    }
}
