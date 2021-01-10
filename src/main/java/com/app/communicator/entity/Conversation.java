package com.app.communicator.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User user;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Message> messages;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<UserInConversation> userInConversation;
}
