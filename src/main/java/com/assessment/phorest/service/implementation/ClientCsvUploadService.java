package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.entity.Client;
import com.assessment.phorest.dto.csv.GenericCsvDto;
import com.assessment.phorest.mapper.ClientMapper;
import com.assessment.phorest.service.generic.GenericCsvUploadService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientCsvUploadService extends GenericCsvUploadService<ClientDTO, Client> {

    @Autowired
    public ClientCsvUploadService(
            GenericRepository<Client> genericRepository,
            ClientMapper clientMapper,
            GenericCsvDto<ClientDTO> genericCsvDto,
            Validator validator) {
        super(genericRepository, clientMapper, genericCsvDto, validator);
    }
}
