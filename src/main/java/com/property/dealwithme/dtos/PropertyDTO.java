package com.property.dealwithme.dtos;

import com.property.dealwithme.enums.PropertyTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {

	private Long id;
	private String title;
	private String description;
	private Double price;
	private String location;
	private PropertyTypeEnum type;
	private String imageUrl;
	private UserDTO owner;
}
 