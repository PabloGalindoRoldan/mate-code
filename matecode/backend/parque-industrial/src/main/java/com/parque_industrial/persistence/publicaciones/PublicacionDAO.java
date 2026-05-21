package com.parque_industrial.persistence.publicaciones;

import com.parque_industrial.entities.Publicacion;
import java.util.List;

public interface PublicacionDAO {
    List<Publicacion> listarTodas();

    Publicacion guardar(Publicacion publicacion);

    boolean eliminar(Long id);
}
