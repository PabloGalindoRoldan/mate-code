package com.parque_industrial.controllers;

import com.parque_industrial.dto.mensajes.ConversacionDTO;
import com.parque_industrial.dto.mensajes.EnviarMensajeRequest;
import com.parque_industrial.dto.auth.UsuarioResponse;
import com.parque_industrial.entities.Mensaje;
import com.parque_industrial.services.GestorMensajeria;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    private final GestorMensajeria gestorMensajeria;

    public MensajeController(GestorMensajeria gestorMensajeria) {
        this.gestorMensajeria = gestorMensajeria;
    }

    @PostMapping
    public ResponseEntity<Mensaje> enviarMensaje(@RequestBody EnviarMensajeRequest request,
            Authentication authentication) {
        String emisorUsername = authentication.getName();
        Mensaje nuevoMensaje = gestorMensajeria.enviarMensaje(emisorUsername, request.getReceptorUsername(),
                request.getContenido());
        return ResponseEntity.ok(nuevoMensaje);
    }

    @GetMapping("/conversacion/{contactoUsername}")
    public ResponseEntity<List<Mensaje>> obtenerHistorial(@PathVariable String contactoUsername,
            Authentication authentication) {
        String username = authentication.getName();
        List<Mensaje> historial = gestorMensajeria.obtenerHistorialCon(username, contactoUsername);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/sin-leer/total")
    public ResponseEntity<Integer> obtenerMensajesSinLeerTotales(Authentication authentication) {
        String username = authentication.getName();
        int conteo = gestorMensajeria.obtenerMensajesSinLeerTotales(username);
        return ResponseEntity.ok(conteo);
    }

    @GetMapping("/conversaciones")
    public ResponseEntity<List<ConversacionDTO>> obtenerConversaciones(Authentication authentication) {
        String username = authentication.getName();
        List<ConversacionDTO> conversaciones = gestorMensajeria.obtenerConversacionesDe(username);
        return ResponseEntity.ok(conversaciones);
    }

    // Cambiado para integrarse a la comunicación total de usuarios
    @GetMapping("/contactos-disponibles")
    public ResponseEntity<List<UsuarioResponse>> obtenerContactosDisponibles(Authentication authentication) {
        String username = authentication.getName();
        List<UsuarioResponse> contactos = gestorMensajeria.obtenerContactosDisponibles(username);
        return ResponseEntity.ok(contactos);
    }
}