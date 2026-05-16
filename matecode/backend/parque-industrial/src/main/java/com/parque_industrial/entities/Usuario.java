package com.parque_industrial.entities;
public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String nombreUsuario;
    private String contraseña;
    private String cuit;
    private Rol rol;
    private Empresa empresa;

    public Usuario(String nombre, String apellido, String email, String nombreUsuario,
                   String cuit, Rol rol, String contraseña, Empresa empresa) {
        validar(nombre);
        validar(apellido);
        validarEmail(email);
        validarUsuario(nombreUsuario);
        validarContraseña(contraseña);
        validarCuit(cuit);

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.cuit = cuit;
        this.rol = rol;
        this.empresa = empresa;
    }
    public Usuario(String nombre, String apellido, String email, String nombreUsuario,
                   String cuit, Rol rol, String contraseña) {
        validar(nombre);
        validar(apellido);
        validarEmail(email);
        validarUsuario(nombreUsuario);
        validarContraseña(contraseña);
        validarCuit(cuit);

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.cuit = cuit;
        this.rol = rol;
        this.empresa = null;
    }


    private void validar(String texto) { // Renombrado a 'texto' para ser más genérico
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El campo no puede ser nulo ni estar vacío");
        }
    }

    private void validarEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";//esto lo saque de un repo q mando el profe
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo ni estar vacío");
        }
        if (!email.matches(regex)) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }
    private void validarUsuario(String Usuario){

    }
    private void validarContraseña(String contraseña){

    }
    private void validarCuit(String cuit)  {
        if (cuit == null || cuit.isBlank()) {
            throw new IllegalArgumentException("El CUIT no puede estar vacío.");
        }
        //  formato  debe ser XX-XXXXXXXX-X
        // Esto valida que sean 2 dígitos, un guion, 8 dígitos, un guion y 1 dígito.
        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$";
        if (!cuit.matches(regexCuit)) {
            throw new IllegalArgumentException("El formato del CUIT es inválido. Debe ser XX-XXXXXXXX-X.");
        }
    }


    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getCuit() {
        return cuit;
    }

    public String getContraseña() {
        return contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
}