import { useRef, useEffect, useState, useCallback, useMemo } from "react";
import Map, { Source, Layer, Popup, type MapRef } from "react-map-gl/maplibre";
import {
    Map as MapIcon, ChevronRight, Building2, Info, CalendarDays, Users, Truck, X, Maximize2, Zap, Droplet, Flame
} from "lucide-react";
import "maplibre-gl/dist/maplibre-gl.css";
import "./MapPanel.css";
import { useMap } from "./MapProvider";
import MapMenu from "./MapMenu";
import LoadingSpinner from "../../ui/loading/LoadingSpinner";
import { lotesApi, empresasApi, consumosApi } from "../../api/axios";

interface EmpresaDTO {
    identificacion: string;
    razonSocial: string;
    esRadicada: boolean;
    idlote: number;
    [key: string]: any;
}

interface PopupInfo {
    longitude: number;
    latitude: number;
    properties: any;
    featureId: number | undefined;
}

interface ModalData {
    properties: any;
    activeEmpresa: EmpresaDTO | undefined;
}

interface ConsumoRecord {
    id?: number;
    mes: number;
    ano: number;
    luz?: { source: string; parsedValue: number } | number;
    agua?: { source: string; parsedValue: number } | number;
    gas?: { source: string; parsedValue: number } | number;
    empleados?: number;
    vehiculos?: number;
    [key: string]: any;
}

function findLinkedEmpresa(featureId: string | number | undefined, empresas: EmpresaDTO[]): EmpresaDTO | undefined {
    if (!empresas.length) return undefined;
    return empresas.find(emp => {
        if (emp.idlote === undefined || emp.idlote === null) return false;
        const idLoteNum = Number(emp.idlote);
        const featIdNum = Number(featureId);
        if (!isNaN(featIdNum) && !isNaN(idLoteNum) && featIdNum === idLoteNum) return true;
        return String(featureId).trim() === String(emp.idlote).trim();
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

const NOMBRE_MESES = [
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];

const LABELS_MAP: Record<string, string> = {
    sup: "Superficie (m²)",
    tipo: "Tipo de Poligono",
    estado: "Estado de ocupacion del lote",
    parque: "Parque Nuevo o Viejo",
    nc: "Nomenclatura Catastral",
    lote: "Número de Lote",
    identificacion: "CUIT",
    razonSocial: "Razón Social",
    esRadicada: "Está Radicada",
    idlote: "Número de Lote"
};

export default function MapPanel() {
    const mapRef = useRef<MapRef>(null);
    const {
        isSatellite, rotationEnabled, isMapMenuOpen, setIsMapMenuOpen,
        showNuevo, showViejo, showStreets, showDisponible, showOcupado, showLabels,
    } = useMap();

    const [isInteracting, setIsInteracting] = useState(false);
    const [hoveredId, setHoveredId] = useState<string | number | null>(null);
    const [popupInfo, setPopupInfo] = useState<PopupInfo | null>(null);

    // Diccionario de ventanas abiertas indexadas por ID de lote/feature para permitir multi-ventana
    const [openModals, setOpenModals] = useState<Record<string | number, ModalData>>({});

    const [mapData, setMapData] = useState<any>(null);
    const [empresas, setEmpresas] = useState<EmpresaDTO[]>([]);
    const [isLoading, setIsLoading] = useState(true);

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

    const activeEmpresa = useMemo(() => {
        if (!popupInfo) return undefined;
        return findLinkedEmpresa(popupInfo.featureId, empresas);
    }, [popupInfo, empresas]);

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

    const openExtendedPanel = (loteId: string | number, properties: any, empresa: EmpresaDTO | undefined) => {
        setOpenModals(prev => ({
            ...prev,
            [loteId]: { properties, activeEmpresa: empresa }
        }));
    };

    const closeExtendedPanel = (loteId: string | number) => {
        setOpenModals(prev => {
            const updated = { ...prev };
            delete updated[loteId];
            return updated;
        });
    };

    return (
        <div className="mapContainer" style={{ position: "relative", width: "100%", height: "100%", overflow: "hidden" }}>
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
                                    "text-field": ["id"],
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
                                        <h3>Lote {popupInfo.featureId}</h3>
                                    </header>

                                    <div className="popup-body">
                                        <p><strong>Estado:</strong> <span className={`status-badge ${popupInfo.properties.estado}`}>{popupInfo.properties.estado}</span></p>

                                        {/* Nuevos datos agregados al cuerpo del popup */}
                                        {popupInfo.properties.sup && (
                                            <p><strong>Superficie:</strong> {popupInfo.properties.sup} m²</p>
                                        )}

                                        {popupInfo.properties.nc && popupInfo.properties.nc.trim().toUpperCase() !== "N/A" && (
                                            <p><strong>NC:</strong> {popupInfo.properties.nc}</p>
                                        )}

                                        {activeEmpresa ? (
                                            <p><strong>Empresa:</strong> {activeEmpresa.razonSocial}</p>
                                        ) : (
                                            popupInfo.properties.estado === "ocupado" && (
                                                <p className="no-empresa-warning">Ocupado (Sin firma vinculada)</p>
                                            )
                                        )}

                                        <button
                                            className="popup-ver-mas-btn"
                                            onClick={() => {
                                                const idKey = popupInfo.featureId ?? popupInfo.properties.lote;
                                                openExtendedPanel(idKey, popupInfo.properties, activeEmpresa);
                                            }}
                                        >
                                            Ver más detalles
                                        </button>
                                    </div>
                                </div>
                            </Popup>
                        )}
                    </>
                )}
            </Map>

            {/* --- RENDERIZADO DE VENTANAS MÚLTIPLES --- */}
            {Object.entries(openModals).map(([loteId, data], index) => (
                <ModalPanel
                    key={loteId}
                    loteId={loteId}
                    data={data}
                    index={index}
                    onClose={closeExtendedPanel}
                />
            ))}
        </div>
    );
}

