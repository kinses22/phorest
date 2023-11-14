package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.PurchaseDTO;
import com.assessment.phorest.entity.Purchase;
import com.assessment.phorest.dto.csv.GenericCsvDto;
import com.assessment.phorest.mapper.PurchaseMapper;
import com.assessment.phorest.service.CsvFileUploadService;
import com.assessment.phorest.service.generic.GenericCsvUploadService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseCsvUploadService extends GenericCsvUploadService<PurchaseDTO, Purchase> implements CsvFileUploadService {

    @Autowired
    public PurchaseCsvUploadService(
            GenericRepository<Purchase> genericRepository,
            PurchaseMapper purchaseMapper,
            GenericCsvDto<PurchaseDTO> genericCsvDto,
            Validator validator) {
        super(genericRepository, purchaseMapper, genericCsvDto, validator);
    }
}
