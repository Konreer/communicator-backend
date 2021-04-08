package com.app.communicator.controller;

import com.app.communicator.dto.ConversationDto;
import com.app.communicator.dto.MessageDto;
import com.app.communicator.entity.Message;
import com.app.communicator.repository.UserRepository;
import com.app.communicator.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;


//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping(value = "/chat/{userid}/{conversationid}", consumes = "application/json")
//    public List<MessageDto> getConversations(@PathVariable Long userid, @PathVariable Long conversationid, @RequestBody MessageDto message) {
//        return messageService.getMessagesForConversation(userid, conversationid);
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/chat/{userid}/{conversationid}")
    public List<MessageDto> getConversations(@PathVariable Long userid, @PathVariable Long conversationid) {
        List<MessageDto> test = messageService.getMessagesForConversation(userid, conversationid);
        return test;

    }
}
