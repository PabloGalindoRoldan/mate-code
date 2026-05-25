package com.parque_industrial.controllers;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.dto.proyecto.ProyectosDTO;
import com.parque_industrial.services.GestorProyectos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos") // Define la base de la ruta para los endpoints
public class ProyectoController {

    private final GestorProyectos gestorProyectos;

    // Inyección de dependencias del servicio
    public ProyectoController(GestorProyectos gestorProyectos) {
        this.gestorProyectos = gestorProyectos;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearProyecto(@RequestBody CrearRequestDTO request) {
        try {
            // Llamamos al servicio para procesar la creación
            gestorProyectos.crearProyectoPreliminar(request);

            return ResponseEntity.ok("Proyecto preliminar creado exitosamente.");
        } catch (Exception e) {
            // En un entorno real, aquí podrías usar un GlobalExceptionHandler
            return ResponseEntity.status(500).body("Error al crear el proyecto: " + e.getMessage());
        }
    }

    @PostMapping("/crearDefinitivo")
    public ResponseEntity<String> crearProyectoDefinitivo(@RequestBody CrearRequestDefinitivoDTO request) {
        try {
            // Llamamos al servicio para procesar la creación definitiva
            gestorProyectos.crearProyectoDefinitivo(request);

            return ResponseEntity.ok("Proyecto definitivo creado exitosamente.");
        } catch (Exception e) {
            // En un entorno real, aquí podrías usar un GlobalExceptionHandler
            return ResponseEntity.status(500).body("Error al crear el proyecto definitivo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ProyectosDTO> listarProyectos() {
        try {
            ProyectosDTO proyectos = gestorProyectos.listarProyectos();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/porCuit")
    public ResponseEntity<ProyectosDTO> listarProyectosPorCuit(@RequestBody String cuit) {
        try {
            ProyectosDTO proyectos = gestorProyectos.listarProyectosPorCuit(cuit);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}