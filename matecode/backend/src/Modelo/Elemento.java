package Modelo;

public class Elemento {
    private String nombre;
    private boolean disponibilidad;

    public Elemento(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre del elemento no puede estar vacío.");
        }
        this.nombre = nombre;
        this.disponibilidad = true; // le asignamos true por defecto, si se crea es porque esta disponible
    }
    public void cambiarDisponibilidad() {
        this.disponibilidad = !this.disponibilidad;
    }
}
