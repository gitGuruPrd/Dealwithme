package com.property.dealwithme.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.property.dealwithme.dtos.PropertyDTO;
import com.property.dealwithme.model.Property;
import com.property.dealwithme.requests.CreatePropertyRequest;
import com.property.dealwithme.response.PropertyResponse;

@Mapper
public interface PropertyMapper {

	PropertyMapper INSTANCE = Mappers.getMapper(PropertyMapper.class);

	Property toEntity(PropertyDTO dto);

	PropertyDTO toPropertyDTO(Property property);

	PropertyDTO toPropertyDTO(CreatePropertyRequest createPropertyRequest);

	PropertyResponse toPropertyResponse(PropertyDTO propertyDTO);

	default Page<PropertyResponse> toPagePropertyResponse(Page<Property> propertyPage) {
		return propertyPage.map(this::toPropertyResponseFromEntity);
	}

	PropertyResponse toPropertyResponseFromEntity(Property property);

	List<PropertyDTO> toPropertyDTOList(List<Property> propertyList);

	List<PropertyResponse> toPropertyResponseList(List<PropertyDTO> properties);

}
