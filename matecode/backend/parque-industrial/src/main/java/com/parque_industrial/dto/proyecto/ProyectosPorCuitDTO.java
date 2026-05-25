package com.parque_industrial.dto.proyecto;

import java.util.List;

public record ProyectosPorCuitDTO(
        List<CrearRequestDefinitivoDTO> definitivos,
        List<CrearRequestDTO> preliminares
) {}
