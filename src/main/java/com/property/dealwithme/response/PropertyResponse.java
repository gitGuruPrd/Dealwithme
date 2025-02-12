package com.property.dealwithme.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
	private Long id;
	private String title;
	private String description;
	private Double price;
	private String location;
	private String type;
	private String imageUrl;
	private String ownerEmail;
}
