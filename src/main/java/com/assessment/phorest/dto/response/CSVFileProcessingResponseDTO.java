package com.assessment.phorest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class CSVFileProcessingResponseDTO {
    private String fileName;
    private Map<String, List<String>> errors;
}
