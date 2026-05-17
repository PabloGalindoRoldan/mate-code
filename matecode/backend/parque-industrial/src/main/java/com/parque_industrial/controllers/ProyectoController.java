package com.parque_industrial.controllers;

import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.services.ProyectoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(
            ProyectoService proyectoService) {

        this.proyectoService = proyectoService;
    }

    @PostMapping("/preliminar")
    public ResponseEntity<String>
    crearProyectoPreliminar(
            @RequestBody ProyectoPreliminar proyecto) {

        try {
            proyectoService
                    .crearProyectoPreliminar(proyecto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Proyecto preliminar creado con éxito");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/preliminar/aprobar")
    public ResponseEntity<String>
    aprobarProyectoPreliminar(
            @RequestBody ProyectoPreliminar proyecto) {

        try {
            proyectoService
                    .aprobarProyectoPreliminar(proyecto);

            return ResponseEntity.ok(
                    "Proyecto aprobado");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/preliminar/rechazar")
    public ResponseEntity<String>
    rechazarProyectoPreliminar(
            @RequestBody ProyectoPreliminar proyecto) {

        try {
            proyectoService
                    .rechazarProyectoPreliminar(proyecto);

            return ResponseEntity.ok(
                    "Proyecto rechazado");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}