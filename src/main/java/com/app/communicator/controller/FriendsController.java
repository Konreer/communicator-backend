package com.app.communicator.controller;

import com.app.communicator.dto.usersDto.UserDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.app.communicator.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FriendsController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/friends")
    public List<UserDataDto> getFriends(@PathVariable Long userId) {
        return userService.getUserContacts(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/invitations")
    public List<UserDataDto> getInvitations(@PathVariable Long userId) {
        return userService.getUserInvitations(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}/invitations/{friendId}")
    public Long acceptInvitation(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.acceptInvitation(userId, friendId);
    }

    @GetMapping("/{keyWord}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDataDto> getUsersByKeyword(@PathVariable String keyWord) {
        return userService.getUsersByKeyword(keyWord);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
    }
}
