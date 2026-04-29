package Modelo;

import java.util.List;
import java.util.ArrayList;

public class AdministradorParque extends Usuario {

    public AdministradorParque(String nombre, String apellido, String email, String nombreUsuario, String contraseña) {
        super(nombre, apellido, email, nombreUsuario, contraseña);
    }
    //
    //entiendo que el admin es quien decide si el proyecto se aprueba rechaza o rectifica,
    // si nosotros pusismos q el gestor de proyectos iba a contener todos los proyectos, entonces quien contenga un gestor
    // de proyectos va a ser el admin, o el gestor va a tener administradores?
}
