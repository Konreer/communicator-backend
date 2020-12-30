package com.app.communicator.repository;

import com.app.communicator.entity.UserInConversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInConversationRepository extends JpaRepository<UserInConversation, Long> {
}
