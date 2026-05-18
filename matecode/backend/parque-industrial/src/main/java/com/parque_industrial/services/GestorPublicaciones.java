package com.parque_industrial.services;

import com.parque_industrial.entities.Publicacion;
import com.parque_industrial.persistence.publicaciones.PublicacionDAO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GestorPublicaciones {

    private final PublicacionDAO publicacionDAO;

    public GestorPublicaciones(PublicacionDAO publicacionDAO) {
        this.publicacionDAO = publicacionDAO;
    }

    public List<Publicacion> obtenerTodas() {
        return publicacionDAO.listarTodas();
    }

    public Publicacion crearPublicacion(Publicacion publicacion) {
        // Business Rule validation checks
        if (publicacion.getTitulo() == null || publicacion.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (publicacion.getContenido() == null || publicacion.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El cuerpo del mensaje es obligatorio.");
        }
        return publicacionDAO.guardar(publicacion);
    }

    public void eliminarPublicacion(Long id) {
        boolean eliminado = publicacionDAO.eliminar(id);
        if (!eliminado) {
            throw new RuntimeException("No se encontró la publicación con ID especificado.");
        }
    }
}