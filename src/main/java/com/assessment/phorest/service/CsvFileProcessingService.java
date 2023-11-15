package com.assessment.phorest.service;

import com.assessment.phorest.dto.response.CSVBatchProcessingResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvFileProcessingService {

    CSVBatchProcessingResponseDTO processCsvFiles(List<MultipartFile> files);
}
