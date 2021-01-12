package com.app.communicator.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;


    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "owner_id")
    @EqualsAndHashCode.Exclude
    private User messageOwner;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "conv_id")
    @EqualsAndHashCode.Exclude
    private Conversation conversation;
}
