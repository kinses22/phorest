package com.assessment.phorest.service.implementation;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CsvUploadServiceImpl implements CsvUploadService {

    @Override
    public String processCsvFiles(List<MultipartFile> files) {

        if (files.isEmpty()){
            // todo: test to see what happens if we try pass empty files
        }

        for (MultipartFile file : files){
            String fileName = file.getOriginalFilename();


            // todo; validate file names and if there is any files

        }


//        // todo: if checksum exists in the database && Status passed for each file and then add to new list
//
//        // todo: list of errors per file and for it to be added to a dto with [ file name records processed, records failed, total, validation errors ]
//        List<String> errors = new ArrayList<>();
//
//
//        String fileName = file.getOriginalFilename();
//        List<CsvDataDTO> csvDTOList = new ArrayList<>();
//        // todo: handle no file as we get 500 back
//        CsvFileConfig csvFileConfig = CsvConfig.getConfigForFile(fileName);
//
//        CsvDataDTO csvDataDto = null;
//        try (CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader(csvFileConfig.getHeaders()).setSkipHeaderRecord(true)
//                .build().parse(new InputStreamReader(file.getInputStream()))) {
//            for (CSVRecord csvRecord : csvParser) {
//                try {
//                    csvDataDto = csvDtoFactoryManager.createDTO(csvFileConfig.getDtoType(), csvRecord);
//                    Set<ConstraintViolation<CsvDataDTO>> violations = validator.validate(csvDataDto);
//                    if (violations.isEmpty()){
//                        csvDTOList.add(csvDataDto);
//                    }
//                } catch (IllegalArgumentException e) {
//                    errors.add(csvRecord.get("id"));
//                }
//            }
//        } catch (IOException | IllegalArgumentException e) {
//            log.info("There was an issue parsing the csv file: {}, {}", e.getMessage(), e);
//            // THROW HERE AS ISSUE WITH PARSING
//        }
//
//        //todo: factory pattern & iterate over all and save individually as FK might not exist
//        if (csvDataDto instanceof ClientDTO) {
//            List<Client> clients = new ArrayList<>();
//            csvDTOList.forEach(clientDTO -> clients.add(clientMapper.mapToEntity(clientDTO)));
//            clientRepository.saveAll(clients);
//        } else if (csvDataDto instanceof AppointmentDTO) {
//            List<Appointment> appointments = new ArrayList<>();
//            csvDTOList.forEach(appointmentDto -> appointments.add(appointmentMapper.mapToEntity(appointmentDto)));
//            appointmentRepository.saveAll(appointments);
//        } else if (csvDataDto instanceof PurchaseDTO) {
//            List<Purchase> purchases = new ArrayList<>();
//            csvDTOList.forEach(purchaseDto -> purchases.add(purchaseMapper.mapToEntity(purchaseDto)));
//            purchaseRepository.saveAll(purchases);
//        } else {
//            throw new IllegalArgumentException("Unsupported DTO type");
//        }
       return "hi";


    }
}
