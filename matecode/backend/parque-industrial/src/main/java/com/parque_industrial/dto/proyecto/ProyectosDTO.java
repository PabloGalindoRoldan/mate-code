package com.parque_industrial.dto.proyecto;

import java.util.List;

public record ProyectosDTO(
        List<CrearRequestDefinitivoDTO> definitivos,
        List<CrearRequestDTO> preliminares
) {}
