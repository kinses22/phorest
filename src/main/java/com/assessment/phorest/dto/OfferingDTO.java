package com.assessment.phorest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public abstract class OfferingDTO implements CsvDataDTO {

    public OfferingDTO(String id, String appointment_id, String name, double price, int loyaltyPoints) {
        this.id = id;
        this.appointmentDTO = new AppointmentDTO(appointment_id);
        this.name = name;
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
    }

    @Size(max = 36, message = "Purchase id can't exceed 36 characters")
    private String id;

    private AppointmentDTO appointmentDTO;

    @Size(max = 20, min = 1, message = "Purchase name can't exceed 36 characters or be blank")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Purchase price needs to greater than 0")
    @DecimalMax(value = "1000.00", message = "Purchase price can't exceed 1000.00")
    private double price;

    @Min(value = 0, message = "Loyalty points can't be less than or equal to 0")
    @Max(value = 100, message = "loyalty points can't be bigger than 100")
    private int loyaltyPoints;
}
