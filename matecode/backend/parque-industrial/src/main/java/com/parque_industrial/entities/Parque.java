package com.parque_industrial.entities;

import com.parque_industrial.services.*;

import java.util.List;

public class Parque {
    private GestorInmobiliario gestorInmobiliarios;
    private AdministradorParque admin;
    private Inventario inventario;
    private GestorPresupuesto gestorPresupuesto;
    private GestorProyectos gestorProyectos;
    private List<RepresentanteEmpresa> representantes;
    private List<Empresa> empresas;

}
