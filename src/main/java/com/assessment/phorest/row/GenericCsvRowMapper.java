package com.assessment.phorest.row;

import org.apache.commons.csv.CSVRecord;

public interface GenericCsvRowMapper<DTO> {
    DTO createDTO(CSVRecord csvData);
}
