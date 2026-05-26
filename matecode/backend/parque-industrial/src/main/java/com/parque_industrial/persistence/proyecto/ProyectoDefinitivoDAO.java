package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.entities.ProyectoDefinitivo;

import java.util.List;

public interface ProyectoDefinitivoDAO {

    List<CrearRequestDefinitivoDTO> listarDefinitivosPorCuit(String cuit);

    List<CrearRequestDefinitivoDTO> listarProyectos();
    void guardarProyectoDefinitivo(ProyectoDefinitivo entidad);

}

