package com.assessment.phorest.service.implementation;

import com.assessment.phorest.util.FileOrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CsvFileProcessingService {

    private final ClientCsvUploadService clientCsvUploadService;
    private final AppointmentCsvUploadService appointmentCsvUploadService;
    private final PurchaseCsvUploadService purchaseCsvUploadService;

    @Autowired
    public CsvFileProcessingService(ClientCsvUploadService clientCsvUploadService,
                                    AppointmentCsvUploadService appointmentCsvUploadService,
                                    PurchaseCsvUploadService purchaseCsvUploadService) {
        this.clientCsvUploadService = clientCsvUploadService;
        this.appointmentCsvUploadService = appointmentCsvUploadService;
        this.purchaseCsvUploadService = purchaseCsvUploadService;
    }

    public String processClientCsvFile(List<MultipartFile> files) {
        List<MultipartFile> orderFiles = FileOrderUtil.orderFiles(files);

        for (MultipartFile file: orderFiles) {
            clientCsvUploadService.processCsvFiles(file);
        }

        return null;
    }


}
