package com.parque_industrial.services;

import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.persistence.proyecto.ProyectoDefinitivoJDBC;
import com.parque_industrial.persistence.proyecto.ProyectoPreliminarJDBC;

import java.sql.Connection;
import java.util.List;

public class GestorProyectos{
    private final Connection connection;

    private final ProyectoPreliminarJDBC preliminarDAO;
    private final ProyectoDefinitivoJDBC definitivoDAO;

    public GestorProyectos(Connection connection) {
        this.connection = connection;
        this.preliminarDAO = new ProyectoPreliminarJDBC(connection);
        this.definitivoDAO = new ProyectoDefinitivoJDBC(connection);
    }

    // -------------------- CREAR --------------------

    public void crearProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
      // recibe un dao, lo transforma y despues lo guarda.
        preliminarDAO.guardar(proyecto);
    }

    public void crearProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        definitivoDAO.guardar(proyecto);
    }

    // -------------------- PRELIMINAR --------------------

    public void aprobarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        proyecto.aprobar();
        preliminarDAO.actualizar(proyecto);
    }

    public void rechazarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        proyecto.rechazar();
        preliminarDAO.actualizar(proyecto);
    }

    public void rectificarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        proyecto.rectificar();
        preliminarDAO.actualizar(proyecto);
    }

    // -------------------- DEFINITIVO --------------------

    public void aprobarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        proyecto.aprobar();
        definitivoDAO.actualizar(proyecto);
    }

    public void rechazarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        proyecto.rechazar();
        definitivoDAO.actualizar(proyecto);
    }

    public void rectificarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        proyecto.rectificar();
        definitivoDAO.actualizar(proyecto);
    }

    // -------------------- CONSULTAS --------------------

    public List<ProyectoPreliminar> proyectosPreliminaresPorEstado(String estado) throws Exception {
        return preliminarDAO.buscarPorEstado(estado);
    }

    public List<ProyectoDefinitivo> proyectosDefinitivosPorEstado(String estado) throws Exception {
        return definitivoDAO.buscarPorEstado(estado);
    }
}
