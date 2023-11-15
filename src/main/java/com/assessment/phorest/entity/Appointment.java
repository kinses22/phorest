package com.assessment.phorest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "appointment")
public class Appointment {

    @Id
    @Column(name = "appointment_id", length = 36, nullable = false)
    private UUID id;

    @ManyToOne
    @JsonProperty("clientDTO")
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;

    @JsonProperty("serviceDTO")
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.REFRESH, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Service> services = new ArrayList<>();

    @JsonProperty("purchaseDTO")
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.REFRESH, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

}

