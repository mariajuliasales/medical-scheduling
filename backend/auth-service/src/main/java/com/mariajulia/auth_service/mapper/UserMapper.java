package com.mariajulia.auth_service.mapper;

import com.mariajulia.auth_service.dto.response.UserResponse;
import com.mariajulia.auth_service.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

}