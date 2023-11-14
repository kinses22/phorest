package com.assessment.phorest.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CSVBatchProcessingResponseDTO {
    List<String> unSupportedFiles;
    List<CSVFileProcessingResponseDTO> csvFileProcessingResponseDTOList = new ArrayList<>();
}
