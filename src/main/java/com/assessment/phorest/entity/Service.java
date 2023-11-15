package com.assessment.phorest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service")
public class Service {

    @Id
    @Column(name = "service_id", length = 36, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @JsonProperty("appointmentDTO")
    @JsonBackReference
    private Appointment appointment;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "price", columnDefinition = "INT DEFAULT 0")
    private double price;

    @Column(name = "loyalty_points", columnDefinition = "INT DEFAULT 0")
    private int loyaltyPoints;

}
