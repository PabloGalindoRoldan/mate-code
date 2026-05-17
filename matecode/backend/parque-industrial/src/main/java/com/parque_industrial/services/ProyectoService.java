package com.parque_industrial.services;

import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.entities.ProyectoPreliminar;
import com.parque_industrial.persistence.jdbc.ProyectoDefinitivoJDBC;
import com.parque_industrial.persistence.jdbc.ProyectoPreliminarJDBC;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoPreliminarJDBC preliminarDAO;
    private final ProyectoDefinitivoJDBC definitivoDAO;

    public ProyectoService(
            ProyectoPreliminarJDBC preliminarDAO,
            ProyectoDefinitivoJDBC definitivoDAO) {

        this.preliminarDAO = preliminarDAO;
        this.definitivoDAO = definitivoDAO;
    }

    // -------------------- CREAR --------------------

    public void crearProyectoPreliminar(
            ProyectoPreliminar proyecto) throws Exception {

        preliminarDAO.guardar(proyecto);
    }

    public void crearProyectoDefinitivo(
            ProyectoDefinitivo proyecto) throws Exception {

        definitivoDAO.guardar(proyecto);
    }

    // -------------------- PRELIMINAR --------------------

    public void aprobarProyectoPreliminar(
            ProyectoPreliminar proyecto) throws Exception {

        proyecto.aprobar();
        preliminarDAO.actualizar(proyecto);
    }

    public void rechazarProyectoPreliminar(
            ProyectoPreliminar proyecto) throws Exception {

        proyecto.rechazar();
        preliminarDAO.actualizar(proyecto);
    }

    // -------------------- DEFINITIVO --------------------

    public void aprobarProyectoDefinitivo(
            ProyectoDefinitivo proyecto) throws Exception {

        proyecto.aprobar();
        definitivoDAO.actualizar(proyecto);
    }

    public void rechazarProyectoDefinitivo(
            ProyectoDefinitivo proyecto) throws Exception {

        proyecto.rechazar();
        definitivoDAO.actualizar(proyecto);
    }

    // -------------------- CONSULTAS --------------------

    public List<ProyectoPreliminar>
    proyectosPreliminaresPorEstado(String estado)
            throws Exception {

        return preliminarDAO.buscarPorEstado(estado);
    }

    public List<ProyectoDefinitivo>
    proyectosDefinitivosPorEstado(String estado)
            throws Exception {

        return definitivoDAO.buscarPorEstado(estado);
    }
}