package com.app.communicator.repository;

import com.app.communicator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE CONCAT(user.name, " +
            "user.surname, user.email) LIKE %?1% AND user.isEnabled = true")
    List<User> findAllByKeyword(String keyword);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findUserById(Long userId);

}
