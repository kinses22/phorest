package com.assessment.phorest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ServiceDTO implements CsvDataDTO {

    //todo: can probably inherit from one dto with purchases/services
    public ServiceDTO(String id, String appointment_id, String name, double price, int loyaltyPoints) {
        this.id = id;
        this.appointmentDTO = new AppointmentDTO(appointment_id);
        this.name = name;
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
    }

    @Size(max = 36, message = "Service id can't exceed 36 characters")
    @NotEmpty
    private String id;

    private AppointmentDTO appointmentDTO;

    @Size(max = 40, min = 1, message = "Service name can't exceed 40 characters or be blank")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Service price needs to greater than 0")
    @DecimalMax(value = "1000.00", message = "Service price can't exceed 1000.00")
    private double price;

    @Min(value = 0, message = "Loyalty points can't be less than or equal to 0")
    @Max(value = 100, message = "loyalty points can't be bigger than 100")
    private int loyaltyPoints;

}
