package com.assessment.phorest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Offerings {

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    @JsonProperty("appointmentDTO")
    private Appointment appointment;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "price", columnDefinition = "INT DEFAULT 0")
    private double price;

    @Column(name = "loyalty_points", columnDefinition = "INT DEFAULT 0")
    private int loyaltyPoints;


}
