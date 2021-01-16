package com.app.communicator.controller;

import com.app.communicator.dto.ConversationDto;
import com.app.communicator.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/menu")
public class ConversationController {
    private final ConversationService conversationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userid}")
    public List<ConversationDto> getConversations(@PathVariable Long userid) {
        return conversationService.getConversations(userid);

    }
//    @ResponseStatus(HttpStatus.OK)
//    @DeleteMapping("/{userid}/friends/{friendid}")
//    public void removeFriend(@PathVariable Long userid, @PathVariable Long friendid) {
//        userService.removeFriend(userid,friendid);
//    }
//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping("/{userid}/friends/{friendid}")
//    public void addFriend(@PathVariable Long userid, @PathVariable Long friendid) {
//        userService.addFriend(userid,friendid);
//    }
}
