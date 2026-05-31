package com.parque_industrial.controllers;

import com.parque_industrial.dto.empresa.CargarConsumoRequest;
import com.parque_industrial.dto.empresa.ConsumoResponseDTO;
import com.parque_industrial.entities.Usuario;
import com.parque_industrial.persistence.usuario.UsuarioDAO;
import com.parque_industrial.services.GestorConsumos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/consumos")
public class ConsumoController {

    private final GestorConsumos gestorConsumos;
    private final UsuarioDAO usuarioDAO;

    // Inyección de dependencias por constructor
    public ConsumoController(GestorConsumos gestorConsumos, UsuarioDAO usuarioDAO) {
        this.gestorConsumos = gestorConsumos;
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * POST /api/consumos
     * Permite a una empresa registrar sus consumos mensuales de manera segura.
     */
    @PostMapping
    public ResponseEntity<String> registrarConsumo(
            @Valid @RequestBody CargarConsumoRequest request,
            Principal principal) {

        String username = principal.getName();
        String cuitEmpresa = obtenerCuitEmpresaPorUsername(username);
        gestorConsumos.registrarConsumoMensual(cuitEmpresa, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Consumo registrado exitosamente para el período " + request.getMes() + "/" + request.getAno());
    }

    /**
     * GET /api/consumos/historial
     * Devuelve la lista de declaraciones previas de la empresa vinculada al usuario
     * autenticado.
     */
    @GetMapping("/historial")
    public ResponseEntity<List<ConsumoResponseDTO>> obtenerHistorialEmpresa(Principal principal) {
        String username = principal.getName();
        String cuitEmpresa = obtenerCuitEmpresaPorUsername(username);
        List<ConsumoResponseDTO> historial = gestorConsumos.obtenerHistorialEmpresa(cuitEmpresa);
        return ResponseEntity.ok(historial);
    }

    /**
     * GET /api/consumos/historial/{cuit}
     * Permite a los administradores del parque consultar el historial completo de
     * cualquier empresa seleccionada usando su CUIT de forma directa.
     */
    @GetMapping("/historial/{cuit}")
    public ResponseEntity<List<ConsumoResponseDTO>> obtenerHistorialEmpresaPorCuit(@PathVariable String cuit) {
        // Reutiliza directo la lógica que ya tenías en el gestor pasándole el CUIT del
        // path
        List<ConsumoResponseDTO> historial = gestorConsumos.obtenerHistorialEmpresa(cuit);
        return ResponseEntity.ok(historial);
    }

    /**
     * GET /api/consumos/reporte-global/{ano}
     * Métrica exclusiva para administradores del parque. No requiere CUIT de
     * empresa.
     */
    @GetMapping("/reporte-global/{ano}")
    public ResponseEntity<List<ConsumoResponseDTO>> obtenerReporteGlobal(@PathVariable int ano) {
        List<ConsumoResponseDTO> reporte = gestorConsumos.obtenerReporteGlobalPorAno(ano);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Método auxiliar privado para resolver la relación Usuario -> Empresa -> CUIT.
     */
    private String obtenerCuitEmpresaPorUsername(String username) {
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en el sistema"));

        if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdentificacion() == null) {
            throw new IllegalArgumentException(
                    "El usuario '" + username + "' no posee una empresa vinculada para operar consumos.");
        }

        return usuario.getEmpresa().getIdentificacion();
    }
}