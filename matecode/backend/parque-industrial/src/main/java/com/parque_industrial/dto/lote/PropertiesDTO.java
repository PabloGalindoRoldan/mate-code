package com.parque_industrial.dto.lote;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PropertiesDTO(double sup, String tipo, String estado, LocalDate fechaVenta, BigDecimal montoVenta,
                            String parque, String nc, String lote) {

}