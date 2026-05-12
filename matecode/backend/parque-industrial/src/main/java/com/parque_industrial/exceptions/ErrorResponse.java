package com.parque_industrial.exceptions;

public record ErrorResponse(
        String error,
        int status
) {
}