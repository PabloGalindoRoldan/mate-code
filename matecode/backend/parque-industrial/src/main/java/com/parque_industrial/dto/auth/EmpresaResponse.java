package com.parque_industrial.dto.auth;

import java.util.List;

public record EmpresaResponse(
        String cuit,
        String razonSocial,
        boolean esRadicada,
        List<UsuarioResponse> representantes
) {}