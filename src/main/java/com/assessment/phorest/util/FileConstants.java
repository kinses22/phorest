package com.assessment.phorest.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileConstants {
    public static final List<String> DESIRED_ORDER = Arrays.asList("clients.csv", "appointments.csv", "services.csv", "purchases.csv");
    public static final Set<String> ALLOWED_FILE_NAMES = new HashSet<>(Arrays.asList("clients.csv", "appointments.csv", "services.csv", "purchases.csv"));

}
