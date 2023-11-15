package com.assessment.phorest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppointmentDTO implements CsvDataDTO {

    public AppointmentDTO(String id, String clientId, String startTime, String endTime) {
        this.id = id;
        this.clientDTO = new ClientDTO(clientId);
        this.startTime = OffsetDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        this.endTime = OffsetDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        this.serviceDTO = new ArrayList<>();
        this.purchaseDTO = new ArrayList<>();
    }

    public AppointmentDTO(String id) {
        this.id = id;
    }

    @Size(max = 36, message = "Appointment id can't exceed 36 characters")
    @NotEmpty
    private String id;

    @JsonIgnore
    private ClientDTO clientDTO;

    @NotNull
    private OffsetDateTime startTime;

    @NotNull
    private OffsetDateTime endTime;

    private List<ServiceDTO> serviceDTO;

    private List<PurchaseDTO> purchaseDTO;

}

