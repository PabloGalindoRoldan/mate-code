package com.parque_industrial.controllers;

import com.parque_industrial.dto.presupuesto.BalancePartidaDTO;
import com.parque_industrial.dto.presupuesto.PresupuestoInicialDTO;
import com.parque_industrial.dto.presupuesto.ModificacionPresupuestariaRequest;
import com.parque_industrial.dto.presupuesto.RegistroGastoRequest;
import com.parque_industrial.services.GestorPresupuesto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/presupuesto")
public class PresupuestoController {
    private final GestorPresupuesto gestorPresupuesto;

    // Inyección manual / constructor de tu Gestor de servicios
    public PresupuestoController(GestorPresupuesto gestorPresupuesto) {
        this.gestorPresupuesto = gestorPresupuesto;
    }

    /**
     * 
     * Devuelve el estado consolidado de ejecución del libro de balances de gastos.
     * 
     * GET /api/presupuesto/balance/2025
     * 
     */

    @GetMapping("/balance/{ejercicio}")
    public ResponseEntity<List<BalancePartidaDTO>> getLibroDeBalances(@PathVariable int ejercicio) {
        List<BalancePartidaDTO> balance = gestorPresupuesto.verLibroDeBalances(ejercicio);
        return ResponseEntity.ok(balance);
    }

    /**
     * 
     * Aplica incrementos o reducciones de partidas (con control estricto del Art.
     * 27 - Ley 5763)
     * POST /api/presupuesto/reestructurar
     */

    @PostMapping("/reestructurar")
    public ResponseEntity<?> reestructurarPartida(@RequestBody ModificacionPresupuestariaRequest request) {
        try {
            gestorPresupuesto.procesarReestructuracion(
                    request.getPresupuestoId(),
                    request.getTipo(),
                    request.getMonto(),
                    request.getJustificacion());
            return ResponseEntity.ok(Map.of("message", "Reestructuración presupuestaria procesada exitosamente."));
        } catch (IllegalArgumentException | IllegalStateException e) {

            // Captura los desvíos legales (ej: intentar decrementar Gastos en Personal) o
            // falta de saldo

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al procesar el movimiento contable."));
        }
    }

    /**
     * 
     * Registra las etapas transaccionales del gasto público (Compromiso, Devengado,
     * 
     * Pago)
     * 
     * POST /api/presupuesto/gasto
     * 
     */

    @PostMapping("/gasto")
    public ResponseEntity<?> registrarFaseGasto(@RequestBody RegistroGastoRequest request) {
        try {
            gestorPresupuesto.registrarGastoAfectacion(
                    request.getPresupuestoId(),
                    new Date(), // Registra con la fecha actual del sistema
                    request.getTipoComprobante(),
                    request.getNroComprobante(),
                    request.getDescripcion(),
                    request.getFase(),
                    request.getMonto());
            return ResponseEntity
                    .ok(Map.of("message", "Etapa de gasto ('" + request.getFase() + "') asentada en el libro diario."));
        } catch (IllegalStateException e) {
            // Captura errores cuando no hay crédito disponible disponible para comprometer
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar la afectación del gasto."));
        }
    }

    @PostMapping("/carga-inicial")
    public ResponseEntity<?> cargarPresupuestoInicial(@RequestBody List<PresupuestoInicialDTO> partidas) {
        try {
            gestorPresupuesto.cargarPresupuestoInicial(partidas);
            return ResponseEntity.ok(Map.of("message", "Presupuesto inicial cargado exitosamente para el ejercicio."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al procesar la carga inicial."));
        }
    }

    @GetMapping("/partidas-catalogo")
    public ResponseEntity<List<Map<String, Object>>> getCatalogo() {
        // Esto debería llamar a un método en tu servicio que haga un SELECT * FROM
        // partidas_presupuestarias
        return ResponseEntity.ok(gestorPresupuesto.obtenerCatalogoPartidas());
    }
}