package com.parque_industrial.services;

import java.sql.SQLException;
import java.util.List;

public interface DAOProyectos<T> {

    // GUARDAR
    void guardar(T proyecto) throws SQLException;

    // BUSCAR POR ID
    T buscarPorId(String identificacion) throws SQLException;

    // BUSCAR POR ESTADO
    List<T> buscarPorEstado(String estado) throws SQLException;

    // ACTUALIZAR
    void actualizar(T proyecto) throws SQLException;

    // ELIMINAR
    void eliminar(String identificacion) throws SQLException;
}