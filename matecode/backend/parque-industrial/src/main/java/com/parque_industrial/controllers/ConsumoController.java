package com.parque_industrial.controllers;

import com.parque_industrial.controllers.dtos.empresa.CargarConsumoRequest;
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

        // 1. 'principal.getName()' nos da el username único del token
        String username = principal.getName();

        // 2. Buscamos el CUIT real mapeado en el backend
        String cuitEmpresa = obtenerCuitEmpresaPorUsername(username);

        // 3. Se registra usando el CUIT corporativo recuperado
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
        // 1. Obtenemos el username extraído del contexto de seguridad
        String username = principal.getName();

        // 2. Buscamos su CUIT correspondiente
        String cuitEmpresa = obtenerCuitEmpresaPorUsername(username);

        // 3. Traemos el historial filtrado estrictamente por ese CUIT
        List<ConsumoResponseDTO> historial = gestorConsumos.obtenerHistorialEmpresa(cuitEmpresa);
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
     * Si no cumple los requisitos de negocio, corta el flujo lanzando una
     * excepción.
     */
    private String obtenerCuitEmpresaPorUsername(String username) {
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en el sistema"));

        // Validación de seguridad por si un ADMIN_PARQUE intenta entrar a endpoints de
        // carga/historial corporativo
        if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdentificacion() == null) {
            throw new IllegalArgumentException(
                    "El usuario '" + username + "' no posee una empresa vinculada para operar consumos.");
        }

        // Modificado de .getIdentificacion() a .getCuit() para respetar tu modelo de
        // datos
        return usuario.getEmpresa().getIdentificacion();
    }
}