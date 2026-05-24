package com.parque_industrial.dto.auth;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword) {
}