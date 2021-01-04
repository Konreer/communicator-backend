package com.app.communicator.service;

import com.app.communicator.dto.usersDto.UserDataDto;
import com.app.communicator.entity.Contact;
import com.app.communicator.entity.User;
import com.app.communicator.repository.ContactRepository;
import com.app.communicator.repository.UserRepository;
import com.app.communicator.security.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public boolean isIdConsistent(Long userid) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userid.equals(user.getUserId())) {
            throw new IllegalArgumentException("User Id not consistent with Id within a token");
        }
        return true;
    }

    public List<UserDataDto> getUserContacts(Long userId) {
        isIdConsistent(userId);

        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No user with such id")).getUserContacts()
                .stream()
                .map(contact -> UserDataDto.builder()
                        .id(contact.getUserContact().getId())
                        .surname(contact.getUserContact().getSurname())
                        .name(contact.getUserContact().getName())
                        .avatarUrl(contact.getUserContact().getAvatarUrl())
                        .build())
                .collect(Collectors.toList());

    }

    public void removeFriend(Long userId, Long friendId) {
        isIdConsistent(userId);
        contactRepository.delete(contactRepository
                .findByUser_IdAndUserContact_Id(userId, friendId)
                .orElseThrow(() -> new IllegalArgumentException("Users are not friends")));
        contactRepository.delete(contactRepository
                .findByUser_IdAndUserContact_Id(friendId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Users are not friends")));
    }

    public void addFriend(Long userId, Long friendId) {
        isIdConsistent(userId);
        User optionalFriend = userRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("No user with such id"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No user with such id"));

        contactRepository.save(Contact
                .builder()
                .user(user)
                .userContact(optionalFriend)
                .build());

    }

    public List<UserDataDto> getUsersByKeyword(String keyWord) {
        return userRepository.findAllByKeyword(keyWord)
                .stream()
                .map(user -> UserDataDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .avatarUrl(user.getAvatarUrl())
                        .build())
                .collect(Collectors.toList());

    }
}