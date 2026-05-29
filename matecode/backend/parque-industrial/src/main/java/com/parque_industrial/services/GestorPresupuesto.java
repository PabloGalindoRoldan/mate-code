package com.parque_industrial.services;

import com.parque_industrial.dto.presupuesto.BalancePartidaDTO;
import com.parque_industrial.persistence.presupuesto.DAOPresupuesto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.parque_industrial.dto.presupuesto.PresupuestoInicialDTO;
import org.springframework.transaction.annotation.Transactional;
import com.parque_industrial.dto.presupuesto.PresupuestoInicialDTO;

@Service
public class GestorPresupuesto {

    // Cambiado a final y sin instanciar a mano para respetar IoC de Spring
    private final DAOPresupuesto daoPresupuesto;

    // Spring inyecta automáticamente la implementación DAOPresupuestoJDBC aquí
    public GestorPresupuesto(DAOPresupuesto daoPresupuesto) {
        this.daoPresupuesto = daoPresupuesto;
    }

    public List<BalancePartidaDTO> verLibroDeBalances(int ejercicio) {
        return daoPresupuesto.obtenerBalancePresupuestario(ejercicio);
    }

    public List<Map<String, Object>> obtenerCatalogoPartidas() {
        return daoPresupuesto.obtenerTodasLasPartidas();
    }

    /**
     * Regla del Art. 20 y 27 de la Ley 5763: Las partidas de Personal (Código que
     * inicia con '1.') son inmutables para decrementos en favor de otros rubros.
     */
    public void procesarReestructuracion(int presupuestoId, String tipo, BigDecimal monto, String justificacion) {
        if (tipo.equalsIgnoreCase("DISMINUCION")) {
            String codigoPartida = daoPresupuesto.obtenerCodigoPartidaPorPresupuestoId(presupuestoId);

            if (codigoPartida.startsWith("1.")) {
                throw new IllegalArgumentException("Violación del Art. 27 de la Ley N° 5763: " +
                        "No está permitido disminuir el crédito presupuestario asignado a Gastos en Personal (Partida "
                        + codigoPartida + ").");
            }

            BigDecimal disponible = daoPresupuesto.obtenerSaldoDisponiblePartida(presupuestoId);
            if (disponible.compareTo(monto) < 0) {
                throw new IllegalStateException(
                        "Saldo insuficiente en la partida para efectuar la disminución solicitada.");
            }
        }
        daoPresupuesto.registrarModificacionPresupuestaria(presupuestoId, tipo, monto, justificacion);
    }

    /**
     * Valida el disponible real antes de generar un compromiso presupuestario.
     */
    public void registrarGastoAfectacion(int presupuestoId, java.util.Date fecha, String tipoComp,
            String nroComp, String desc, String fase, BigDecimal monto) {

        if (fase.equalsIgnoreCase("COMPROMISO")) {
            BigDecimal disponible = daoPresupuesto.obtenerSaldoDisponiblePartida(presupuestoId);
            if (disponible.compareTo(monto) < 0) {
                throw new IllegalStateException(
                        "Crédito Insuficiente. La partida presupuestaria no posee saldo disponible para este compromiso.");
            }
        }

        java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
        daoPresupuesto.registrarFaseGasto(presupuestoId, sqlDate, tipoComp, nroComp, desc, fase, monto);
    }

    @Transactional
    public void cargarPresupuestoInicial(List<PresupuestoInicialDTO> partidas) {
        for (PresupuestoInicialDTO dto : partidas) {

            // 1. Validar si ya existe presupuesto para este ejercicio y partida
            if (daoPresupuesto.existePresupuesto(dto.getPartidaId(), dto.getEjercicioFiscal())) {
                throw new IllegalArgumentException("Ya existe una asignación de crédito para la partida ID "
                        + dto.getPartidaId() + " en el ejercicio " + dto.getEjercicioFiscal());
            }

            // 2. Insertar registro inicial
            daoPresupuesto.insertarPresupuestoInicial(
                    dto.getPartidaId(),
                    dto.getEjercicioFiscal(),
                    dto.getFuenteFinanciamiento(),
                    BigDecimal.valueOf(dto.getMonto()));
        }
    }
}