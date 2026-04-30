package Modelo;
public abstract class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String nombreUsuario;
    private String contraseña;

    public Usuario(String nombre, String apellido, String email, String nombreUsuario, String contraseña) throws Exception {
        validar(nombre);
        validar(apellido);
        validarEmail(email);
        // estas dos no las implemente, deberiamos verificar que no ingrese un nombre de usuario que ya exista en la base de datos
        validarUsuario(nombreUsuario);
        validarContraseña(contraseña);

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
    }

    private void validar(String texto) throws Exception { // Renombrado a 'texto' para ser más genérico
        if (texto == null || texto.isBlank()) {
            throw new Exception("El campo no puede ser nulo ni estar vacío");
        }
    }

    private void validarEmail(String email) throws Exception {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";//esto lo saque de un repo q mando el profe
        if (email == null || email.isBlank()) {
            throw new Exception("El email no puede ser nulo ni estar vacío");
        }
        if (!email.matches(regex)) {
            throw new Exception("El formato del email no es válido");
        }
    }
    private void validarUsuario(String Usuario){

    }
    private void validarContraseña(String contraseña){

    }
}