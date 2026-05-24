package com.parque_industrial.controllers;

import com.parque_industrial.dto.proyecto.ProyectoDefinitivoRequest;
import com.parque_industrial.dto.proyecto.ProyectoPreliminarRequest;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.services.GestorProyectos;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

    private final GestorProyectos service;

    public ProyectoController(GestorProyectos service) {
        this.service = service;
    }

    // -------- PRELIMINAR --------

    @PostMapping("/preliminar")
    public ResponseEntity<String> crearPreliminar(
            @Valid @RequestBody ProyectoPreliminarRequest request)
            throws Exception {

        service.crearProyectoPreliminar(request);

        return ResponseEntity.ok(
                "Proyecto preliminar creado");
    }

    @PutMapping("/preliminar/{id}/enviar-revision")
    public ResponseEntity<String> enviarPreliminar(
            @PathVariable String id)
            throws Exception {

        service.enviarPreliminarARevision(id);

        return ResponseEntity.ok(
                "Enviado a revisión");
    }

    @PutMapping("/preliminar/{id}/aprobar")
    public ResponseEntity<String> aprobarPreliminar(
            @PathVariable String id)
            throws Exception {

        service.aprobarPreliminar(id);

        return ResponseEntity.ok("Aprobado");
    }

    @PutMapping("/preliminar/{id}/rechazar")
    public ResponseEntity<String> rechazarPreliminar(
            @PathVariable String id)
            throws Exception {

        service.rechazarPreliminar(id);

        return ResponseEntity.ok("Rechazado");
    }

    @PutMapping("/preliminar/{id}/rectificar")
    public ResponseEntity<String> rectificarPreliminar(
            @PathVariable String id)
            throws Exception {

        service.rectificarPreliminar(id);

        return ResponseEntity.ok("Rectificado");
    }

    // -------- DEFINITIVO --------

    @PostMapping("/preliminar/{id}/definitivo")
    public ResponseEntity<String> crearDefinitivo(
            @PathVariable String id,
            @Valid @RequestBody ProyectoDefinitivoRequest request)
            throws Exception {

        service.crearDefinitivoDesdePreliminar(
                id, request);

        return ResponseEntity.ok(
                "Proyecto definitivo creado");
    }

    @PutMapping("/definitivo/{id}/enviar-revision")
    public ResponseEntity<String> enviarDefinitivo(
            @PathVariable String id)
            throws Exception {

        service.enviarDefinitivoARevision(id);

        return ResponseEntity.ok(
                "Enviado a revisión");
    }

    @PutMapping("/definitivo/{id}/aprobar")
    public ResponseEntity<String> aprobarDefinitivo(
            @PathVariable String id)
            throws Exception {

        service.aprobarDefinitivo(id);

        return ResponseEntity.ok("Aprobado");
    }

    @PutMapping("/definitivo/{id}/rechazar")
    public ResponseEntity<String> rechazarDefinitivo(
            @PathVariable String id)
            throws Exception {

        service.rechazarDefinitivo(id);

        return ResponseEntity.ok("Rechazado");
    }

    @PutMapping("/definitivo/{id}/rectificar")
    public ResponseEntity<String> rectificarDefinitivo(
            @PathVariable String id)
            throws Exception {

        service.rectificarDefinitivo(id);

        return ResponseEntity.ok("Rectificado");
    }

    // -------- CONSULTAS --------

    @GetMapping("/preliminar")
    public ResponseEntity<List<ProyectoPreliminar>>
    listarPreliminar(
            @RequestParam String estado)
            throws Exception {

        return ResponseEntity.ok(
                service.listarPreliminares(estado));
    }

    @GetMapping("/definitivo")
    public ResponseEntity<List<ProyectoDefinitivo>>
    listarDefinitivo(
            @RequestParam String estado)
            throws Exception {

        return ResponseEntity.ok(
                service.listarDefinitivos(estado));
    }

    @GetMapping("/preliminar/{id}/estado")
    public ResponseEntity<String>
    estadoPreliminar(
            @PathVariable String id)
            throws Exception {

        return ResponseEntity.ok(
                service.estadoPreliminar(id));
    }

    @GetMapping("/definitivo/{id}/estado")
    public ResponseEntity<String>
    estadoDefinitivo(
            @PathVariable String id)
            throws Exception {

        return ResponseEntity.ok(
                service.estadoDefinitivo(id));
    }
}