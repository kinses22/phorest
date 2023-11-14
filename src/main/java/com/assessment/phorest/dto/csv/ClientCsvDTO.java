package com.assessment.phorest.dto.csv;

import com.assessment.phorest.dto.ClientDTO;
import com.assessment.phorest.enumeration.Gender;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class ClientCsvDTO implements GenericCsvDto<ClientDTO> {

    @Override
    public ClientDTO createDTO(CSVRecord csvRecord) {
        return new ClientDTO(csvRecord.get("id"),
                csvRecord.get("first_name"),
                csvRecord.get("last_name"),
                csvRecord.get("email"),
                csvRecord.get("phone"),
                Gender.valueOf(csvRecord.get("gender")),
                Boolean.parseBoolean(csvRecord.get("banned")));
    }
}
