import { useRef, useEffect, useState } from "react";
import Map, { Source, Layer, type MapRef } from "react-map-gl/maplibre";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import mapData from "../../assets/data/parqueIndustrialMap.json";

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);

    // Estado para controlar el tipo de mapa
    const [isSatellite, setIsSatellite] = useState(false);

    // URLs de los estilos
    const styleBase = "https://basemaps.cartocdn.com/gl/positron-nolabels-gl-style/style.json";
    const styleSatelite = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"; // Opción clara que incluye satélite/híbrido

    useEffect(() => {
        let animationFrame: number;
        const rotate = () => {
            const map = mapRef.current?.getMap();
            if (map && !map.isMoving() && !map.isRotating()) {
                map.setBearing(map.getBearing() + 0.08);
            }
            animationFrame = requestAnimationFrame(rotate);
        };
        rotate();
        return () => cancelAnimationFrame(animationFrame);
    }, []);

    return (
        <div className="mapContainer">
            {/* Botón de Toggle */}
            <button
                className="mapToggleButton"
                onClick={() => setIsSatellite(!isSatellite)}
            >
                {isSatellite ? "Ver Plano" : "Ver Satélite"}
            </button>

            <Map
                ref={mapRef}
                initialViewState={{
                    longitude: -62.964,
                    latitude: -40.852,
                    zoom: 15.2,
                    pitch: 45,
                }}
                // Cambia dinámicamente según el estado
                mapStyle={isSatellite ? styleSatelite : styleBase}
                style={{ width: "100%", height: "100%" }}
            >
                <Source id="lotes-source" type="geojson" data={mapData as any}>
                    <Layer
                        id="lotes-fill"
                        type="fill"
                        paint={{
                            "fill-color": "#FFFFFF",
                            "fill-opacity": isSatellite ? 0.2 : 0.4, // Menos opacidad en satélite para ver el suelo
                        }}
                    />

                    <Layer
                        id="lotes-outline"
                        type="line"
                        paint={{
                            "line-color": "#8CC63F",
                            "line-width": isSatellite ? 2 : 1, // Línea un poco más gruesa en satélite para resaltar
                        }}
                    />

                    <Layer
                        id="lotes-hover"
                        type="fill"
                        paint={{
                            "fill-color": "#75944D",
                            "fill-opacity": [
                                "case",
                                ["boolean", ["feature-state", "hover"], false],
                                0.3,
                                0
                            ],
                        }}
                    />
                </Source>
            </Map>
        </div>
    );
}