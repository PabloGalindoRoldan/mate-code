package com.parque_industrial.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> manejarIllegalArgument(IllegalArgumentException ex) {
                ErrorResponse error = new ErrorResponse(ex.getMessage(), 400);
                return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> manejarValidacionesSpring(MethodArgumentNotValidException ex) {
                String mensajeDefault = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getDefaultMessage())
                                .findFirst()
                                .orElse("Error de validación en los datos enviados.");

                ErrorResponse error = new ErrorResponse(mensajeDefault, 400);
                return ResponseEntity.badRequest().body(error);
        }
}