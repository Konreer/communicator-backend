package com.app.communicator.controller;

import com.app.communicator.dto.MessageDto;
import com.app.communicator.entity.UserInConversation;
import com.app.communicator.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@AllArgsConstructor
public class MessageSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/message")
    public void greeting(MessageDto messageDto) {
        Set<UserInConversation> users =  messageService.sendMessageToUser(messageDto.getOwnerId(), messageDto.getConversationId(), messageDto);
        for (UserInConversation user : users)
        {
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(user.getUser().getId()), "/messages", messageDto);
        }
    }
}
