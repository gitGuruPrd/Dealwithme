package com.property.dealwithme.managers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.property.dealwithme.dtos.UserDTO;
import com.property.dealwithme.mapper.UserMapper;
import com.property.dealwithme.model.UserEntity;
import com.property.dealwithme.repo.UserRepo;

@Component
public class UserManager {

	
	@Autowired
	private UserRepo userRepo;
	
	public UserDTO getByEmail(String email)
	{
		Optional<UserEntity> userEntityOptional= userRepo.findByEmail(email);
		if(userEntityOptional.isPresent())
			return UserMapper.INSTANCE.toUserDTO(userEntityOptional.get());
		return null;
	}
	
	public void save(UserDTO userDTO) {
        userRepo.save(UserMapper.INSTANCE.toUserEntity(userDTO));
    }
}
