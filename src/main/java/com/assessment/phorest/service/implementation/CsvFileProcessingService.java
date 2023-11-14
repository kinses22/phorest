package com.assessment.phorest.service.implementation;

import com.assessment.phorest.configuration.CsvUploadServiceConfig;
import com.assessment.phorest.service.CsvFileUploadService;
import com.assessment.phorest.util.FileOrderUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class CsvFileProcessingService {

    @Resource
    private final Map<String, CsvFileUploadService> csvUploadServiceMap;

    public CsvFileProcessingService(Map<String, CsvFileUploadService> csvUploadServiceMap) {
        this.csvUploadServiceMap = csvUploadServiceMap;
    }

    public String processClientCsvFile(List<MultipartFile> files) {
        List<MultipartFile> orderFiles = FileOrderUtil.orderFiles(files);

        for (MultipartFile file: orderFiles) {
            String fileName = file.getOriginalFilename();
            CsvFileUploadService csvFileUploadService = csvUploadServiceMap.get(fileName);
            csvFileUploadService.processCsvFiles(file);
        }

        return "Hi";
    }


}
