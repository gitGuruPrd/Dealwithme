package com.property.dealwithme.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.property.dealwithme.enums.PropertyTypeEnum;
import com.property.dealwithme.model.Property;

public interface PropertyRepo extends JpaRepository<Property, Long> {

	@Query("SELECT p FROM Property p WHERE " +"(:location IS NULL OR p.location LIKE %:location%) AND "+ "(:minPrice IS NULL OR p.price >= :minPrice) AND "+ "(:maxPrice IS NULL OR p.price <= maxPrice) AND " + 
	"(:type IS NULL OR p.type= :typ)"
			)
	Page<Property> findAllWithFilters(@Param("location") String location, @Param("minPrice") Double minPrice,@Param("maxPrice") Double maxPrice,
			@Param("type") PropertyTypeEnum type, Pageable pageable);
	
	List<Property> findByOwnerId(Long ownerId);
}
