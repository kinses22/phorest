package com.assessment.phorest.dto.csv;

import com.assessment.phorest.dto.AppointmentDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCsvDTO implements GenericCsvDto<AppointmentDTO> {

    @Override
    public AppointmentDTO createDTO(CSVRecord csvRecord) {
        return new AppointmentDTO(csvRecord.get("id"),
                csvRecord.get("client_id"),
                csvRecord.get("start_time"),
                csvRecord.get("end_time"));
    }
}
