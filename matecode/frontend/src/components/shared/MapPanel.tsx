import { useRef, useEffect, useState, useCallback, useMemo } from "react";
import Map, { Source, Layer, Popup, type MapRef } from "react-map-gl/maplibre";
import { Map as MapIcon, ChevronRight, Loader2 } from "lucide-react";
import centroid from "@turf/centroid";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import mapData from "../../assets/data/parqueIndustrialMap2.json";
import { useMap } from "./MapProvider";
import MapMenu from "./MapMenu";

// Define the shape of our popup state
interface PopupInfo {
    longitude: number;
    latitude: number;
    properties: any;
    extraData?: any;
}

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);
    const {
        isSatellite, rotationEnabled, isMapMenuOpen, setIsMapMenuOpen,
        showNuevo, showViejo, showStreets, showDisponible, showOcupado
    } = useMap();

    const [isInteracting, setIsInteracting] = useState(false);
    const [hoveredId, setHoveredId] = useState<string | number | null>(null);
    const [popupInfo, setPopupInfo] = useState<PopupInfo | null>(null);
    const [isLoadingExtra, setIsLoadingExtra] = useState(false);

    const styleBase = "https://basemaps.cartocdn.com/gl/positron-nolabels-gl-style/style.json";
    const styleSatelite = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json";

    // --- 1. STYLES & FILTERS ---
    const dynamicColor: any = useMemo(() => [
        "case",
        ["all", ["literal", showOcupado], ["==", ["get", "estado"], "ocupado"]], "#FF8B94",
        ["all", ["literal", showStreets], ["==", ["get", "estado"], "infraestructura"]], "#808080",
        "#8CC63F"
    ], [showDisponible, showOcupado, showStreets]);

    const mapFilter: any = useMemo(() => [
        "all",
        ["case", ["==", ["get", "parque"], "nuevo"], ["literal", showNuevo], true],
        ["case", ["==", ["get", "parque"], "viejo"], ["literal", showViejo], true],
        ["case", ["==", ["get", "estado"], "infraestructura"], ["literal", showStreets], true]
    ], [showNuevo, showViejo, showStreets]);

    // --- 2. AUTO-ROTATION ---
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

    // --- 3. INTERACTION HANDLERS ---
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

    const onClick = useCallback(async (event: any) => {
        const feature = event.features && event.features[0];
        if (!feature) {
            setPopupInfo(null);
            return;
        }

        // Calculate center using turf
        const polyCentroid = centroid(feature);
        const [lng, lat] = polyCentroid.geometry.coordinates;

        setPopupInfo({
            longitude: lng,
            latitude: lat,
            properties: feature.properties
        });

        // --- MOCK: FETCH DATA FROM OTHER SOURCES ---
        setIsLoadingExtra(true);
        try {
            // Simulate an API call using the ID from GeoJSON
            // const response = await fetch(`/api/lotes/${feature.properties.lote}`);
            // const extraData = await response.json();

            await new Promise(resolve => setTimeout(resolve, 800)); // simulate delay
            const mockExtra = { price: "$50,000", owner: "Inversiones S.A." };

            setPopupInfo(prev => prev ? { ...prev, extraData: mockExtra } : null);
        } catch (error) {
            console.error("Error fetching extra data", error);
        } finally {
            setIsLoadingExtra(false);
        }
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

                onMouseDown={startInteraction}
                onMouseUp={stopInteraction}
                onMoveStart={startInteraction}
                onMoveEnd={stopInteraction}

                onMouseMove={onHover}
                onMouseLeave={() => {
                    setHoveredId(null);
                    if (mapRef.current) mapRef.current.getMap().getCanvas().style.cursor = "";
                }}
                onMouseEnter={() => {
                    if (mapRef.current) mapRef.current.getMap().getCanvas().style.cursor = "pointer";
                }}
                onClick={onClick}
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

                {/* --- POPUP COMPONENT --- */}
                {popupInfo && (
                    <Popup
                        longitude={popupInfo.longitude}
                        latitude={popupInfo.latitude}
                        anchor="bottom"
                        onClose={() => setPopupInfo(null)}
                        closeOnClick={false}
                        className="custom-popup"
                    >
                        <div className="popup-content">
                            <h4 className="font-bold border-b mb-2">Lote: {popupInfo.properties.lote}</h4>
                            <div className="text-sm">
                                <p><strong>Estado:</strong> {popupInfo.properties.estado}</p>
                                <p><strong>Parque:</strong> {popupInfo.properties.parque}</p>

                                <hr className="my-2" />

                                {isLoadingExtra ? (
                                    <div className="flex items-center gap-2 text-gray-500">
                                        <Loader2 size={14} className="animate-spin" />
                                        <span>Cargando detalles...</span>
                                    </div>
                                ) : popupInfo.extraData ? (
                                    <div className="bg-blue-50 p-2 rounded">
                                        <p><strong>Valor:</strong> {popupInfo.extraData.price}</p>
                                        <p><strong>Titular:</strong> {popupInfo.extraData.owner}</p>
                                    </div>
                                ) : null}
                            </div>
                        </div>
                    </Popup>
                )}
            </Map>
        </div>
    );
}