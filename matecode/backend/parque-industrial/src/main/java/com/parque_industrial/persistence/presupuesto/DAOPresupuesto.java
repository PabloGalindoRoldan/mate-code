package com.parque_industrial.persistence.presupuesto;

import com.parque_industrial.dto.presupuesto.BalancePartidaDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DAOPresupuesto {
    List<BalancePartidaDTO> obtenerBalancePresupuestario(int ejercicioFiscal);

    BigDecimal obtenerSaldoDisponiblePartida(int presupuestoId);

    void registrarModificacionPresupuestaria(int presupuestoId, String tipo, BigDecimal monto, String justificacion);

    void registrarFaseGasto(int presupuestoId, java.sql.Date fecha, String tipoComp, String nroComp, String desc,
            String fase, BigDecimal monto);

    String obtenerCodigoPartidaPorPresupuestoId(int presupuestoId);

    boolean existePresupuesto(int partidaId, int ejercicioFiscal);

    void insertarPresupuestoInicial(int partidaId, int ejercicio, String fuente, BigDecimal monto);

    List<Map<String, Object>> obtenerTodasLasPartidas();

    void crearPartida(String codigo, String nombre, String nivel, Integer parentId);

    List<Map<String, Object>> obtenerHistorialMovimientos(int presupuestoId);
}