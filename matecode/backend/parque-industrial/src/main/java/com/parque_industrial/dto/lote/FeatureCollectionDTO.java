package com.parque_industrial.dto.lote;

import java.util.List;

public class FeatureCollectionDTO {

    private final String type;
    private final List<FeatureDTO> features;

    public FeatureCollectionDTO(List<FeatureDTO> features) {
        this.type = "FeatureCollection";
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public List<FeatureDTO> getFeatures() {
        return features;
    }
}