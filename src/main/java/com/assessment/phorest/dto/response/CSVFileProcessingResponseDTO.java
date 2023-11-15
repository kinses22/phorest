package com.assessment.phorest.dto.response;

import com.assessment.phorest.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class CSVFileProcessingResponseDTO {
    private String fileName;
    private Map<String, List<String>> validationErrors;
    private Status status;
}
