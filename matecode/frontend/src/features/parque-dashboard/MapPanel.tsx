import { useRef, useEffect, useState, useCallback, useMemo } from "react";
import Map, { Source, Layer, Popup, type MapRef } from "react-map-gl/maplibre";
import { Map as MapIcon, ChevronRight, Building2, Info, CheckCircle2, XCircle } from "lucide-react";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import { useMap } from "./MapProvider";
import MapMenu from "./MapMenu";
import LoadingSpinner from "../../ui/loading/LoadingSpinner";
import { lotesApi, empresasApi } from "../../api/axios";

interface EmpresaDTO {
    identificacion: string;
    razonSocial: string;
    esRadicada: boolean;
    idlote: number;
}

interface PopupInfo {
    longitude: number;
    latitude: number;
    properties: any;
    featureId: number | undefined;
}

// Reusable pure function to match map features to database companies
function findLinkedEmpresa(featureId: string | number | undefined, empresas: EmpresaDTO[]): EmpresaDTO | undefined {
    if (!empresas.length) return undefined;

    return empresas.find(emp => {
        if (emp.idlote === undefined || emp.idlote === null) return false;

        // 1. Direct match with top-level feature ID (as Number)
        if (featureId !== undefined && featureId !== null && Number(featureId) === Number(emp.idlote)) {
            return true;
        }

        // 2. Direct match with top-level feature ID (as String)
        if (featureId !== undefined && featureId !== null && String(featureId).trim() === String(emp.idlote).trim()) {
            return true;
        }

        return false;
    });
}

