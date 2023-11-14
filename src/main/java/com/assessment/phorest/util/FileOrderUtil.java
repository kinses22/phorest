package com.assessment.phorest.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileOrderUtil {
    public static List<MultipartFile> orderFiles(List<MultipartFile> files) {
        List<String> desiredOrder = Arrays.asList("clients", "appointments", "services", "purchases");
        List<MultipartFile> orderedFiles = new ArrayList<>();

        for (String fileType : desiredOrder) {
            for (MultipartFile file : files) {
                if (Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().contains(fileType)) {
                    orderedFiles.add(file);
                }
            }
        }

        return orderedFiles;
    }
}



