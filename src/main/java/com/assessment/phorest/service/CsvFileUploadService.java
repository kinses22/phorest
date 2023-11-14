package com.assessment.phorest.service;

import org.springframework.web.multipart.MultipartFile;

public interface CsvFileUploadService {
    String processCsvFiles(MultipartFile file);
}
