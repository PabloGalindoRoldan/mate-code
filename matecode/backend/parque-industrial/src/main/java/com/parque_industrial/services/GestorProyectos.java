package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.dto.proyecto.ProyectosDTO;

import java.util.List;

public interface GestorProyectos {
    void crearProyectoPreliminar(CrearRequestDTO dto);

    void crearProyectoDefinitivo(CrearRequestDefinitivoDTO request);

    ProyectosDTO listarProyectos();

    ProyectosDTO listarProyectosPorCuit(String cuit);

    void rectificarProyectoPreliminar(Integer proyectoId);

    void aprobarProyectoPreliminar(Integer proyectoId);

    void rechazarProyectoPreliminar(Integer proyectoId);

    void rechazarProyectoDefinitivo(Integer proyectoId);

    void rectificarProyectoDefinitivo(Integer proyectoId);

    void aprobarProyectoDefinitivo(Integer proyectoId);

    void ponerEnRevisionProyectoPreliminar(Integer proyectoId);

    void actualizarPreliminar(CrearRequestDTO request);

    void ponerEnRevisionProyectoDefinitivo(Integer proyectoId);

    void actualizarDefinitivo(CrearRequestDefinitivoDTO request);
}