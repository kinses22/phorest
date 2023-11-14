package com.assessment.phorest.dto.csv;

import org.apache.commons.csv.CSVRecord;

public interface GenericCsvDto<DTO> {
    DTO createDTO(CSVRecord csvData);
}
