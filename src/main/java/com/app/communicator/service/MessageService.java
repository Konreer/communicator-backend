package com.app.communicator.service;

import com.app.communicator.dto.MessageDto;
import com.app.communicator.entity.Conversation;
import com.app.communicator.entity.Message;
import com.app.communicator.entity.UserInConversation;
import com.app.communicator.exception.ObjectNotFoundException;
import com.app.communicator.repository.ConversationRepository;
import com.app.communicator.repository.MessageRepository;
import com.app.communicator.repository.UserInConversationRepository;
import com.app.communicator.repository.UserRepository;
import com.app.communicator.security.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserInConversationRepository userInConversationRepository;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<MessageDto> getMessagesForConversation(Long userId, Long conversationId) {
        isIdConsistent(userId);
        if (!userInConversationRepository.findAllByUser_Id(userId).stream().anyMatch(userInConversation -> userInConversation.getId().equals(userId))) {
            throw new IllegalArgumentException("User id: " + userId + " tried to get messages from conversation id: " + conversationId + " which he is not part of");
        }
        return messageRepository.getAllByConversation_Id(conversationId).stream()
                .map(message -> MessageDto.builder()
                        .conversationId(message.getConversation().getId())
                        .date(message.getSendTime())
                        .text(message.getMessage())
                        .avatarUrl(message.getMessageOwner().getAvatarUrl())
                        .ownerId(message.getMessageOwner().getId()).build()).collect(Collectors.toList());
    }

    public Set<UserInConversation> sendMessageToUser(Long userId, Long conversationId, MessageDto messageDto) {
        if (userInConversationRepository.findAllByUser_Id(userId).stream()
                .filter(userInConversation ->
                        userInConversation
                                .getConversation()
                                .getId().equals(conversationId))
                .collect(Collectors.toList())
                .isEmpty()) {
            throw new IllegalArgumentException("User id: " + userId + " tried to send message to conversation id: " + conversationId + " which he is not part of");
        }

        Conversation conversation = conversationRepository
                .findById(messageDto.getConversationId())
                .orElseThrow((() -> new ObjectNotFoundException("Cenversation with id:" + messageDto.getConversationId() + " does not exist")));

        messageRepository.save(Message.builder()
                .message(messageDto.getText())
                .messageOwner(userRepository.findUserById(messageDto.getOwnerId()))
                .sendTime(messageDto.getDate())
                .conversation(conversation)
                .build());

        return conversation.getUserInConversation();

    }


    private boolean isIdConsistent(Long userid) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userid != user.getUserId()) {
            throw new IllegalArgumentException("User Id not consisstent with Id within a token");
        }
        return true;
    }
}
