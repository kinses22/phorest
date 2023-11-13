package com.assessment.phorest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "purchase")
public class Purchase extends Offering {

    @Id
    @Column(name = "purchase_id", nullable = false)
    private UUID id;

}
