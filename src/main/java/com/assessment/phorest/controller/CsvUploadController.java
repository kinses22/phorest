package com.assessment.phorest.controller;

import com.assessment.phorest.service.implementation.CsvFileProcessingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Csv Upload Controller")
public class CsvUploadController {

    private final CsvFileProcessingService csvFileProcessingService;

    public CsvUploadController(CsvFileProcessingService csvFileProcessingService) {
        this.csvFileProcessingService = csvFileProcessingService;
    }

    @PostMapping( value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsvFile(@RequestPart("file") List<MultipartFile> csvFiles) {
        String result = csvFileProcessingService.processClientCsvFile(csvFiles);
        return ResponseEntity.ok(result);
    }

}
