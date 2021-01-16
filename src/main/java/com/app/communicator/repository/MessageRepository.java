package com.app.communicator.repository;

import com.app.communicator.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getAllByConversation_Id(Long conversationId);
}
