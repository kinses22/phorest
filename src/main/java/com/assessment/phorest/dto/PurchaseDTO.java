package com.assessment.phorest.dto;

public class PurchaseDTO extends OfferingDTO implements CsvDataDTO {

    public PurchaseDTO(String id, String appointment_id, String name, double price, int loyaltyPoints) {
        super(id, appointment_id, name, price, loyaltyPoints);
    }
}
