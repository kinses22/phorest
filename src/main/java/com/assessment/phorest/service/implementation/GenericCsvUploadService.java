package com.assessment.phorest.service.implementation;

import com.assessment.phorest.dao.GenericRepository;
import com.assessment.phorest.dto.CsvDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericCsvUploadService<DTO, Entity, Mapper> {

    @Autowired
    private GenericRepository<Entity> genericRepository;

    @Autowired
    private Mapper mapper;

    public String processCsvFiles(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        List<CsvDataDTO> csvDTOList = new ArrayList<>();
        // todo: handle no file as we get 500 back
        CsvFileConfig csvFileConfig = CsvConfig.getConfigForFile(fileName);


    }


}
