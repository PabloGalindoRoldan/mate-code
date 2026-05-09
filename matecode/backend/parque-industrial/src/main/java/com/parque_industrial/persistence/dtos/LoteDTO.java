package com.parque_industrial.persistence.dtos;
import com.parque_industrial.entities.Lote;

import java.time.LocalDate;

import static com.parque_industrial.entities.Lote.DISPONIBLE;

public record LoteDTO(
        int identificacion,
        double superficie,
    String estado,
    LocalDate fechaVenta,
    double montoVenta
) {
    public LoteDTO( int identificacion, double superficie)   {
        this(identificacion, superficie, DISPONIBLE, null, 0);
    }
    public LoteDTO{
        if (identificacion < 0 ){
            throw new IllegalArgumentException("La identificación del lote es un numero postivo");
        }
        if (superficie <= 0) {
            throw new IllegalArgumentException("La superficie debe ser un valor positivo");
        }
    }
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

