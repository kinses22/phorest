package com.assessment.phorest.entity;

import com.assessment.phorest.enumeration.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Entity
@Data
@Table(name = "client", indexes = @Index(name = "idx_banned", columnList = "banned"))
public class Client {

    @Id
    @Column(name = "client_id", length = 36, nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "second_name", length = 20)
    private String secondName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 6)
    private Gender gender;

    @Column(name = "banned", length = 5)
    private boolean banned;

}
