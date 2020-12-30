package com.app.communicator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToMany(mappedBy = "userContact", fetch = FetchType.EAGER)
    private Set<Contact> userContacts;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Conversation> userConversations;
}