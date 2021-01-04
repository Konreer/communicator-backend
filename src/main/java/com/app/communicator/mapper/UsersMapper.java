package com.app.communicator.mapper;

import com.app.communicator.dto.securityDto.RegisterUserDto;
import com.app.communicator.entity.User;

public interface UsersMapper {

    static User fromRegisterUserToUser(RegisterUserDto registerUserDto) {
        return User
                .builder()
                .name(registerUserDto.getName())
                .surname(registerUserDto.getSurname())
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .password(registerUserDto.getPassword())
                .build();
    }

}
