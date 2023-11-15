package com.assessment.phorest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO implements CsvDataDTO {

    public AppointmentDTO(String id, String clientId, String startTime, String endTime) {
        this.id = id;
        this.clientDTO = new ClientDTO(clientId);
        this.startTime = Objects.equals(startTime, "") ? null : OffsetDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        this.endTime = Objects.equals(endTime, "") ? null : OffsetDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        this.serviceDTO = new ArrayList<>();
        this.purchaseDTO = new ArrayList<>();
    }

    public AppointmentDTO(String id, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public AppointmentDTO(String id) {
        this.id = id;
    }

    @Size(max = 36, message = "Appointment id can't exceed 36 characters")
    @NotEmpty( message = "Appointment id can't be null")
    private String id;

    private ClientDTO clientDTO;

    @NotNull( message = "Start time can't be null or empty")
    private OffsetDateTime startTime;

    @NotNull( message = "End time can't be null or empty")
    private OffsetDateTime endTime;

    @NotNull( message = "Services can't be null")
    private List<ServiceDTO> serviceDTO;

    private List<PurchaseDTO> purchaseDTO;
}

