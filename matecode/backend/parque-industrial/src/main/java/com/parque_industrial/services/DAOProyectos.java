package com.parque_industrial.services;

import com.parque_industrial.entities.Proyecto;
import com.parque_industrial.entities.ProyectoDefinitivo;
import com.parque_industrial.entities.ProyectoPreliminar;

import java.util.List;
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla

public interface DAOProyectos {
   // toodos estso metodos tienen que estar en la persistencia
   public void crearProyectoPreeliminar( ProyectoPreliminar proyecto);
   public void crearProyectoDefinitivo( ProyectoDefinitivo proyecto);
   public void aprobarProyecto(Proyecto proyecto);
   public void rechazarProyecto(Proyecto proyecto);
   public void rectificarProyecto(Proyecto proyecto);
   public List<Proyecto> proyectosPendientes();
   public List<Proyecto> proyectosAprobados();
   public List<Proyecto> proyectosRechazados();
   public List<Proyecto> proyectosRectificados();
}
