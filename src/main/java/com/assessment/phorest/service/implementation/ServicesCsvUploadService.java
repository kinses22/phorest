package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.ServiceDTO;
import com.assessment.phorest.row.GenericCsvRowMapper;
import com.assessment.phorest.mapper.ServiceMapper;
import com.assessment.phorest.service.CsvFileUploadService;
import com.assessment.phorest.service.generic.GenericCsvUploadService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesCsvUploadService extends GenericCsvUploadService<ServiceDTO, com.assessment.phorest.entity.Service> implements CsvFileUploadService {

    @Autowired
    public ServicesCsvUploadService(
            GenericRepository<com.assessment.phorest.entity.Service> genericRepository,
            ServiceMapper serviceMapper,
            GenericCsvRowMapper<ServiceDTO> genericCsvRowMapper,
            Validator validator) {
        super(genericRepository, serviceMapper, genericCsvRowMapper, validator);
    }
}
