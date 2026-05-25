package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.entities.ProyectoPreliminar;
import java.util.List;
import java.util.Optional;

public interface ProyectoDAO {
    void guardar(ProyectoPreliminar proyecto);

    Optional<ProyectoPreliminar> buscarPorId(Long id);

    List<ProyectoPreliminar> listarPorUsuario(String usuarioNombre);

    void actualizar(ProyectoPreliminar proyecto);

    void eliminar(Long id);
}