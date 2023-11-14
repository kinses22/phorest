package com.assessment.phorest.service.generic;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.response.CSVFileProcessingResponseDTO;
import com.assessment.phorest.parser.GenericCsvParser;
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
import java.util.*;

@Slf4j
public abstract class GenericCsvUploadService<DTO, Entity> {

    private final GenericRepository<Entity> genericRepository;
    private final GenericMapper<DTO, Entity> mapper;
    private final GenericCsvParser<DTO> genericCsvParser;
    private final Validator validator;

    @Autowired
    public GenericCsvUploadService(GenericRepository<Entity> genericRepository, GenericMapper<DTO, Entity> mapper,
                                   GenericCsvParser<DTO> genericCsvParser, Validator validator) {
        this.genericRepository = genericRepository;
        this.mapper = mapper;
        this.genericCsvParser = genericCsvParser;
        this.validator = validator;
    }

    //todo: bring back a model - errors etc etc
    public CSVFileProcessingResponseDTO processCsvFiles(MultipartFile file) {
        Map<String, List<String>> errors = new HashMap<>();
        String fileName = file.getOriginalFilename();
        // todo: handle no file as we get 500 back
        CsvFileConfig csvFileConfig = CsvConfig.getConfigForFile(fileName);
        List<DTO> dTOList = parseCsvFile(file, csvFileConfig, errors);
        saveEntities(dTOList);
        // todo: return model with errors, status etc
        return new CSVFileProcessingResponseDTO(fileName, errors);

    }

    private List<DTO> parseCsvFile(MultipartFile file, CsvFileConfig csvFileConfig, Map<String, List<String>> errors) {
        List<DTO> dTOList = new ArrayList<>();

        try (CSVParser csvParser = createCsvParser(file, csvFileConfig)) {
            for (CSVRecord csvRecord : csvParser) {
                processCsvRecord(csvRecord, dTOList, errors);
            }
        } catch (IOException | IllegalArgumentException e) {
            log.info("There was an issue parsing the csv file: {}, {}", e.getMessage(), e);
            // THROW HERE AS ISSUE WITH PARSING
        }
        return dTOList;
    }

    private CSVParser createCsvParser(MultipartFile file, CsvFileConfig csvFileConfig) throws IOException {
        return CSVFormat.DEFAULT.builder()
                .setHeader(csvFileConfig.getHeaders())
                .setSkipHeaderRecord(true)
                .build()
                .parse(new InputStreamReader(file.getInputStream()));
    }

    private void processCsvRecord(CSVRecord csvRecord, List<DTO> dTOList, Map<String, List<String>> errors) {
        try {
            DTO dto = genericCsvParser.createDTO(csvRecord);
            Set<ConstraintViolation<DTO>> violations = validator.validate(dto);
            if (violations.isEmpty()) {
                dTOList.add(dto);
            } else {
                List<String> validationErrors = new ArrayList<>();
                for (ConstraintViolation<DTO> violation : violations) {
                    validationErrors.add(violation.getMessage());
                }
                errors.put(csvRecord.get("id"), validationErrors);
            }
        } catch (IllegalArgumentException e) {
            errors.put(csvRecord.get("id"), List.of(e.getMessage()));
        }
    }

    private void saveEntities(List<DTO> dTOList) {
        List<Entity> entityList = new ArrayList<>();
        dTOList.forEach(dto -> entityList.add(mapper.mapToEntity(dto)));
        genericRepository.saveAll(entityList);
    }


}
