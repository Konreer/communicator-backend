package com.app.communicator.repository;

import com.app.communicator.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT CASE WHEN COUNT(contact)>0 THEN TRUE ELSE FALSE END FROM Contact contact WHERE (contact.user.id = ?1 AND contact.userContact.id = ?2)" +
            " OR (contact.user.id = ?2 AND contact.userContact.id = ?1)")
    Boolean existsByIds(Long userId, Long userContactId);

    @Query("SELECT contact FROM Contact contact WHERE (contact.user.id = ?1 AND contact.userContact.id = ?2)" +
            " OR (contact.user.id = ?2 AND contact.userContact.id = ?1)")
    Optional<Contact> findByUserIdAndContactId(Long userId, Long userContactId);

    List<Contact> findAllByUserContact_IdAndIsAcceptedFalse(Long userId);
    Optional<Contact> findByUser_IdAndUserContact_Id(Long userid, Long usercontactid);
}