// --- SUBCOMPONENTE DE EXPEDIENTE FLOTANTE INDEPENDIENTE ---
interface ModalPanelProps {
    loteId: string | number;
    data: ModalData;
    index: number;
    onClose: (loteId: string | number) => void;
}

function ModalPanel({ loteId, data, index, onClose }: ModalPanelProps) {
    const [position, setPosition] = useState({ x: 60 + index * 25, y: 80 + index * 25 });
    const [isDragging, setIsDragging] = useState(false);
    const dragStart = useRef({ x: 0, y: 0 });

    const [ultimoConsumo, setUltimoConsumo] = useState<ConsumoRecord | null>(null);
    const [isConsumosLoading, setIsConsumosLoading] = useState(false);

    useEffect(() => {
        if (!data.activeEmpresa?.identificacion) {
            setUltimoConsumo(null);
            return;
        }

        const controller = new AbortController();
        setIsConsumosLoading(true);

        consumosApi.getHistorialPorEmpresa(data.activeEmpresa.identificacion)
            .then((historial: ConsumoRecord[]) => {
                if (historial && historial.length > 0) {
                    const ordenado = [...historial].sort((a, b) => {
                        if (a.ano !== b.ano) return b.ano - a.ano;
                        return b.mes - a.mes;
                    });
                    setUltimoConsumo(ordenado[0]);
                } else {
                    setUltimoConsumo(null);
                }
            })
            .catch((err) => {
                console.error(`Error consultando consumos de la empresa en lote ${loteId}:`, err);
                setUltimoConsumo(null);
            })
            .finally(() => {
                setIsConsumosLoading(false);
            });

        return () => controller.abort();
    }, [data.activeEmpresa?.identificacion, loteId]);

    // --- Lógica del arrastre individual ---
    const handleMouseDown = (e: React.MouseEvent) => {
        // Evitamos que el arrastre empiece si se hace click en el botón de cerrar
        if ((e.target as HTMLElement).closest("button")) return;
        e.stopPropagation();

        setIsDragging(true);
        dragStart.current = {
            x: e.clientX - position.x,
            y: e.clientY - position.y
        };
    };

    const handleMouseMove = useCallback((e: MouseEvent) => {
        if (!isDragging) return;
        setPosition({
            x: e.clientX - dragStart.current.x,
            y: e.clientY - dragStart.current.y
        });
    }, [isDragging]);

    const handleMouseUp = useCallback(() => {
        setIsDragging(false);
    }, []);

    useEffect(() => {
        if (isDragging) {
            window.addEventListener("mousemove", handleMouseMove);
            window.addEventListener("mouseup", handleMouseUp);
        }
        return () => {
            window.removeEventListener("mousemove", handleMouseMove);
            window.removeEventListener("mouseup", handleMouseUp);
        };
    }, [isDragging, handleMouseMove, handleMouseUp]);

    const getLuzValue = (record: ConsumoRecord) => typeof record.luz === "object" ? record.luz.parsedValue : record.luz;
    const getAguaValue = (record: ConsumoRecord) => typeof record.agua === "object" ? record.agua.parsedValue : record.agua;
    const getGasValue = (record: ConsumoRecord) => typeof record.gas === "object" ? record.gas.parsedValue : record.gas;

    return (
        <div
            className="draggable-modal-panel"
            // Capturamos cualquier mousedown dentro del cuerpo del modal para que tampoco mueva el mapa
            onMouseDown={(e) => e.stopPropagation()}
            style={{
                position: "absolute",
                left: `${position.x}px`,
                top: `${position.y}px`,
                zIndex: 12 + index,
                cursor: isDragging ? "grabbing" : "default"
            }}
        >
            <header
                className="modal-panel-header"
                onMouseDown={handleMouseDown}
                style={{ cursor: "grab" }}
            >
                <div className="modal-header-title">
                    <Building2 size={16} />
                    <span>Expediente Completo: Lote {loteId}</span>
                </div>
                <button className="close-modal-btn" onClick={() => onClose(loteId)}>
                    <X size={16} />
                </button>
            </header>

            <div className="modal-panel-body">
                <section className="modal-data-section">
                    <h4>Datos Técnicos del Lote</h4>
                    <div className="modal-dynamic-grid">
                        {Object.entries(data.properties)
                            .filter(([key]) => key !== "lote")
                            .map(([key, value]) => (
                                <div key={key} className="data-box">
                                    <span className="data-key">{LABELS_MAP[key] || key}:</span>
                                    <span className="data-value">{String(value)}</span>
                                </div>
                            ))}
                    </div>
                </section>

                <section className="modal-data-section">
                    <h4>Ficha Completa de la Empresa</h4>
                    {data.activeEmpresa ? (
                        <div className="modal-dynamic-grid">
                            {Object.entries(data.activeEmpresa).map(([key, value]) => (
                                <div key={key} className="data-box">
                                    <span className="data-key">
                                        {LABELS_MAP[key] || key}
                                    </span>
                                    <span className="data-value">
                                        {typeof value === "boolean"
                                            ? (value ? "Sí" : "No")
                                            : String(value ?? "N/A")}
                                    </span>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p className="text-muted text-sm">Este lote no registra una empresa activa actualmente.</p>
                    )}
                </section>

                <section className="modal-data-section">
                    <h4>Último Balance de Consumos e Indicadores</h4>
                    {isConsumosLoading ? (
                        <p className="consumos-loading">Cargando mediciones desde la API...</p>
                    ) : ultimoConsumo ? (
                        <div className="modal-consumos-wrapper">
                            <div className="modal-periodo-badge">
                                <CalendarDays size={14} />
                                <span>Período: {NOMBRE_MESES[ultimoConsumo.mes - 1]} / {ultimoConsumo.ano}</span>
                            </div>

                            <div className="modal-indicators-grid">
                                <div className="indicator-card energy">
                                    <Zap size={16} color="#fbff00" />
                                    <div className="info">
                                        <span className="title">Energía</span>
                                        <span className="val">{getLuzValue(ultimoConsumo) ?? 0} kWh</span>
                                    </div>
                                </div>
                                <div className="indicator-card water">
                                    <Droplet size={16} color="#00bfff" />
                                    <div className="info">
                                        <span className="title">Agua Potable</span>
                                        <span className="val">{getAguaValue(ultimoConsumo) ?? 0} m³</span>
                                    </div>
                                </div>
                                <div className="indicator-card gas">
                                    <Flame size={16} color="#ff6b35" />
                                    <div className="info">
                                        <span className="title">Gas Gasoducto</span>
                                        <span className="val">{getGasValue(ultimoConsumo) ?? 0} m³</span>
                                    </div>
                                </div>
                                <div className="indicator-card staff">
                                    <Users size={16} color="#808080" />
                                    <div className="info">
                                        <span className="title">Nómina Personal</span>
                                        <span className="val">{ultimoConsumo.empleados ?? 0} operarios</span>
                                    </div>
                                </div>
                                <div className="indicator-card vehicles">
                                    <Truck size={16} color="#ad11cc" />
                                    <div className="info">
                                        <span className="title">Flota Activa</span>
                                        <span className="val">{ultimoConsumo.vehiculos ?? 0} unidades</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <p className="text-muted text-sm">No existen registros analíticos cargados para la firma en este periodo.</p>
                    )}
                </section>
            </div>
        </div>
    );
}