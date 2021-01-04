package com.app.communicator.controller;

import com.app.communicator.dto.ContactDataDto;
import com.app.communicator.dto.securityDto.RefreshTokenDto;
import com.app.communicator.dto.securityDto.TokensDto;
import com.app.communicator.entity.User;
import com.app.communicator.security.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.app.communicator.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FriendsController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userid}/friends")
    public List<ContactDataDto> getFriends(@PathVariable Long userid) {
        return userService.getUserContacts(userid);

    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userid}/friends/{friendid}")
    public void removeFriend(@PathVariable Long userid, @PathVariable Long friendid) {
        userService.removeFriend(userid,friendid);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userid}/friends/{friendid}")
    public void addFriend(@PathVariable Long userid, @PathVariable Long friendid) {
        userService.addFriend(userid,friendid);
    }
}
