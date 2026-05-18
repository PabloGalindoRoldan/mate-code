package com.parque_industrial.controllers;

import com.parque_industrial.entities.Publicacion;
import com.parque_industrial.services.GestorPublicaciones;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private final GestorPublicaciones gestorPublicaciones;

    public PublicacionController(GestorPublicaciones gestorPublicaciones) {
        this.gestorPublicaciones = gestorPublicaciones;
    }

    // Accessible to both Public landing page view and admin panel layout
    @GetMapping
    public ResponseEntity<List<Publicacion>> obtenerTodas() {
        return ResponseEntity.ok(gestorPublicaciones.obtenerTodas());
    }

    // Secured via configuration mappings later
    @PostMapping
    public ResponseEntity<Publicacion> crear(@RequestBody Publicacion publicacion) {
        Publicacion nueva = gestorPublicaciones.crearPublicacion(publicacion);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // Secured via configuration mappings later
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestorPublicaciones.eliminarPublicacion(id);
        return ResponseEntity.noContent().build();
    }
}