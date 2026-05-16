import { useRef, useEffect, useState, useCallback } from "react";
import Map, { Source, Layer, type MapRef } from "react-map-gl/maplibre";
import { Map as MapIcon, ChevronRight } from "lucide-react";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import mapData from "../../assets/data/parqueIndustrialMap2.json";
import { useMap } from "./MapProvider";
import MapMenu from "./MapMenu";

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);
    const {
        isSatellite, rotationEnabled, isMapMenuOpen, setIsMapMenuOpen,
        showNuevo, showViejo, showStreets, showDisponible, showOcupado
    } = useMap();

    const [isInteracting, setIsInteracting] = useState(false);
    const [hoveredId, setHoveredId] = useState<string | number | null>(null);

    const styleBase = "https://basemaps.cartocdn.com/gl/positron-nolabels-gl-style/style.json";
    const styleSatelite = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json";

    // --- 1. LÓGICA DE COLOR DINÁMICA ---
    const dynamicColor: any = [
        "case",
        ["all", ["literal", showDisponible], ["==", ["get", "estado"], "disponible"]], "#76e3bb",
        ["all", ["literal", showOcupado], ["==", ["get", "estado"], "ocupado"]], "#FF8B94",
        ["all", ["literal", showStreets], ["==", ["get", "estado"], "infraestructura"]], "#808080",
        "#8CC63F"
    ];

    // --- 2. LÓGICA DE FILTRO (VISIBILIDAD) ---
    const mapFilter: any = [
        "all",
        ["case", ["==", ["get", "parque"], "nuevo"], ["literal", showNuevo], true],
        ["case", ["==", ["get", "parque"], "viejo"], ["literal", showViejo], true],
        ["case", ["==", ["get", "estado"], "infraestructura"], ["literal", showStreets], true]
    ];

    // Rotación automática del mapa
    useEffect(() => {
        let animationFrame: number;
        const rotate = () => {
            // Solo rota si NO hay interacción y la rotación está habilitada
            if (!isInteracting && rotationEnabled && mapRef.current) {
                const map = mapRef.current.getMap();
                map.setBearing(map.getBearing() + 0.08);
            }
            animationFrame = requestAnimationFrame(rotate);
        };
        rotate();
        return () => cancelAnimationFrame(animationFrame);
    }, [isInteracting, rotationEnabled]);

    // Handlers de interacción corregidos
    const startInteraction = useCallback(() => setIsInteracting(true), []);
    const stopInteraction = useCallback(() => setIsInteracting(false), []);

    const onHover = useCallback((event: any) => {
        const map = mapRef.current?.getMap();
        if (!map) return;
        const feature = event.features && event.features[0];
        const newHoveredId = feature ? feature.id : null;

        if (newHoveredId !== hoveredId) {
            if (hoveredId !== null) map.setFeatureState({ source: "lotes-source", id: hoveredId }, { hover: false });
            if (newHoveredId !== null) map.setFeatureState({ source: "lotes-source", id: newHoveredId }, { hover: true });
            setHoveredId(newHoveredId);
        }
    }, [hoveredId]);

    const onMouseLeave = useCallback(() => {
        const map = mapRef.current?.getMap();
        if (!map || hoveredId === null) return;
        map.setFeatureState({ source: "lotes-source", id: hoveredId }, { hover: false });
        setHoveredId(null);
        map.getCanvas().style.cursor = "";
    }, [hoveredId]);

    const onMouseEnter = useCallback(() => {
        const map = mapRef.current?.getMap();
        if (map) map.getCanvas().style.cursor = "pointer";
    }, []);

    return (
        <div className="mapContainer">
            <button
                className={`map-settings-btn ${isMapMenuOpen ? "map-settings-menu-open" : ""}`}
                onClick={() => setIsMapMenuOpen(!isMapMenuOpen)}
            >
                {isMapMenuOpen ? <ChevronRight size={20} /> : <MapIcon size={20} />}
            </button>

            <MapMenu {...useMap()} isOpen={isMapMenuOpen} />

            <Map
                ref={mapRef}
                initialViewState={{
                    longitude: -62.966,
                    latitude: -40.838,
                    zoom: 15.4,
                    pitch: 45
                }}
                mapStyle={isSatellite ? styleSatelite : styleBase}
                style={{ width: "100%", height: "100%" }}
                interactiveLayerIds={["lotes-fill"]}

                // Eventos de Mouse
                onMouseDown={startInteraction}
                onMouseUp={stopInteraction}

                // Eventos Táctiles (Celulares/Tablets)
                onTouchStart={startInteraction}
                onTouchEnd={stopInteraction}

                // Eventos de Cámara (Por si se mueve con teclado o gestos complejos)
                onMoveStart={startInteraction}
                onMoveEnd={stopInteraction}

                onMouseMove={onHover}
                onMouseLeave={onMouseLeave}
                onMouseEnter={onMouseEnter}
            >
                <Source id="lotes-source" type="geojson" data={mapData as any} promoteId="lote">
                    <Layer
                        id="lotes-outline"
                        type="line"
                        filter={mapFilter}
                        paint={{
                            "line-color": dynamicColor,
                            "line-width": isSatellite ? 2 : 1.2,
                            "line-opacity": 1
                        }}
                    />
                    <Layer
                        id="lotes-fill"
                        type="fill"
                        filter={mapFilter}
                        paint={{
                            "fill-color": dynamicColor,
                            "fill-opacity": [
                                "case",
                                ["boolean", ["feature-state", "hover"], false],
                                0.5,
                                0.05
                            ]
                        }}
                    />
                </Source>
            </Map>
        </div>
    );
}