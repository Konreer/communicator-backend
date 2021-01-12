package com.app.communicator.service;

import com.app.communicator.dto.usersDto.UserDataDto;
import com.app.communicator.entity.Contact;
import com.app.communicator.entity.User;
import com.app.communicator.exception.ObjectNotFoundException;
import com.app.communicator.repository.ContactRepository;
import com.app.communicator.repository.UserRepository;
import com.app.communicator.security.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public boolean isIdConsistent(Long userid) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userid.equals(user.getUserId())) {
            throw new IllegalArgumentException("User Id not consistent with Id within a token");
        }

        System.out.println(user);
        return true;
    }

    public List<UserDataDto> getUserContacts(Long userId) {
        isIdConsistent(userId);

        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("No user with such id")).getUserContacts()
                .stream()
                .filter(contact -> contact.getIsAccepted().equals(true))
                .map(contact -> UserDataDto.builder()
                        .id(contact.getUserContact().getId())
                        .surname(contact.getUserContact().getSurname())
                        .name(contact.getUserContact().getName())
                        .avatarUrl(contact.getUserContact().getAvatarUrl())
                        .build())
                .collect(Collectors.toList());

    }

    public List<UserDataDto> getUserInvitations(Long userId) {
        isIdConsistent(userId);

        return contactRepository.findAllByUserContact_IdAndIsAcceptedFalse(userId)
                .stream()
                .map(contact -> UserDataDto.builder()
                        .id(contact.getUser().getId())
                        .surname(contact.getUser().getSurname())
                        .name(contact.getUser().getName())
                        .avatarUrl(contact.getUser().getAvatarUrl())
                        .build())
                .collect(Collectors.toList());
    }

    public Long acceptInvitation(Long userId, Long friendId){
        isIdConsistent(userId);
        Contact contact = contactRepository.findByUserIdAndContactId(userId, friendId)
                .orElseThrow(() -> new ObjectNotFoundException("Contact with given ids does not exist!"));
        contact.setIsAccepted(true);
        return contact.getId();
    }

    public void removeFriend(Long userId, Long friendId) {
        isIdConsistent(userId);
        contactRepository.delete(contactRepository.findByUserIdAndContactId(userId, friendId)
                .orElseThrow(() -> new ObjectNotFoundException("Users with given ids are not friends!")));
    }

    public UserDataDto addFriend(Long userId, Long friendId) {
        isIdConsistent(userId);

        if(contactRepository.existsByIds(userId, friendId)){
            throw new IllegalArgumentException("Contact already exists!");
        }

        if(userId.equals(friendId)){
            throw new IllegalArgumentException("User cannot add to contact list himself!");
        }

        User optionalFriend = userRepository.findById(friendId).orElseThrow(() -> new ObjectNotFoundException("No user with such id"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("No user with such id"));

        contactRepository.save(Contact
                .builder()
                .user(user)
                .userContact(optionalFriend)
                .isAccepted(false)
                .build());

        return UserDataDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .avatarUrl(user.getAvatarUrl())
                .build();
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