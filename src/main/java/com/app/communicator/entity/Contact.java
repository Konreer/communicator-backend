package com.app.communicator.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "his_contact")
    @EqualsAndHashCode.Exclude
    private User userContact;

}
