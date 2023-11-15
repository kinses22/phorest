package com.assessment.phorest.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CSVParentProcessingResponseDTO {
    List<String> unSupportedFiles;
    List<CSVChildProcessingResponseDTO> csvChildProcessingResponseDTOList = new ArrayList<>();
}
