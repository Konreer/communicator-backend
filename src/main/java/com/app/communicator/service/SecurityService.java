package com.app.communicator.service;

import com.app.communicator.dto.securityDto.RegisterUserDto;
import com.app.communicator.mapper.UsersMapper;
import com.app.communicator.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(RegisterUserDto registerUserDto) {

        if (userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new SecurityException("Username already exists");
        }

        if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new SecurityException("Email already exists");
        }

        if(!registerUserDto.getPassword().equals(registerUserDto.getRepeatedPassword())){
            throw new SecurityException("Passwords are not the same");
        }

         var user = UsersMapper.fromRegisterUserToUser(registerUserDto);
         user.setPassword(passwordEncoder.encode(user.getPassword()));
         user.setIsEnabled(false);
         return userRepository
                 .save(user)
                 .getId();
    }

//    public Boolean activateAccount(Long userId){
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new ObjectNotFoundException("user with given id does not exist!"));
//        user.setIsEnabled(true);
//        return user.getIsEnabled();
//    }
}
