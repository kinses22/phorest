package com.assessment.phorest.row;

import com.assessment.phorest.dto.PurchaseDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class PurchaseCsvRowMapper implements GenericCsvRowMapper<PurchaseDTO> {

    @Override
    public PurchaseDTO createDTO(CSVRecord csvRecord) {
        return new PurchaseDTO(csvRecord.get("id"),
                csvRecord.get("appointment_id"),
                csvRecord.get("name"),
                Double.parseDouble(csvRecord.get("price")),
                Integer.parseInt(csvRecord.get("loyalty_points")));
    }
}
