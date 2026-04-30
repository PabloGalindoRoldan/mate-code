package Modelo;

import java.util.ArrayList;
import java.util.List;

public interface GestorProyectos {
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
