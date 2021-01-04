package com.app.communicator.service;

import com.app.communicator.dto.ContactDataDto;
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
        if (userid != user.getUserId()) {
            throw new IllegalArgumentException("User Id not consisstent with Id within a token");
        }
        return true;
    }

    public List<ContactDataDto> getUserContacts(Long userId) {
        isIdConsistent(userId);

        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No user with such id")).getUserContacts()
                .stream()
                .map(contact -> ContactDataDto.builder()
                        .contactId(contact.getUserContact().getId())
                        .contactSurname(contact.getUserContact().getSurname())
                        .contactName(contact.getUserContact().getName())
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

}
