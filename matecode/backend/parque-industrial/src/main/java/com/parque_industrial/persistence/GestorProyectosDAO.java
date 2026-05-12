package com.parque_industrial.persistence;

import com.parque_industrial.entities.GestorProyectos;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.entities.ProyectoPreliminar;

import java.sql.Connection;
import java.util.List;

public class GestorProyectosDAO implements GestorProyectos {

    private final Connection connection;

    private final ProyectoPreliminarJDBC preliminarDAO;
    private final ProyectoDefinitivoJDBC definitivoDAO;

    public GestorProyectosDAO(Connection connection) {
        this.connection = connection;
        this.preliminarDAO = new ProyectoPreliminarJDBC(connection);
        this.definitivoDAO = new ProyectoDefinitivoJDBC(connection);
    }

    // -------------------- CREAR --------------------

    @Override
    public void crearProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        preliminarDAO.guardar(proyecto);
    }

    @Override
    public void crearProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        definitivoDAO.guardar(proyecto);
    }

    // -------------------- PRELIMINAR --------------------

    @Override
    public void aprobarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        proyecto.aprobar();
        preliminarDAO.actualizar(proyecto);
    }

    @Override
    public void rechazarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        proyecto.rechazar();
        preliminarDAO.actualizar(proyecto);
    }

    @Override
    public void rectificarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception {
        proyecto.rectificar();
        preliminarDAO.actualizar(proyecto);
    }

    // -------------------- DEFINITIVO --------------------

    @Override
    public void aprobarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        proyecto.aprobar();
        definitivoDAO.actualizar(proyecto);
    }

    @Override
    public void rechazarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        proyecto.rechazar();
        definitivoDAO.actualizar(proyecto);
    }

    @Override
    public void rectificarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception {
        proyecto.rectificar();
        definitivoDAO.actualizar(proyecto);
    }

    // -------------------- CONSULTAS --------------------

    @Override
    public List<ProyectoPreliminar> proyectosPreliminaresPorEstado(String estado) throws Exception {
        return preliminarDAO.buscarPorEstado(estado);
    }

    @Override
    public List<ProyectoDefinitivo> proyectosDefinitivosPorEstado(String estado) throws Exception {
        return definitivoDAO.buscarPorEstado(estado);
    }
}