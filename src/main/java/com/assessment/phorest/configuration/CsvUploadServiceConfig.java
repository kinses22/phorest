package com.assessment.phorest.configuration;

import com.assessment.phorest.service.CsvFileUploadService;
import com.assessment.phorest.service.implementation.AppointmentCsvUploadService;
import com.assessment.phorest.service.implementation.ClientCsvUploadService;
import com.assessment.phorest.service.implementation.PurchaseCsvUploadService;
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
            PurchaseCsvUploadService purchaseCsvUploadService) {

        Map<String, CsvFileUploadService> map = new HashMap<>();
        map.put("client.csv", clientCsvUploadService);
        map.put("appointment.csv", appointmentCsvUploadService);
        map.put("purchase.csv", purchaseCsvUploadService);
        // todo: service

        return map;
    }
}
