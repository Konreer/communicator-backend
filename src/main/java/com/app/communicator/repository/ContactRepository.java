package com.app.communicator.repository;

import com.app.communicator.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByUser_IdAndUserContact_Id(Long userid, Long usercontactid);
}
