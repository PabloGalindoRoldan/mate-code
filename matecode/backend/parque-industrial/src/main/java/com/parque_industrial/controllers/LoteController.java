package com.parque_industrial.controllers;

import com.parque_industrial.dto.lote.*;


import com.parque_industrial.services.GeoJsonMapper;
import com.parque_industrial.services.GestorInmobiliario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final GestorInmobiliario gestor;

    private final GeoJsonMapper mapper;

    public LoteController(GestorInmobiliario gestor, GeoJsonMapper mapper) {
        this.gestor = gestor;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<FeatureCollectionDTO> listar() {
        return ResponseEntity.ok(mapper.convertirLista(gestor.listarLotes()));
    }

    @PostMapping("/reservar")
    public ResponseEntity<Void> reservar(@RequestBody ReservarRequestDTO request) {
        gestor.reservarLote(request.identificacion());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancelarReserva")
    public ResponseEntity<Void> cancelarReserva(@RequestBody AnularReservaRequestDTO request) {
        gestor.cancelarReserva(request.identificacion());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/vender")
    public ResponseEntity<Void> vender(@RequestBody VentaRequestDTO request) {
        gestor.venderLote(request.identificacion(), request.monto(), request.fechaVenta());
        return ResponseEntity.ok().build();
    }
}