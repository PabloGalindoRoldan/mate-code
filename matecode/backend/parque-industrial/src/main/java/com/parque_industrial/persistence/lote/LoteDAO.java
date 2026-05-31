package com.parque_industrial.persistence.lote;

import com.parque_industrial.entities.Lote;

import java.util.List;

public interface LoteDAO {

    // void crear(Lote lote);

    void actualizar(Lote lote);

    Lote buscarPorID(int id);

    List<Lote> buscarTodos();
}