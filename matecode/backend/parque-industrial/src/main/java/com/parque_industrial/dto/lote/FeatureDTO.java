package com.parque_industrial.dto.lote;

public class FeatureDTO {

    private final String type;
    private final PropertiesDTO properties;
    private final GeometryDTO geometry;
    private final int id;

    public FeatureDTO(PropertiesDTO properties, GeometryDTO geometry, int id) {
        this.type = "Feature";
        this.properties = properties;
        this.geometry = geometry;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public PropertiesDTO getProperties() {
        return properties;
    }

    public GeometryDTO getGeometry() {
        return geometry;
    }

    public int getId() {
        return id;
    }
}