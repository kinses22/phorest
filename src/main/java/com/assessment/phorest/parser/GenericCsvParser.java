package com.assessment.phorest.parser;

import org.apache.commons.csv.CSVRecord;

public interface GenericCsvParser<DTO> {
    DTO createDTO(CSVRecord csvData);
}
