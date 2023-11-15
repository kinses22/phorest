package com.assessment.phorest.service;

import com.assessment.phorest.dto.response.CSVParentProcessingResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvFileProcessingService {

    CSVParentProcessingResponseDTO uploadCsvFiles(List<MultipartFile> files);
}
