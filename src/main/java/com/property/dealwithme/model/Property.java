package com.property.dealwithme.model;

import com.property.dealwithme.enums.PropertyTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "properties")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Property {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PropertyTypeEnum type; // RENT or SELL

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false) // Foreign key to users table
	private UserEntity owner;

	@Column
	private String imageUrl;
}
