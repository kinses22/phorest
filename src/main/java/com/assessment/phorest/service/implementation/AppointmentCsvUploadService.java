package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.AppointmentDTO;
import com.assessment.phorest.entity.Appointment;
import com.assessment.phorest.dto.csv.GenericCsvDto;
import com.assessment.phorest.mapper.AppointmentMapper;
import com.assessment.phorest.service.generic.GenericCsvUploadService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentCsvUploadService extends GenericCsvUploadService<AppointmentDTO, Appointment> {

    @Autowired
    public AppointmentCsvUploadService(
            GenericRepository<Appointment> genericRepository,
            AppointmentMapper appointmentMapper,
            GenericCsvDto<AppointmentDTO> genericCsvDto,
            Validator validator) {
        super(genericRepository, appointmentMapper, genericCsvDto, validator);
    }
}