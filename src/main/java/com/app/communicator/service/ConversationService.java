package com.app.communicator.service;

import com.app.communicator.dto.ContactDataDto;
import com.app.communicator.dto.ConversationDto;
import com.app.communicator.entity.*;
import com.app.communicator.repository.ContactRepository;
import com.app.communicator.repository.ConversationRepository;
import com.app.communicator.repository.UserInConversationRepository;
import com.app.communicator.repository.UserRepository;
import com.app.communicator.security.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConversationService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final UserInConversationRepository userInConversationRepository;

    public List<ConversationDto> getConversations(Long userId) {
        //isIdConsistent(userId);
        return userInConversationRepository.findAllByUser_Id(userId).stream().map(
                userInConversation -> ConversationDto
                        .builder()
                        .id(userInConversation.getConversation().getId())
                        .conversationName(generateConversationName(userInConversation.getConversation(), userId))
                        .lastMessage(getLatestMessage(userInConversation.getConversation())).build())
                .collect(Collectors.toList());
    }

    private String getLatestMessage(Conversation conversation) {
        return conversation.getMessages().stream().min(Comparator.comparing(Message::getSendTime))
                .get()
                .getMessage();
    }

    private String generateConversationName(Conversation conversation, Long userId) {
        if (conversation.getUserInConversation().size() == 2) {
            for (UserInConversation userInConversation : conversation.getUserInConversation()) {
                if (!userInConversation.getUser().getId().equals(userId)) return userInConversation.getUser().getName() + ' ' + userInConversation.getUser().getSurname();
            }
        }
        return "";
    }


    private boolean isIdConsistent(Long userid) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userid != user.getUserId()) {
            throw new IllegalArgumentException("User Id not consisstent with Id within a token");
        }
        return true;
    }

}
