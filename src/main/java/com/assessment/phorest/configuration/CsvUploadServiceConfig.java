package com.assessment.phorest.configuration;

import com.assessment.phorest.service.CsvFileUploadService;
import com.assessment.phorest.service.implementation.AppointmentCsvUploadService;
import com.assessment.phorest.service.implementation.ClientCsvUploadService;
import com.assessment.phorest.service.implementation.PurchaseCsvUploadService;
import com.assessment.phorest.service.implementation.ServicesCsvUploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CsvUploadServiceConfig {

    @Bean
    public Map<String, CsvFileUploadService> csvUploadServiceMap(
            ClientCsvUploadService clientCsvUploadService,
            AppointmentCsvUploadService appointmentCsvUploadService,
            PurchaseCsvUploadService purchaseCsvUploadService,
            ServicesCsvUploadService servicesCsvUploadService) {
        Map<String, CsvFileUploadService> map = new HashMap<>();
        map.put("clients.csv", clientCsvUploadService);
        map.put("appointments.csv", appointmentCsvUploadService);
        map.put("purchases.csv", purchaseCsvUploadService);
        map.put("services.csv", servicesCsvUploadService);

        return map;
    }
}
