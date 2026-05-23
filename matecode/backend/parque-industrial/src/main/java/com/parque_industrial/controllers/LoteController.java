package com.parque_industrial.controllers;

import com.parque_industrial.dto.lote.*;
import com.parque_industrial.services.GeoJsonMapper;
import com.parque_industrial.services.GestorInmobiliario;

import java.util.List;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
    public ResponseEntity<FeatureCollectionDTO> listar(WebRequest request) {
        // 1. Fetch data from your actual Gestor service
        List<com.parque_industrial.entities.Lote> lotes = gestor.listarLotes();

        // 2. Generate the unique fingerprint using the collection's state hash
        String etagValue = Integer.toHexString(lotes.hashCode());

        // 3. Evaluate conditional header mapping.
        // If true, Spring updates response headers to 304 and returns an empty payload
        // body automatically.
        if (request.checkNotModified(etagValue)) {
            return null;
        }

        // 4. Cache Miss: Run your actual GeoJSON serialization logic only when things
        // change
        return ResponseEntity.ok()
                // Replaces Spring Security's "no-store" blocks with validation directives
                .cacheControl(CacheControl.noCache().mustRevalidate())
                .eTag(etagValue)
                .body(mapper.convertirLista(lotes));
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