package com.parque_industrial.persistence.inventario;

import com.parque_industrial.entities.Elemento;
import java.util.List;
import java.util.Optional;

public interface InventarioDAO {
    Elemento guardar(Elemento elemento);

    Optional<Elemento> buscarPorId(Integer id);

    List<Elemento> listarTodos();

    List<Elemento> listarActivos();

    void actualizar(Elemento elemento);
}