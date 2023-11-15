package com.assessment.phorest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "purchase")
public class Purchase {

    @Id
    @Column(name = "purchase_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @JsonProperty("appointmentDTO")
    private Appointment appointment;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "price", columnDefinition = "INT DEFAULT 0")
    private double price;

    @Column(name = "loyalty_points", columnDefinition = "INT DEFAULT 0")
    private int loyaltyPoints;

}
