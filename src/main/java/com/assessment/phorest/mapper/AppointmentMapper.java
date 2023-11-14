package com.assessment.phorest.mapper;

import com.assessment.phorest.dto.AppointmentDTO;
import com.assessment.phorest.entity.Appointment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper implements GenericMapper<AppointmentDTO, Appointment> {

    private final ObjectMapper objectMapper;

    @Autowired
    public AppointmentMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Appointment mapToEntity(AppointmentDTO appointmentDto) {
        return objectMapper.convertValue(appointmentDto, Appointment.class);
    }
}

