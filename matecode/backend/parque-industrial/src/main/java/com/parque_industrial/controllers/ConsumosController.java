package com.parque_industrial.controllers;

import com.parque_industrial.controllers.dtos.consumos.*;
import com.parque_industrial.persistence.dtos.ConsumosDTO;
import com.parque_industrial.persistence.dtos.LoteDTO;
import com.parque_industrial.services.GestorConsumos;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consumos")
public class ConsumosController {
    private final GestorConsumos gestor;

    public ConsumosController(GestorConsumos gestorConsumos) {
        this.gestor = gestorConsumos;
    }

    @PostMapping("/registrar")
    public void registrarConsumo(@RequestBody   CrearRequestDTO d) {
            ConsumosDTO consumosDTO = new ConsumosDTO(d.luz(), d.gas(), d.agua(), d.residuos(), d.cantEmpleados(), d.cantVehiculos(), d.idEmpresa());
            gestor.registrarConsumos(consumosDTO);
    }
    @PostMapping("/asignarEmpleados")
    public void asignarCantEmpleados(@RequestBody AsignarCantEmpleadosRequestDTO d) {
        gestor.asignarCantEmpleados(d.idEmpresa(), d.cantEmpleados());
    }
    @PostMapping("/asignarVehiculos")
    public void asignarCantVehiculos(@RequestBody AsignarCantVehiculosRequestDTO d) {
        gestor.asignarCantVheiculos(d.idEmpresa(), d.cantVehiculos());
    }
    @PostMapping("/ASigarGas")
    public void asignarCantGas(@RequestBody AsignarGasRequestDTO d) {
        gestor.asignarConsumoGas(d.idEmpresa(), d.gas());
    }
    @PostMapping("/ASigarLuz")
    public void asignarCantLuz(@RequestBody AsignarLuzRequestDTO d) {
        gestor.asignarConsumoLuz(d.idEmpresa(), d.luz());
    }
    @PostMapping("/ASigarAgua")
    public void asignarCantAgua(@RequestBody AsignarAguaRequestDTO d) {
        gestor.asignarConsumoGas(d.idEmpresa(), d.agua());
    }
    @PostMapping("/AsigarResiduos")    public void asignarCantResiduos(@RequestBody AsignarResiduosRequestDTO d) {
        gestor.asignarConsumoResiduos(d.cuit(), d.kg());
    }
}