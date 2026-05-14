package com.parque_industrial.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse>
    manejarIllegalArgument(
            IllegalArgumentException ex
    ) {

        ErrorResponse error =
                new ErrorResponse(
                        ex.getMessage(),
                        400
                );

        return ResponseEntity.badRequest().body(error);
    }
}