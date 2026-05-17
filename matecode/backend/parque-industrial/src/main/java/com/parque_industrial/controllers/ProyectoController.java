package com.parque_industrial.controllers;

import com.parque_industrial.dto.proyecto.ProyectoDefinitivoRequest;
import com.parque_industrial.dto.proyecto.ProyectoPreliminarRequest;
import com.parque_industrial.services.ProyectoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService service;

    public ProyectoController(ProyectoService service) {
        this.service = service;
    }

    // -------- PRELIMINAR --------

    @PostMapping("/preliminar")
    public ResponseEntity<?> crearPreliminar(
            @RequestBody ProyectoPreliminarRequest request)
            throws Exception {

        service.crearProyectoPreliminar(request);
        return ResponseEntity.ok("Creado");
    }

    @PutMapping("/preliminar/{id}/enviar-revision")
    public ResponseEntity<?> enviarPreliminar(
            @PathVariable String id)
            throws Exception {

        service.enviarPreliminarARevision(id);
        return ResponseEntity.ok("Enviado a revisión");
    }

    @PutMapping("/preliminar/{id}/aprobar")
    public ResponseEntity<?> aprobarPreliminar(@PathVariable String id)
            throws Exception {

        service.aprobarPreliminar(id);
        return ResponseEntity.ok("Aprobado");
    }

    @PutMapping("/preliminar/{id}/rechazar")
    public ResponseEntity<?> rechazarPreliminar(@PathVariable String id)
            throws Exception {

        service.rechazarPreliminar(id);
        return ResponseEntity.ok("Rechazado");
    }

    @PutMapping("/preliminar/{id}/rectificar")
    public ResponseEntity<?> rectificarPreliminar(@PathVariable String id)
            throws Exception {

        service.rectificarPreliminar(id);
        return ResponseEntity.ok("Rectificado");
    }

    // -------- DEFINITIVO --------

    @PostMapping("/preliminar/{id}/definitivo")
    public ResponseEntity<?> crearDefinitivo(
            @PathVariable String id,
            @RequestBody ProyectoDefinitivoRequest request)
            throws Exception {

        service.crearDefinitivoDesdePreliminar(id, request);
        return ResponseEntity.ok("Definitivo creado");
    }

    @PutMapping("/definitivo/{id}/enviar-revision")
    public ResponseEntity<?> enviarDefinitivo(@PathVariable String id)
            throws Exception {

        service.enviarDefinitivoARevision(id);
        return ResponseEntity.ok("Enviado a revisión");
    }

    @PutMapping("/definitivo/{id}/aprobar")
    public ResponseEntity<?> aprobarDefinitivo(@PathVariable String id)
            throws Exception {

        service.aprobarDefinitivo(id);
        return ResponseEntity.ok("Aprobado");
    }

    @PutMapping("/definitivo/{id}/rechazar")
    public ResponseEntity<?> rechazarDefinitivo(@PathVariable String id)
            throws Exception {

        service.rechazarDefinitivo(id);
        return ResponseEntity.ok("Rechazado");
    }

    @PutMapping("/definitivo/{id}/rectificar")
    public ResponseEntity<?> rectificarDefinitivo(@PathVariable String id)
            throws Exception {

        service.rectificarDefinitivo(id);
        return ResponseEntity.ok("Rectificado");
    }

    // -------- CONSULTAS --------

    @GetMapping("/preliminar")
    public ResponseEntity<?> listarPreliminar(@RequestParam String estado)
            throws Exception {

        return ResponseEntity.ok(service.listarPreliminares(estado));
    }

    @GetMapping("/definitivo")
    public ResponseEntity<?> listarDefinitivo(@RequestParam String estado)
            throws Exception {

        return ResponseEntity.ok(service.listarDefinitivos(estado));
    }

    @GetMapping("/preliminar/{id}/estado")
    public ResponseEntity<?> estadoPreliminar(@PathVariable String id)
            throws Exception {

        return ResponseEntity.ok(service.estadoPreliminar(id));
    }

    @GetMapping("/definitivo/{id}/estado")
    public ResponseEntity<?> estadoDefinitivo(@PathVariable String id)
            throws Exception {

        return ResponseEntity.ok(service.estadoDefinitivo(id));
    }
}