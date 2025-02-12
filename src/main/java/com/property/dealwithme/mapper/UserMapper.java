package com.property.dealwithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.property.dealwithme.dtos.UserDTO;
import com.property.dealwithme.model.UserEntity;
import com.property.dealwithme.requests.UserRequest;
import com.property.dealwithme.response.UserResponse;


@Mapper
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(UserEntity userEntity);

    UserDTO toUserDTO(UserRequest userRequest);
    UserResponse toUserResponse(UserDTO userDTO);
	
}
