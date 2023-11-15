package com.assessment.phorest.service.generic;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.response.CSVFileProcessingResponseDTO;
import com.assessment.phorest.enumeration.Status;
import com.assessment.phorest.mapper.GenericMapper;
import com.assessment.phorest.row.GenericCsvRowMapper;
import com.assessment.phorest.util.CsvConfig;
import com.assessment.phorest.util.CsvFileConfig;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.assessment.phorest.util.EntityUtil.getId;

@Slf4j
public abstract class GenericCsvUploadService<DTO, Entity> {

    private final GenericRepository<Entity> genericRepository;
    private final GenericMapper<DTO, Entity> mapper;
    private final GenericCsvRowMapper<DTO> genericCsvRowMapper;
    private final Validator validator;

    @Autowired
    public GenericCsvUploadService(GenericRepository<Entity> genericRepository, GenericMapper<DTO, Entity> mapper,
                                   GenericCsvRowMapper<DTO> genericCsvRowMapper, Validator validator) {
        this.genericRepository = genericRepository;
        this.mapper = mapper;
        this.genericCsvRowMapper = genericCsvRowMapper;
        this.validator = validator;
    }

    public CSVFileProcessingResponseDTO processCsvFiles(MultipartFile file) {
        Map<String, List<String>> validationErrors = new HashMap<>();
        String fileName = file.getOriginalFilename();
        CsvFileConfig csvFileConfig = CsvConfig.getConfigForFile(fileName);
        List<DTO> dTOList = parseCsvFile(file, csvFileConfig, validationErrors);
        saveEntities(dTOList, csvFileConfig.getDtoType(), validationErrors);
        Status status = getUploadStatus(validationErrors);
        return new CSVFileProcessingResponseDTO(fileName, validationErrors, status);

    }

    private List<DTO> parseCsvFile(MultipartFile file, CsvFileConfig csvFileConfig, Map<String, List<String>> validationErrors) {
        List<DTO> dTOList = new ArrayList<>();

        try (CSVParser csvParser = createCsvParser(file, csvFileConfig)) {
            for (CSVRecord csvRecord : csvParser) {
                processCsvRecord(csvRecord, dTOList, validationErrors);
            }
        } catch (IOException | IllegalArgumentException e) {
            log.info("There was an issue parsing the csv file:{}, {}, {}", file, e.getMessage(), e);
            throw new IllegalArgumentException("There was an issue parsing the csv file: " + file + " " + e.getMessage());
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

    private void processCsvRecord(CSVRecord csvRecord, List<DTO> dTOList, Map<String, List<String>> validationErrors) {
        try {
            DTO dto = genericCsvRowMapper.createDTO(csvRecord);
            Set<ConstraintViolation<DTO>> violations = validator.validate(dto);
            if (violations.isEmpty()) {
                dTOList.add(dto);
            } else {
                List<String> violationErrors = new ArrayList<>();
                for (ConstraintViolation<DTO> violation : violations) {
                    violationErrors.add(violation.getMessage());
                }
                validationErrors.put(csvRecord.get("id").equals("") ? "ID field":
                                csvRecord.get("id"), violationErrors);
            }
        } catch (IllegalArgumentException e) {
            validationErrors.put(csvRecord.get("id").equals("") ? "ID field" : csvRecord.get("id")
                    , List.of(e.getMessage()));
        }
    }

    private void saveEntities(List<DTO> dTOList, String dTO, Map<String, List<String>> validationErrors) {
        List<Entity> entityList = new ArrayList<>();
        dTOList.forEach(dto -> entityList.add(mapper.mapToEntity(dto)));
        AtomicReference<UUID> entityId = new AtomicReference<>();
        try {
            entityList.forEach( entity -> {
                entityId.set(getId(entity));
                    genericRepository.save(entity);
                    }
                    );
        } catch (ConstraintViolationException | EntityNotFoundException | DataIntegrityViolationException e) {
            log.error("Sql Constraint error for entity: {} and id: {} , {}", dTO, entityId, e.getMessage());
            validationErrors.put(entityId.toString(), List.of("Sql constraint error"));
        } catch (Exception e) {
            log.error("Sql error for entity: {} and id: {} , {}", dTO, entityId, e.getMessage());
            validationErrors.put(entityId.toString(), List.of(e.getCause().toString()));
        }
    }

    private Status getUploadStatus(Map<String, List<String>> validationErrors) {
        Status status = Status.PROCESSED;
        if (!validationErrors.isEmpty()){
            status = Status.PARTIALLY_PROCESSED;
        }
        return status;
    }
}
