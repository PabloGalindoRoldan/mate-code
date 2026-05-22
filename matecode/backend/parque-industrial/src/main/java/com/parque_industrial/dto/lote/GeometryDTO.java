package com.parque_industrial.dto.lote;

public class GeometryDTO {

    private final String type;
    private final Object coordinates;

    public GeometryDTO(String type, Object coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public Object getCoordinates() {
        return coordinates;
    }
}
