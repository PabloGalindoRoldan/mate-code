package com.parque_industrial.persistence.consumos;

import com.parque_industrial.entities.Consumo;
import java.util.List;
import java.util.Optional;

public interface ConsumoDAO {

    // Guarda el consumo del mes declarado por la empresa
    void registrarConsumo(Consumo consumo);

    // Busca si ya existe una carga para esa empresa en ese mes/año específico
    boolean existePeriodo(String cuitEmpresa, int mes, int ano);

    // Trae el historial completo de declaraciones de una sola empresa (ordenado por
    // fecha)
    List<Consumo> obtenerHistorialPorEmpresa(String cuitEmpresa);

    // Trae el último consumo cargado por la empresa (ideal para el "arrastre de
    // datos")
    Optional<Consumo> obtenerUltimoConsumo(String cuitEmpresa);

    // Trae todos los consumos del parque para un año específico (para los reportes
    // del Admin)
    List<Consumo> obtenerConsumosGlobalesPorAno(int ano);
}