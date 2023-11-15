package com.assessment.phorest.service;

import com.assessment.phorest.dto.response.CSVChildProcessingResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CsvFileUploadService {
    CSVChildProcessingResponseDTO processCsvFiles(MultipartFile file);
}
