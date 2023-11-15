package com.assessment.phorest.row;

import com.assessment.phorest.dto.AppointmentDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCsvRowMapper implements GenericCsvRowMapper<AppointmentDTO> {

    @Override
    public AppointmentDTO createDTO(CSVRecord csvRecord) {
        return new AppointmentDTO(csvRecord.get("id"),
                csvRecord.get("client_id"),
                csvRecord.get("start_time"),
                csvRecord.get("end_time"));
    }
}
