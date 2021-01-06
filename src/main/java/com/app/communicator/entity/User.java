package com.app.communicator.entity;

import lombok.*;

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
    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @OneToMany(mappedBy = "userContact", fetch = FetchType.EAGER)
    private Set<Contact> userContacts;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Conversation> userConversations;
}
