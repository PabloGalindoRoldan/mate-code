package com.parque_industrial.dto.lote;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VentaRequestDTO(int identificacion, BigDecimal monto, LocalDate fechaVenta) {
}