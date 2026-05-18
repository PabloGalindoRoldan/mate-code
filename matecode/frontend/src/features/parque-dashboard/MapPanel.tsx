import { useRef, useEffect, useState, useCallback } from "react";
import Map, { Source, Layer, type MapRef } from "react-map-gl/maplibre";
import { Map as MapIcon, ChevronRight } from "lucide-react";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import mapData from "../../assets/data/parqueIndustrialMap2.json";
import { useMap } from "./MapProvider";
import MapMenu from "./MapMenu";
import LoadingSpinner from "../../ui/loading/LoadingSpinner"; // Importamos tu nuevo loader

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);
    const {
        isSatellite, rotationEnabled, isMapMenuOpen, setIsMapMenuOpen,
        showNuevo, showViejo, showStreets, showDisponible, showOcupado
    } = useMap();

    const [isInteracting, setIsInteracting] = useState(false);
    const [hoveredId, setHoveredId] = useState<string | number | null>(null);

    // Estado para controlar el delay de inicialización/renderizado del mapa
    const [isLoading, setIsLoading] = useState(true);

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
            if (!isInteracting && rotationEnabled && mapRef.current) {
                const map = mapRef.current.getMap();
                map.setBearing(map.getBearing() + 0.08);
            }
            animationFrame = requestAnimationFrame(rotate);
        };
        rotate();
        return () => cancelAnimationFrame(animationFrame);
    }, [isInteracting, rotationEnabled]);

    // Handlers de interacción
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
        <div className="mapContainer" style={{ position: "relative" }}>
            {/* Si está cargando el render, clavamos el spinner inline centrado en el contenedor del mapa */}
            {isLoading && (
                <div style={{
                    position: "absolute",
                    top: 0, left: 0, width: "100%", height: "100%",
                    display: "flex", alignItems: "center", justifyContent: "center",
                    backgroundColor: "#ffffff", zIndex: 10
                }}>
                    <LoadingSpinner text="Cargando mapa de lotes..." />
                </div>
            )}

            <button
                className={`map-settings-btn ${isMapMenuOpen ? "map-settings-menu-open" : ""}`}
                onClick={() => setIsMapMenuOpen(!isMapMenuOpen)}
                style={{ zIndex: 11 }} // Lo subimos un nivel por encima del loading
            >
                {isMapMenuOpen ? <ChevronRight size={20} /> : <MapIcon size={20} />}
            </button>

            <MapMenu {...useMap()} isOpen={isMapMenuOpen} />

            <Map
                ref={mapRef}
                initialViewState={{
                    longitude: -62.963,
                    latitude: -40.840,
                    zoom: 15.3,
                    pitch: 45
                }}
                mapStyle={isSatellite ? styleSatelite : styleBase}
                style={{ width: "100%", height: "100%" }}
                interactiveLayerIds={["lotes-fill"]}

                // Desactivamos el loading cuando MapLibre termine de renderizar la primera vista estable
                onIdle={() => setIsLoading(false)}

                // Eventos de Mouse
                onMouseDown={startInteraction}
                onMouseUp={stopInteraction}

                // Eventos Táctiles
                onTouchStart={startInteraction}
                onTouchEnd={stopInteraction}

                // Eventos de Cámara
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