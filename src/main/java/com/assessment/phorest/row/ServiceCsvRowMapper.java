package com.assessment.phorest.row;

import com.assessment.phorest.dto.ServiceDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class ServiceCsvRowMapper implements GenericCsvRowMapper<ServiceDTO> {

    @Override
    public ServiceDTO createDTO(CSVRecord csvRecord) {
        return new ServiceDTO(csvRecord.get("id"),
                csvRecord.get("appointment_id"),
                csvRecord.get("name"),
                Double.parseDouble(csvRecord.get("price")),
                Integer.parseInt(csvRecord.get("loyalty_points")));
    }
}
