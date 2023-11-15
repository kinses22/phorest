package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dto.response.CSVParentProcessingResponseDTO;
import com.assessment.phorest.service.CsvFileProcessingService;
import com.assessment.phorest.service.CsvFileUploadService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.assessment.phorest.util.FileConstants.ALLOWED_FILE_NAMES;
import static com.assessment.phorest.util.FileConstants.DESIRED_ORDER;

@Service
public class CsvFileProcessingServiceImpl implements CsvFileProcessingService {

    @Resource
    private final Map<String, CsvFileUploadService> csvUploadServiceMap;

    public CsvFileProcessingServiceImpl(Map<String, CsvFileUploadService> csvUploadServiceMap) {
        this.csvUploadServiceMap = csvUploadServiceMap;
    }

    public CSVParentProcessingResponseDTO uploadCsvFiles(List<MultipartFile> files) {
        CSVParentProcessingResponseDTO csvParentProcessingResponseDTO = new CSVParentProcessingResponseDTO();
        Map<String, MultipartFile> matchingFilesMap = new HashMap<>();
        List<String> nonMatchingFilesList = new ArrayList<>();

        getSupportedFileNamesAndTypes(files, matchingFilesMap, nonMatchingFilesList);
        csvParentProcessingResponseDTO.setUnSupportedFiles(nonMatchingFilesList);
        for (String orderedFileName : DESIRED_ORDER) {
            MultipartFile file = matchingFilesMap.get(orderedFileName);
            if (file != null) {
                CsvFileUploadService csvFileUploadService = csvUploadServiceMap.get(file.getOriginalFilename());
                if (csvFileUploadService != null) {
                    csvParentProcessingResponseDTO
                            .getCsvChildProcessingResponseDTOList().add(csvFileUploadService.processCsvFiles(file));
                }

            }
        }
        return csvParentProcessingResponseDTO;
    }

    private void getSupportedFileNamesAndTypes(List<MultipartFile> files, Map<String, MultipartFile> matchingFilesMap, List<String> nonMatchingFilesList) {
        for (MultipartFile file : files) {
            String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
            if (ALLOWED_FILE_NAMES.contains(fileName)) {
                matchingFilesMap.put(fileName, file);
            } else {
                nonMatchingFilesList.add(fileName);
            }
        }
    }
}
