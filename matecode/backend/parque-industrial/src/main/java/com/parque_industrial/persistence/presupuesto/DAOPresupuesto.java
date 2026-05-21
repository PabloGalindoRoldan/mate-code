package com.parque_industrial.persistence.presupuesto;

import com.parque_industrial.dto.presupuesto.BalancePartidaDTO;
import java.math.BigDecimal;
import java.util.List;

public interface DAOPresupuesto {
    List<BalancePartidaDTO> obtenerBalancePresupuestario(int ejercicioFiscal);

    BigDecimal obtenerSaldoDisponiblePartida(int presupuestoId);

    void registrarModificacionPresupuestaria(int presupuestoId, String tipo, BigDecimal monto, String justificacion);

    void registrarFaseGasto(int presupuestoId, java.sql.Date fecha, String tipoComp, String nroComp, String desc,
            String fase, BigDecimal monto);

    String obtenerCodigoPartidaPorPresupuestoId(int presupuestoId);
}