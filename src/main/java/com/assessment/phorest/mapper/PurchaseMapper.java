package com.assessment.phorest.mapper;

import com.assessment.phorest.dto.PurchaseDTO;
import com.assessment.phorest.entity.Purchase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper implements GenericMapper<PurchaseDTO, Purchase> {

    private final ObjectMapper objectMapper;

    @Autowired
    public PurchaseMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Purchase mapToEntity(PurchaseDTO purchaseDto) {
        return objectMapper.convertValue(purchaseDto, Purchase.class);
    }

    public PurchaseDTO mapToDTO(Purchase purchase) {
        return objectMapper.convertValue(purchase, PurchaseDTO.class);
    }
}

