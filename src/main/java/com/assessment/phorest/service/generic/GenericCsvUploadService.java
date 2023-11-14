package com.assessment.phorest.service.generic;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.csv.GenericCsvDto;
import com.assessment.phorest.mapper.GenericMapper;
import com.assessment.phorest.util.CsvConfig;
import com.assessment.phorest.util.CsvFileConfig;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class GenericCsvUploadService<DTO, Entity> {

    private final GenericRepository<Entity> genericRepository;
    private final GenericMapper<DTO, Entity> mapper;
    private final GenericCsvDto<DTO> genericCsvDto;
    private final Validator validator;

    @Autowired
    public GenericCsvUploadService(GenericRepository<Entity> genericRepository, GenericMapper<DTO, Entity> mapper,
                                   GenericCsvDto<DTO> genericCsvDto, Validator validator) {
        this.genericRepository = genericRepository;
        this.mapper = mapper;
        this.genericCsvDto = genericCsvDto;
        this.validator = validator;
    }

    //todo: bring back a model - errors etc etc
    public String processCsvFiles(MultipartFile file) {

        // todo: errors -
        List<String> errors = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        // todo: handle no file as we get 500 back
        CsvFileConfig csvFileConfig = CsvConfig.getConfigForFile(fileName);

        List<DTO> dTOList = new ArrayList<>();
        try (CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader(csvFileConfig.getHeaders()).setSkipHeaderRecord(true)
                .build().parse(new InputStreamReader(file.getInputStream()))) {
            for (CSVRecord csvRecord : csvParser) {
                try {
                    DTO dto = genericCsvDto.createDTO(csvRecord);
                    Set<ConstraintViolation<DTO>> violations = validator.validate(dto);
                    if (violations.isEmpty()) {
                        dTOList.add(dto);
                    }
                } catch (IllegalArgumentException e) {
                    errors.add(csvRecord.get("id"));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            log.info("There was an issue parsing the csv file: {}, {}", e.getMessage(), e);
            // THROW HERE AS ISSUE WITH PARSING
        }

        //todo: factory pattern & iterate over all and save individually as FK might not exist

        List<Entity> entityList = new ArrayList<>();
        dTOList.forEach(dto -> entityList.add(mapper.mapToEntity(dto)));
        genericRepository.saveAll(entityList);

        // todo: return model with errors, status etc
        return "hi";

    }


}
