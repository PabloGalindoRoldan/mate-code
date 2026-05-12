package com.parque_industrial.entities;

import java.util.ArrayList;
import java.util.List;

public interface GestorProyectos {
   // toodos estso metodos tienen que estar en la persistencia
   public void crearProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception;

   public void crearProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception;

   public void aprobarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception;

   public void aprobarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception;

   public void rechazarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception;

   public void rechazarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception;

   public void rectificarProyectoPreliminar(ProyectoPreliminar proyecto) throws Exception;

   public void rectificarProyectoDefinitivo(ProyectoDefinitivo proyecto) throws Exception;

   public List<ProyectoPreliminar> proyectosPreliminaresPorEstado(String estado) throws Exception;

   public List<ProyectoDefinitivo> proyectosDefinitivosPorEstado(String estado) throws Exception;
}
