package com.parque_industrial.persistence.dtos;
import com.parque_industrial.entities.Lote;

import java.time.LocalDate;
public record LoteDTO(
        int identificacion,
        double superficie,
    String estado,
    LocalDate fechaVenta,
    double montoVenta
) {
    public Lote entidad() throws Exception  {
        return new Lote(this.identificacion, this.superficie,  this.estado, this.fechaVenta, this.montoVenta);
    }
    public static LoteDTO dto(Lote lote) {
        return new LoteDTO(
                lote.getIdentificacion(),
                lote.getSuperficie(),
            lote.getEstado(),
            lote.getFechaVenta(),
            lote.getMontoVenta()
        );
    }
}

