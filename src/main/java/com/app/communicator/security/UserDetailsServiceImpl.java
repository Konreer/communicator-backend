package com.app.communicator.security;

import com.app.communicator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@Qualifier("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(userFromDb -> new CustomUser(
                        userFromDb.getId(),
                        userFromDb.getUsername(),
                        userFromDb.getPassword(),
                        userFromDb.getIsEnabled(), true, true, true,
                        Collections.emptyList()
                )).orElseThrow(() -> new SecurityException("cannot find user with username " + username));
    }
}
