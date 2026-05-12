package com.parque_industrial.persistence.usuario;

import com.parque_industrial.entities.Usuario;

public interface UsuarioDAO {

    public void guardar(Usuario usuario);

    //metodos como guardar, etc
    // es una interfaz por la logica de si el dia de manana queremos cambiar a otro tipo de persistencia, no tendriamos que cambiar nada en el codigo, solo la implementacion de esta interfaz
}
