package com.assessment.phorest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "service")
public class Service extends Offerings {

    @Id
    @Column(name = "service_id", length = 36, nullable = false)
    private UUID id;

}
