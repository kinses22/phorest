package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.entity.Client;
import com.assessment.phorest.parser.GenericCsvParser;
import com.assessment.phorest.mapper.ClientMapper;
import com.assessment.phorest.service.CsvFileUploadService;
import com.assessment.phorest.service.generic.GenericCsvUploadService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientCsvUploadService extends GenericCsvUploadService<ClientDTO, Client> implements CsvFileUploadService {

    @Autowired
    public ClientCsvUploadService(
            GenericRepository<Client> genericRepository,
            ClientMapper clientMapper,
            GenericCsvParser<ClientDTO> genericCsvParser,
            Validator validator) {
        super(genericRepository, clientMapper, genericCsvParser, validator);
    }
}
