package com.assessment.phorest.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CsvConfig {

    private static final Map<String, CsvFileConfig> fileConfigs = new HashMap<>();

    static {
        fileConfigs.put("clients.csv", new CsvFileConfig("Client", new String[]{"id", "first_name", "last_name", "email", "phone", "gender", "banned"}));
        fileConfigs.put("appointments.csv", new CsvFileConfig("Appointment", new String[]{"id", "client_id", "start_time", "end_time"}));
        fileConfigs.put("services.csv", new CsvFileConfig("Service", new String[]{"id", "appointment_id", "name", "price", "loyalty_points"}));
        fileConfigs.put("purchases.csv", new CsvFileConfig("Purchase", new String[]{"id", "appointment_id", "name", "price", "loyalty_points"}));
    }

    public static CsvFileConfig getConfigForFile(String fileName) {
        // todo: handle no file
        return fileConfigs.get(fileName);
    }

}
