package com.parque_industrial.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parque_industrial.dto.lote.*;
import com.parque_industrial.entities.Lote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeoJsonMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FeatureCollectionDTO convertirLista(List<Lote> lotes) {

        List<FeatureDTO> features = lotes.stream().map(this::convertir).toList();

        return new FeatureCollectionDTO(features);
    }

    public FeatureDTO convertir(Lote lote) {

        Object coordinates;

        try {

            coordinates = objectMapper.readValue(lote.getCoordinates(), Object.class);

        } catch (Exception e) {

            throw new RuntimeException("Error convirtiendo coordinates JSON");
        }

        PropertiesDTO properties = new PropertiesDTO(lote.getSuperficie(), lote.getTipo(), lote.getEstado(), lote.getFechaVenta(), lote.getMontoVenta(), lote.getParque(), lote.getNc(), lote.getNroLote());
        GeometryDTO geometry = new GeometryDTO("Polygon", coordinates);

        return new FeatureDTO(properties, geometry, lote.getIdentificacion());
    }
}