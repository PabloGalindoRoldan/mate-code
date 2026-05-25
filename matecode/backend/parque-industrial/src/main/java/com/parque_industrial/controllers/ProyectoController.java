package com.parque_industrial.controllers;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.services.GestorProyectos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}