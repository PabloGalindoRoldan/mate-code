package com.parque_industrial.services;

import com.parque_industrial.controllers.dtos.empresa.CargarConsumoRequest;
import com.parque_industrial.dto.empresa.ConsumoResponseDTO;
import com.parque_industrial.entities.Consumo;
import com.parque_industrial.persistence.consumos.ConsumoDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorConsumosImpl implements GestorConsumos {

    private final ConsumoDAO consumoDAO;

    // Inyección de dependencia por constructor
    public GestorConsumosImpl(ConsumoDAO consumoDAO) {
        this.consumoDAO = consumoDAO;
    }

    @Override
    public void registrarConsumoMensual(String cuitEmpresa, CargarConsumoRequest request) {
        // 1. Validar la regla de negocio: ¿Ya declaró este mes y año?
        if (consumoDAO.existePeriodo(cuitEmpresa, request.getMes(), request.getAno())) {
            throw new IllegalArgumentException(
                    "La empresa ya registró los consumos correspondientes al período: "
                            + request.getMes() + "/" + request.getAno());
        }

        // 2. Mapear el Request DTO a la Entidad de dominio
        Consumo nuevoConsumo = new Consumo();
        nuevoConsumo.setCuitEmpresa(cuitEmpresa);
        nuevoConsumo.setMes(request.getMes());
        nuevoConsumo.setAno(request.getAno());
        nuevoConsumo.setGas(request.getGas());
        nuevoConsumo.setLuz(request.getLuz());
        nuevoConsumo.setAgua(request.getAgua());
        nuevoConsumo.setEmpleados(request.getEmpleados());
        nuevoConsumo.setVehiculos(request.getVehiculos());

        // 3. Persistir en Railway usando JDBC
        consumoDAO.registrarConsumo(nuevoConsumo);
    }

    @Override
    public List<ConsumoResponseDTO> obtenerHistorialEmpresa(String cuitEmpresa) {
        // Recuperamos las entidades y las transformamos a DTOs usando Streams de Java
        return consumoDAO.obtenerHistorialPorEmpresa(cuitEmpresa)
                .stream()
                .map(ConsumoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsumoResponseDTO> obtenerReporteGlobalPorAno(int ano) {
        // Recuperamos los datos del parque completo del año solicitado
        return consumoDAO.obtenerConsumosGlobalesPorAno(ano)
                .stream()
                .map(ConsumoResponseDTO::new)
                .collect(Collectors.toList());
    }
}