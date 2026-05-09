import { useRef, useEffect, useState, useCallback } from "react";
import Map, { Source, Layer, type MapRef } from "react-map-gl/maplibre";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import mapData from "../../assets/data/parqueIndustrialMap.json";
import { useMap } from "./MapProvider";

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);
    const { isSatellite, setIsSatellite, rotationEnabled, setRotationEnabled } = useMap();

    // States
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [isInteracting, setIsInteracting] = useState(false);
    const [hoveredId, setHoveredId] = useState<string | number | null>(null);

    const styleBase = "https://basemaps.cartocdn.com/gl/positron-nolabels-gl-style/style.json";
    const styleSatelite = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json";

    // --- AUTO-ROTATE LOGIC ---
    useEffect(() => {
        let animationFrame: number;
        const rotate = () => {
            // Only rotate if interaction is off AND rotation setting is ON
            if (!isInteracting && rotationEnabled && mapRef.current) {
                const map = mapRef.current.getMap();
                map.setBearing(map.getBearing() + 0.08);
            }
            animationFrame = requestAnimationFrame(rotate);
        };
        rotate();
        return () => cancelAnimationFrame(animationFrame);
    }, [isInteracting, rotationEnabled]);

    // --- HOVER HANDLERS ---
    const onHover = useCallback((event: any) => {
        const map = mapRef.current?.getMap();
        if (!map) return;

        const feature = event.features && event.features[0];
        const newHoveredId = feature ? feature.id : null;

        if (newHoveredId !== hoveredId) {
            if (hoveredId !== null) {
                map.setFeatureState({ source: "lotes-source", id: hoveredId }, { hover: false });
            }
            if (newHoveredId !== null) {
                map.setFeatureState({ source: "lotes-source", id: newHoveredId }, { hover: true });
            }
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
            {/* --- SETTINGS MENU --- */}
            <div className="mapControls">
                <button
                    className="mapToggleButton"
                    onClick={() => setIsMenuOpen(!isMenuOpen)}
                >
                    Configuración
                </button>

                {isMenuOpen && (
                    <div className="mapMenuDropdown">
                        <label className="menuItem">
                            <input
                                type="checkbox"
                                checked={isSatellite}
                                onChange={() => setIsSatellite(!isSatellite)}
                            />
                            Vista Satélite
                        </label>
                        <label className="menuItem">
                            <input
                                type="checkbox"
                                checked={rotationEnabled}
                                onChange={() => setRotationEnabled(!rotationEnabled)}
                            />
                            Rotación Automática
                        </label>
                    </div>
                )}
            </div>

            <Map
                ref={mapRef}
                initialViewState={{
                    longitude: -62.964,
                    latitude: -40.840,
                    zoom: 15.4,
                    pitch: 45,
                }}
                mapStyle={isSatellite ? styleSatelite : styleBase}
                style={{ width: "100%", height: "100%" }}

                interactiveLayerIds={["lotes-fill"]}
                onMouseMove={onHover}
                onMouseLeave={onMouseLeave}
                onMouseEnter={onMouseEnter}

                onMouseDown={() => setIsInteracting(true)}
                onMouseUp={() => setIsInteracting(false)}
                onDragStart={() => setIsInteracting(true)}
                onDragEnd={() => setIsInteracting(false)}
                onZoomStart={() => setIsInteracting(true)}
                onZoomEnd={() => setIsInteracting(false)}
                onTouchStart={() => setIsInteracting(true)}
                onTouchEnd={() => setIsInteracting(false)}
            >
                <Source id="lotes-source" type="geojson" data={mapData as any} promoteId="id">
                    <Layer
                        id="lotes-fill"
                        type="fill"
                        paint={{
                            "fill-color": "#FFFFFF",
                            "fill-opacity": isSatellite ? 0.2 : 0.4,
                        }}
                    />
                    <Layer
                        id="lotes-outline"
                        type="line"
                        paint={{
                            "line-color": "#8CC63F",
                            "line-width": isSatellite ? 2 : 1,
                        }}
                    />
                    <Layer
                        id="lotes-hover"
                        type="fill"
                        paint={{
                            "fill-color": "#8CC63F",
                            "fill-opacity": ["case", ["boolean", ["feature-state", "hover"], false], 0.5, 0],
                        }}
                    />
                </Source>
            </Map>
        </div>
    );
}