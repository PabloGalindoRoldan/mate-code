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


    @PostMapping("/aprobarPreliminar")
    public ResponseEntity<String> aprobarProyectoPreliminar(@RequestBody Integer proyectoId) {
        try {
            // Aquí podrías llamar a un método del servicio para aprobar el proyecto preliminar
            // gestorProyectos.aprobarProyectoPreliminar(proyectoId);
            return ResponseEntity.ok("Proyecto preliminar aprobado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al aprobar el proyecto preliminar: " + e.getMessage());
        }
    }

    @PostMapping("/rectificarPreliminar")
    public ResponseEntity<String> rectificarPreliminar(@RequestBody Integer proyectoId) {

        return null;
    }

    @PostMapping("/rechazarPreliminar")
    public ResponseEntity<String> rechazarPreliminar(@RequestBody Integer proyectoId) {

        return null;
    }

//    @PostMapping("/ModificarPreliminar") // capaz no deberia devolver CrearRequestDTO, este seria aquel q usaria la empresa para modificar la rectificacion q le hayanhecho
//    public ResponseEntity<CrearRequestDTO> modificarPreliminar(@RequestBody CrearRequestDTO request) {
//
//    }
    @PostMapping("/aprobarDefinitivo")
    public ResponseEntity<String> aprobarProyectoDefinitivo(@RequestBody Integer proyectoId) {
        return null;
    }

    @PostMapping("/rectificarDefinitivo")
    public ResponseEntity<String> rectificarDefinitivo(@RequestBody Integer proyectoId) {

        return null;
    }

    @PostMapping("/rechazarDefinitivo")
    public ResponseEntity<String> rechazarDefinitivo(@RequestBody Integer proyectoId) {

        return null;
    }

}