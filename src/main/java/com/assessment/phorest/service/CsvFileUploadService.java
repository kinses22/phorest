package com.assessment.phorest.service;

import com.assessment.phorest.dto.response.CSVFileProcessingResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CsvFileUploadService {
    CSVFileProcessingResponseDTO processCsvFiles(MultipartFile file);
}
