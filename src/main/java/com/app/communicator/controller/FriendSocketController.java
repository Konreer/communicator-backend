package com.app.communicator.controller;

import com.app.communicator.dto.usersDto.UserDataDto;
import com.app.communicator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class FriendSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

    @MessageMapping("/{userId}/friends/{friendId}")
    public void addFriend(@DestinationVariable Long userId, @DestinationVariable Long friendId, Principal principal) {
        System.out.println(principal.getName());
        UserDataDto user = userService.addFriend(userId, friendId);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(friendId), "/invitations", user);
    }

}
