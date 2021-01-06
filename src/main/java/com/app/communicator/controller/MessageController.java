package com.app.communicator.controller;

import com.app.communicator.dto.MessageDto;
import com.app.communicator.entity.Message;
import com.app.communicator.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

//    @MessageMapping("/queue/messages/{recipientid}")
//    public void sendMessage(@DestinationVariable Long recipientid, @Payload MessageDto message){
//        simpMessagingTemplate.convertAndSend("/queue/"+ recipientid, message);
//    }

//    @MessageMapping("/register")
//    @SendToUser("/queue/newMember")
//    public Set<String> registerUser(int webChatUsername){
//        if(!connectedUsers.contains(webChatUsername)) {
//            connectedUsers.add(webChatUsername);
//            simpMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername);
//            return connectedUsers;
//        } else {
//            return new HashSet<>();
//        }
//    }

//    @MessageMapping("/unregister")
//    @SendTo("/topic/disconnectedUser")
//    public String unregisterUser(String webChatUsername){
//        connectedUsers.remove(webChatUsername);
//        return webChatUsername;
//    }

    @MessageMapping("/message")
    public void greeting(String message){
        System.out.println("ODEBRALEM PENIS");
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(3), "/msg", message);
    }
}
