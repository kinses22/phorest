package com.assessment.phorest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppointmentDTO implements CsvDataDTO {

    public AppointmentDTO(String id, String clientId, String startTime, String endTime) {
        this.id = id;
        this.clientDTO = new ClientDTO(clientId);
        this.startTime = ZonedDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        this.endTime = ZonedDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        this.serviceDTO = new ArrayList<>();
        this.purchaseDTO = new ArrayList<>();
    }

    public AppointmentDTO(String id) {
        this.id = id;
    }

    @Size(max = 36, message = "Appointment id can't exceed 36 characters")
    private String id;

    private ClientDTO clientDTO;

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    private ZonedDateTime endTime;

    private List<ServiceDTO> serviceDTO;

    private List<PurchaseDTO> purchaseDTO;

}

