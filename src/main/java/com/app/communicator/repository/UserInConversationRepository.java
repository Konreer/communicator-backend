package com.app.communicator.repository;

import com.app.communicator.entity.UserInConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInConversationRepository extends JpaRepository<UserInConversation, Long> {
    List<UserInConversation> findAllByUser_Id(Long userId);
}
