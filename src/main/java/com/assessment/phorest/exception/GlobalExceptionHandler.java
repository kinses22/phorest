package com.assessment.phorest.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        log.error("InvalidFormatException: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }
}
