package com.parque_industrial.services;

import com.parque_industrial.controllers.dtos.mensaje.ConversacionDTO;
import com.parque_industrial.dto.auth.UsuarioResponse;
import com.parque_industrial.entities.Mensaje;
import com.parque_industrial.persistence.mensajes.MensajeDAO;
import com.parque_industrial.persistence.usuario.UsuarioDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestorMensajeria {

    private final MensajeDAO mensajeDAO;
    private final UsuarioDAO usuarioDAO;

    public GestorMensajeria(MensajeDAO mensajeDAO, UsuarioDAO usuarioDAO) {
        this.mensajeDAO = mensajeDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public Mensaje enviarMensaje(String emisorUsername, String receptorUsername, String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío");
        }

        Mensaje mensaje = new Mensaje(
                emisorUsername,
                receptorUsername, // Si es masivo, acá viaja la constante "TODOS"
                contenido.trim(),
                LocalDateTime.now(),
                false);

        return mensajeDAO.guardar(mensaje);
    }

    public List<Mensaje> obtenerHistorialCon(String usuarioAutenticado, String contactoUsername) {
        // Si el chat seleccionado es el canal de difusión general
        if ("TODOS".equals(contactoUsername)) {
            return mensajeDAO.obtenerMensajesDeDifusion();
        }

        List<Mensaje> conversacion = mensajeDAO.obtenerConversacion(usuarioAutenticado, contactoUsername);
        mensajeDAO.marcarComoLeidos(usuarioAutenticado, contactoUsername);
        return conversacion;
    }

    public int obtenerMensajesSinLeerTotales(String username) {
        return mensajeDAO.contarMensajesSinLeer(username);
    }

    public List<ConversacionDTO> obtenerConversacionesDe(String username) {
        return mensajeDAO.obtenerConversacionesActivas(username);
    }

    // Devuelve todos los usuarios del parque omitiendo al que está logueado en este
    // momento
    public List<UsuarioResponse> obtenerContactosDisponibles(String usernameActual) {
        return usuarioDAO.obtenerTodosLosUsuariosMenos(usernameActual);
    }
}