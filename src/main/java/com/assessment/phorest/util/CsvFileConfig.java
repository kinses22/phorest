package com.assessment.phorest.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CsvFileConfig {
    private String dtoType;
    private String[] headers;
}
