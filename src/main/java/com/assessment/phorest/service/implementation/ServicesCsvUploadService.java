package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.PurchaseDTO;
import com.assessment.phorest.dto.ServiceDTO;
import com.assessment.phorest.dto.csv.GenericCsvDto;
import com.assessment.phorest.entity.Purchase;
import com.assessment.phorest.mapper.PurchaseMapper;
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
            GenericCsvDto<ServiceDTO> genericCsvDto,
            Validator validator) {
        super(genericRepository, serviceMapper, genericCsvDto, validator);
    }
}
