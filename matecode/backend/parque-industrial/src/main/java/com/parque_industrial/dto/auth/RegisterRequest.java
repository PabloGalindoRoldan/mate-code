package com.parque_industrial.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(String nombre, String apellido, String email, String nombreUsuario,
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    String password, String confirmarPassword, String cuitUsuario, String razonSocialEmpresa, String cuitEmpresa) {
}