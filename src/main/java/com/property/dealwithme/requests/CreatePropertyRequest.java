package com.property.dealwithme.requests;

import com.property.dealwithme.enums.PropertyTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyRequest {

	private String title;
	private String description;
	private Double price;
	private String location;
	private PropertyTypeEnum type;
}
