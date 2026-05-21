package com.parque_industrial.dto.lote;

public record CambiarEstadoRequest(int identificacion, String estado) {
}
// estado tiene que ser "disponible", "reservado" , o "vendido" asi con minusculas sino exepcion
