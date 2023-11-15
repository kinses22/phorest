package com.assessment.phorest.mapper;

import com.assessment.phorest.dto.ServiceDTO;
import com.assessment.phorest.entity.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper implements GenericMapper<ServiceDTO, Service>{

    private final ObjectMapper objectMapper;

    @Autowired
    public ServiceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Service mapToEntity(ServiceDTO serviceDto) {
        return objectMapper.convertValue(serviceDto, Service.class);
    }

    @Override
    public ServiceDTO mapToDTO(Service service) {
        return objectMapper.convertValue(service, ServiceDTO.class);
    }
}

