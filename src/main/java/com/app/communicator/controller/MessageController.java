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


    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/chat/{userid}/{conversationid}", consumes = "application/json")
    public List<MessageDto> getConversations(@PathVariable Long userid, @PathVariable Long conversationid, @RequestBody MessageDto message) {
        return messageService.getMessagesForConversation(userid, conversationid);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/chat/{userid}/{conversationid}")
    public List<MessageDto> getConversations(@PathVariable Long userid, @PathVariable Long conversationid) {
        return messageService.getMessagesForConversation(userid, conversationid);

    }

    @MessageMapping({"/message"})
    public void greeting(String message) {
        System.out.println("ODEBRALEM PENIS");
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(3), "/msg", ConversationDto.builder().id(1L).lastMessage("penis").avatarUrl("https://ocdn.eu/pulscms-transforms/1/LFmk9kuTURBXy9hYTdjMTMzMS0zMmVlLTQxN2YtODFiMS01YTM0NjI0YjhkMDkuanBlZ5GVAs0BigDDw4GhMAE").conversationName("nie wiem").build());
    }
}
