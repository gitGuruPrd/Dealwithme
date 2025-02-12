package com.property.dealwithme.managers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.property.dealwithme.dtos.PropertyDTO;
import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.model.Property;
import com.property.dealwithme.repo.PropertyRepo;
import com.property.dealwithme.response.PropertyResponse;
import com.property.dealwithme.mapper.PropertyMapper;
@Component
public class PropertyManager {

	@Autowired
	private PropertyRepo propertyRepository;

	@Autowired
	private UserManager userManager;

	public PropertyDTO save(PropertyDTO propertyDTO) {
		Property entity = PropertyMapper.INSTANCE.toEntity(propertyDTO);
		return PropertyMapper.INSTANCE.toPropertyDTO(propertyRepository.save(entity));
	}
	
	public PropertyDTO findById(Long id)
	{
		Optional<Property> entity= propertyRepository.findById(id);
		if(entity.isPresent())
			return PropertyMapper.INSTANCE.toPropertyDTO(entity.get());
		return null;
	}
	
	public Page<PropertyResponse> getAllProperties(String location, Double minPrice, Double maxPrice, PropertyTypeEnum type, int page, int size ){
		Pageable pageable=PageRequest.of(page, size);
		Page<Property> propertyPage=propertyRepository.findAllWithFilters(location, minPrice, maxPrice, type, pageable);
		return PropertyMapper.INSTANCE.toPagePropertyResponse(propertyPage);
	}
	
	public List<PropertyDTO> getPropertiesByOwner(Long ownerId)
	{
		return PropertyMapper.INSTANCE.toPropertyDTOList(propertyRepository.findByOwnerId(ownerId));
	}
}
