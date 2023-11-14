package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.ServiceDTO;
import com.assessment.phorest.parser.GenericCsvParser;
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
            GenericCsvParser<ServiceDTO> genericCsvParser,
            Validator validator) {
        super(genericRepository, serviceMapper, genericCsvParser, validator);
    }
}
