package com.assessment.phorest.dto;

public class ServiceDTO extends OfferingDTO implements CsvDataDTO {

    public ServiceDTO(String id, String appointment_id, String name, double price, int loyaltyPoints) {
        super(id, appointment_id, name, price, loyaltyPoints);
    }
}
