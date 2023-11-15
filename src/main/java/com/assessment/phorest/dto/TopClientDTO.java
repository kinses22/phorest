package com.assessment.phorest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TopClientDTO {
    private UUID clientId;
    private String firstName;
    private String email;
    private Long totalLoyaltyPoints;
}