function getPolygonCentroid(geometry: any): [number, number] {
    if (!geometry || !geometry.coordinates) return [0, 0];
    let points: any[] = [];
    const { type, coordinates } = geometry;

    try {
        if (type === "Polygon") points = coordinates[0];
        else if (type === "MultiPolygon") points = coordinates[0][0];

        if (!Array.isArray(points) || points.length === 0) return [0, 0];

        let totalLng = 0, totalLat = 0, validCount = 0;

        for (let i = 0; i < points.length; i++) {
            const pt = points[i];
            if (pt && typeof pt[0] === "number" && typeof pt[1] === "number" && !isNaN(pt[0]) && !isNaN(pt[1])) {
                totalLng += pt[0];
                totalLat += pt[1];
                validCount++;
            }
        }

        if (validCount === 0) return [0, 0];
        return [totalLng / validCount, totalLat / validCount];
    } catch (error) {
        console.warn("Failed resolving geometry coordinates safely:", error);
        return [0, 0];
    }
}

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);
    const {
        isSatellite, rotationEnabled, isMapMenuOpen, setIsMapMenuOpen,
        showNuevo, showViejo, showStreets, showDisponible, showOcupado, showLabels,
    } = useMap();

    const [isInteracting, setIsInteracting] = useState(false);
    const [hoveredId, setHoveredId] = useState<string | number | null>(null);
    const [popupInfo, setPopupInfo] = useState<PopupInfo | null>(null);

    const [mapData, setMapData] = useState<any>(null);
    const [empresas, setEmpresas] = useState<EmpresaDTO[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    // Fetch initial layers and corporate entities
    useEffect(() => {
        const controller = new AbortController();
        setIsLoading(true);

        Promise.all([
            lotesApi.getMapaLotes({ signal: controller.signal }),
            empresasApi.listarEmpresas({ signal: controller.signal })
        ])
            .then(([geoJsonData, empresasData]) => {
                setMapData(geoJsonData);
                setEmpresas(empresasData);
            })
            .catch((err) => {
                if (err.name === "CanceledError" || err.message === "canceled") return;
                console.error("Error updating Map panel entities layers:", err);
            })
            .finally(() => {
                setIsLoading(false);
            });

        return () => controller.abort();
    }, []);

    // DERIVED STATE: Dynamically compute the active company on render without using a useEffect hook
    const activeEmpresa = useMemo(() => {
        if (!popupInfo) return undefined;
        return findLinkedEmpresa(popupInfo.featureId, empresas);
    }, [popupInfo, empresas]);

    // Mapbox Map rotation loop
    useEffect(() => {
        let animationFrame: number;
        const rotate = () => {
            if (!isInteracting && rotationEnabled && mapRef.current) {
                const map = mapRef.current.getMap();
                map.setBearing(map.getBearing() + 0.07);
            }
            animationFrame = requestAnimationFrame(rotate);
        };
        rotate();
        return () => cancelAnimationFrame(animationFrame);
    }, [isInteracting, rotationEnabled]);

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

    const onMapClick = useCallback((event: any) => {
        const feature = event.features && event.features[0];
        if (!feature || !feature.geometry) {
            setPopupInfo(null);
            return;
        }

        const [lng, lat] = getPolygonCentroid(feature.geometry);
        if (lng === 0 && lat === 0) {
            setPopupInfo(null);
            return;
        }

        setPopupInfo({
            longitude: lng,
            latitude: lat,
            featureId: feature.id,
            properties: feature.properties
        });
    }, []);

    const styleBase = "https://basemaps.cartocdn.com/gl/positron-nolabels-gl-style/style.json";
    const styleSatelite = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json";

    const dynamicColor: any = [
        "case",
        ["all", ["literal", showDisponible], ["==", ["get", "estado"], "disponible"]], "#76e3bb",
        ["all", ["literal", showOcupado], ["==", ["get", "estado"], "ocupado"]], "#FF8B94",
        ["all", ["literal", showStreets], ["==", ["get", "estado"], "infraestructura"]], "#808080",
        "#8CC63F"
    ];

    const mapFilter: any = [
        "all",
        ["case", ["==", ["get", "parque"], "nuevo"], ["literal", showNuevo], true],
        ["case", ["==", ["get", "parque"], "viejo"], ["literal", showViejo], true],
        ["case", ["==", ["get", "estado"], "infraestructura"], ["literal", showStreets], true]
    ];

    return (
        <div className="mapContainer" style={{ position: "relative" }}>
            {(isLoading || !mapData) && (
                <div style={{
                    position: "absolute",
                    top: 0, left: 0, width: "100%", height: "100%",
                    display: "flex", alignItems: "center", justifyContent: "center",
                    backgroundColor: "#ffffff", zIndex: 10
                }}>
                    <LoadingSpinner text="Cargando mapa e información del parque..." />
                </div>
            )}

            <button
                className={`map-settings-btn ${isMapMenuOpen ? "map-settings-menu-open" : ""}`}
                onClick={() => setIsMapMenuOpen(!isMapMenuOpen)}
                style={{ zIndex: 11 }}
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
                onMouseDown={startInteraction}
                onMouseUp={stopInteraction}
                onTouchStart={startInteraction}
                onTouchEnd={stopInteraction}
                onMoveStart={startInteraction}
                onMoveEnd={stopInteraction}
                onMouseMove={onHover}
                onMouseLeave={onMouseLeave}
                onMouseEnter={onMouseEnter}
                onClick={onMapClick}
            >
                {mapData && (
                    <>
                        <Source id="lotes-source" type="geojson" data={mapData}>
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
                            <Layer
                                id="lotes-text-labels"
                                type="symbol"
                                filter={[
                                    "all",
                                    mapFilter,
                                    ["==", ["match", ["get", "tipo"], "lote", true, false], true]
                                ] as any}
                                layout={{
                                    "text-field": ["get", "lote"],
                                    "text-size": 10,
                                    "text-anchor": "center",
                                    "text-justify": "center",
                                    "text-allow-overlap": false,
                                    "visibility": showLabels ? "visible" : "none"
                                }}
                                paint={{
                                    "text-color": "#1e293b",
                                    "text-halo-color": "#ffffff",
                                    "text-halo-width": 1.6
                                }}
                            />
                        </Source>

                        {popupInfo && (
                            <Popup
                                longitude={popupInfo.longitude}
                                latitude={popupInfo.latitude}
                                anchor="bottom"
                                onClose={() => setPopupInfo(null)}
                                closeOnClick={false}
                                className="lote-info-popup"
                            >
                                <div className="popup-wrapper">
                                    <header className="popup-header">
                                        <Info size={16} />
                                        <h3>Detalle del Lote {popupInfo.properties.lote}</h3>
                                    </header>

                                    <div className="popup-body">
                                        <section className="popup-section">
                                            <p><strong>Estado:</strong> <span className={`status-badge ${popupInfo.properties.estado}`}>{popupInfo.properties.estado}</span></p>
                                            <p><strong>Sector:</strong> {popupInfo.properties.parque === "nuevo" ? "Parque Nuevo" : "Parque Viejo"}</p>
                                            {popupInfo.properties.sup && (
                                                <p><strong>Superficie:</strong> {popupInfo.properties.sup} m²</p>
                                            )}
                                            {popupInfo.properties.nc != "N/A" && (
                                                <p><strong>N.C.:</strong> {popupInfo.properties.nc}</p>
                                            )}
                                        </section>

                                        {activeEmpresa ? (
                                            <section className="popup-section empresa-details">
                                                <h4><Building2 size={14} /> Información Empresarial</h4>
                                                <p><strong>Razón Social:</strong> {activeEmpresa.razonSocial}</p>
                                                <p><strong>CUIT:</strong> {activeEmpresa.identificacion}</p>
                                                <p className="radicada-status">
                                                    <strong>Radicada:</strong>{" "}
                                                    {activeEmpresa.esRadicada ? (
                                                        <span className="text-success"><CheckCircle2 size={12} /> Sí</span>
                                                    ) : (
                                                        <span className="text-danger"><XCircle size={12} /> No</span>
                                                    )}
                                                </p>
                                            </section>
                                        ) : (
                                            popupInfo.properties.estado === "ocupado" && (
                                                <p className="no-empresa-warning">No se encontraron datos de la firma asociada.</p>
                                            )
                                        )}
                                    </div>
                                </div>
                            </Popup>
                        )}
                    </>
                )}
            </Map>
        </div>
    );
}