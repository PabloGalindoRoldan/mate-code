package com.parque_industrial.persistence.mensajes;

import com.parque_industrial.controllers.dtos.mensaje.ConversacionDTO;
import com.parque_industrial.entities.Mensaje;
import java.util.List;

public interface MensajeDAO {
    Mensaje guardar(Mensaje mensaje);

    List<Mensaje> obtenerConversacion(String username1, String username2);

    void marcarComoLeidos(String receptorUsername, String emisorUsername);

    int contarMensajesSinLeer(String username);

    List<ConversacionDTO> obtenerConversacionesActivas(String username);

    List<Mensaje> obtenerMensajesDeDifusion();
}