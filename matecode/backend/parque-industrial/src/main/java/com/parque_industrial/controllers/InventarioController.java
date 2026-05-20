package com.parque_industrial.controllers;

import com.parque_industrial.dto.inventario.BajaElementoRequestDTO;
import com.parque_industrial.dto.inventario.ElementoRequestDTO;
import com.parque_industrial.dto.inventario.ElementoResponseDTO;
import com.parque_industrial.services.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public ResponseEntity<ElementoResponseDTO> crear(@RequestBody ElementoRequestDTO request) {
        ElementoResponseDTO nuevoElemento = inventarioService.crearElemento(request);
        return new ResponseEntity<>(nuevoElemento, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElementoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        ElementoResponseDTO elemento = inventarioService.buscarPorId(id);
        return ResponseEntity.ok(elemento);
    }

    @GetMapping
    public ResponseEntity<List<ElementoResponseDTO>> listar(
            @RequestParam(value = "soloActivos", defaultValue = "false") boolean soloActivos) {
        List<ElementoResponseDTO> elementos = soloActivos
                ? inventarioService.obtenerElementosActivos()
                : inventarioService.obtenerTodosLosElementos();
        return ResponseEntity.ok(elementos);
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<ElementoResponseDTO> darDeBaja(@PathVariable Integer id,
            @RequestBody BajaElementoRequestDTO request) {
        ElementoResponseDTO elementoDadoDeBaja = inventarioService.procesarBajaLogica(id, request);
        return ResponseEntity.ok(elementoDadoDeBaja);
    }

    // Manejo local de excepciones comunes de negocio para retornar respuestas
    // limpias a la UI
    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<String> manejarExcepcionesDeNegocio(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}