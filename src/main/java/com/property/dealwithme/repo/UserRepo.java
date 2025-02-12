package com.property.dealwithme.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.property.dealwithme.model.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);

	
}
