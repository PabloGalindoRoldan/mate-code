package com.parque_industrial.controllers;

import com.parque_industrial.dto.proyecto.*;
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

    // @PostMapping("/porCuit")
    // public ResponseEntity<ProyectosDTO> listarProyectosPorCuit(@RequestBody
    // String cuit) {
    // try {
    // ProyectosDTO proyectos = gestorProyectos.listarProyectosPorCuit(cuit);
    // return ResponseEntity.ok(proyectos);
    // } catch (Exception e) {
    // return ResponseEntity.status(500).body(null);
    // }
    // }

    @PostMapping("/porCuit")
    public ResponseEntity<ProyectosDTO> listarProyectosPorCuit(@RequestBody CuitRequest request) {
        try {
            ProyectosDTO proyectos = gestorProyectos.listarProyectosPorCuit(request.getCuit());
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/cambiarEstadoPreliminar")
    public ResponseEntity<String> cambiarEstadoPreliminar(
            @RequestBody CambiarEstadoDTO request
    ) {
        try {

            switch (request.getEstado().toUpperCase()) {

                case "APROBADO" -> gestorProyectos
                        .aprobarProyectoPreliminar(
                                request.getProyectoId()
                        );

                case "RECHAZADO" -> gestorProyectos
                        .rechazarProyectoPreliminar(
                                request.getProyectoId()
                        );

                case "RECTIFICAR" -> gestorProyectos
                        .rectificarProyectoPreliminar(
                                request.getProyectoId()
                        );

                case "EN_REVISION" -> gestorProyectos
                        .ponerEnRevisionProyectoPreliminar(
                                request.getProyectoId()
                        );

                default -> {
                    return ResponseEntity.badRequest().body(
                            "Estado inválido"
                    );
                }
            }

            return ResponseEntity.ok(
                    "Estado actualizado correctamente"
            );

        } catch (Exception e) {

            return ResponseEntity.status(500).body(
                    "Error al cambiar el estado: "
                            + e.getMessage()
            );
        }
    }
//    @PostMapping("/aprobarPreliminar")
//    public ResponseEntity<String> aprobarProyectoPreliminar(@RequestBody Integer proyectoId) {
//        try {
//            gestorProyectos.aprobarProyectoPreliminar(proyectoId);
//            return ResponseEntity.ok("Proyecto preliminar aprobado exitosamente.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error al aprobar el proyecto preliminar: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/rectificarPreliminar")
//    public ResponseEntity<String> rectificarPreliminar(@RequestBody Integer proyectoId) {
//        try {
//            gestorProyectos.rectificarProyectoPreliminar(proyectoId);
//            return ResponseEntity.ok("Proyecto preliminar rectificado exitosamente.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error al rectificar el proyecto preliminar: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/rechazarPreliminar")
//    public ResponseEntity<String> rechazarPreliminar(@RequestBody Integer proyectoId) {
//        try {
//            gestorProyectos.rechazarProyectoPreliminar(proyectoId);
//            return ResponseEntity.ok("Proyecto preliminar rechazado exitosamente.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error al rechazar el proyecto preliminar: " + e.getMessage());
//        }
//    }

     @PutMapping("/actualizarPreliminar")
     public ResponseEntity<String> actualizadoPreliminar(@RequestBody CrearRequestDTO request) {
         try {
             // Llamamos al servicio para procesar la creación
             gestorProyectos.actualizarPreliminar(request);

             return ResponseEntity.ok("Proyecto preliminar actualizado exitosamente.");
         } catch (Exception e) {
             return ResponseEntity.status(500).body("Error al actualizado el proyecto: " + e.getMessage());
         }
     }


    @PostMapping("/aprobarDefinitivo")
    public ResponseEntity<String> aprobarProyectoDefinitivo(@RequestBody Integer proyectoId) {
        try {
            gestorProyectos.aprobarProyectoDefinitivo(proyectoId);
            return ResponseEntity.ok("Proyecto definitivo rechazado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al rechazar el proyecto definitivo: " + e.getMessage());
        }
    }

    @PostMapping("/rectificarDefinitivo")
    public ResponseEntity<String> rectificarDefinitivo(@RequestBody Integer proyectoId) {
        try {
            gestorProyectos.rectificarProyectoDefinitivo(proyectoId);
            return ResponseEntity.ok("Proyecto definitivo rectificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al rectificar el proyecto definitivo: " + e.getMessage());
        }
    }

    @PostMapping("/rechazarDefinitivo")
    public ResponseEntity<String> rechazarDefinitivo(@RequestBody Integer proyectoId) {
        try {
            gestorProyectos.rechazarProyectoDefinitivo(proyectoId);
            return ResponseEntity.ok("Proyecto definitivo rechazado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al rechazar el proyecto definitivo: " + e.getMessage());
        }
    }

}