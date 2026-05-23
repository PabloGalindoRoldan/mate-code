// // DraggableModalPanel.tsx
// import React, { useRef, useState, useEffect, useCallback } from "react";
// import { Building2, X, CalendarDays, Zap, Droplet, Flame, Users, Truck } from "lucide-react";

// export interface ConsumoRecord {
//     id?: number;
//     mes: number;
//     ano: number;
//     luz?: any;
//     agua?: any;
//     gas?: any;
//     empleados?: number;
//     vehiculos?: number;
//     [key: string]: any;
// }

// interface DraggableModalPanelProps {
//     isOpen: boolean;
//     onClose: () => void;
//     data: {
//         properties: any;
//         activeEmpresa: any;
//         ultimoConsumo: ConsumoRecord | null;
//         isConsumosLoading: boolean;
//     };
// }

// const NOMBRE_MESES = [
//     "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
//     "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
// ];

// export default function DraggableModalPanel({ isOpen, onClose, data }: DraggableModalPanelProps) {
//     const { properties, activeEmpresa, ultimoConsumo, isConsumosLoading } = data;

//     // Estado local para la posición y arrastre
//     const [position, setPosition] = useState({ x: 60, y: 80 });
//     const [isDragging, setIsDragging] = useState(false);
//     const dragStart = useRef({ x: 0, y: 0 });

//     const handleMouseDown = (e: React.MouseEvent) => {
//         if ((e.target as HTMLElement).closest("button")) return;
//         setIsDragging(true);
//         dragStart.current = {
//             x: e.clientX - position.x,
//             y: e.clientY - position.y
//         };
//     };

//     const handleMouseMove = useCallback((e: MouseEvent) => {
//         if (!isDragging) return;
//         setPosition({
//             x: e.clientX - dragStart.current.x,
//             y: e.clientY - dragStart.current.y
//         });
//     }, [isDragging]);

//     const handleMouseUp = useCallback(() => {
//         setIsDragging(false);
//     }, []);

//     useEffect(() => {
//         if (isDragging) {
//             window.addEventListener("mousemove", handleMouseMove);
//             window.addEventListener("mouseup", handleMouseUp);
//         }
//         return () => {
//             window.removeEventListener("mousemove", handleMouseMove);
//             window.removeEventListener("mouseup", handleMouseUp);
//         };
//     }, [isDragging, handleMouseMove, handleMouseUp]);

//     if (!isOpen) return null;

//     // Extractores seguros adaptados para soportar primitivos (numbers) u objetos de deserialización compleja
//     const getLuzValue = (record: ConsumoRecord) =>
//         record.luz && typeof record.luz === "object" ? record.luz.parsedValue : (record.luz ?? 0);

//     const getAguaValue = (record: ConsumoRecord) =>
//         record.agua && typeof record.agua === "object" ? record.agua.parsedValue : (record.agua ?? 0);

//     const getGasValue = (record: ConsumoRecord) =>
//         record.gas && typeof record.gas === "object" ? record.gas.parsedValue : (record.gas ?? 0);

//     return (
//         <div
//             className="draggable-modal-panel"
//             style={{
//                 position: "absolute",
//                 left: `${position.x}px`,
//                 top: `${position.y}px`,
//                 zIndex: 12,
//                 cursor: isDragging ? "grabbing" : "default"
//             }}
//         >
//             <header
//                 className="modal-panel-header"
//                 onMouseDown={handleMouseDown}
//                 style={{ cursor: "grab" }}
//             >
//                 <div className="modal-header-title">
//                     <Building2 size={16} />
//                     <span>Expediente Completo: Lote {properties?.lote ?? "N/A"}</span>
//                 </div>
//                 <button className="close-modal-btn" onClick={onClose}>
//                     <X size={16} />
//                 </button>
//             </header>

//             <div className="modal-panel-body">
//                 {/* 1. Datos Técnicos */}
//                 <section className="modal-data-section">
//                     <h4>Datos Técnicos del Lote (Payload GeoJSON)</h4>
//                     <div className="modal-dynamic-grid">
//                         {properties && Object.entries(properties).map(([key, value]) => (
//                             <div key={key} className="data-box">
//                                 <span className="data-key">{key}:</span>
//                                 <span className="data-value">{String(value ?? "N/A")}</span>
//                             </div>
//                         ))}
//                     </div>
//                 </section>

//                 {/* 2. Ficha de Empresa */}
//                 <section className="modal-data-section">
//                     <h4>Ficha Completa de la Empresa</h4>
//                     {activeEmpresa ? (
//                         <div className="modal-dynamic-grid">
//                             {Object.entries(activeEmpresa).map(([key, value]) => (
//                                 <div key={key} className="data-box">
//                                     <span className="data-key">{key}:</span>
//                                     <span className="data-value">
//                                         {typeof value === "boolean" ? (value ? "Sí" : "No") : String(value ?? "N/A")}
//                                     </span>
//                                 </div>
//                             ))}
//                         </div>
//                     ) : (
//                         <p className="text-muted text-sm">Este lote no registra una empresa activa actualmente.</p>
//                     )}
//                 </section>

//                 {/* 3. Consumos */}
//                 <section className="modal-data-section">
//                     <h4>Último Balance de Consumos e Indicadores</h4>
//                     {isConsumosLoading ? (
//                         <p className="consumos-loading">Cargando mediciones desde la API...</p>
//                     ) : ultimoConsumo ? (
//                         <div className="modal-consumos-wrapper">
//                             <div className="modal-periodo-badge">
//                                 <CalendarDays size={14} />
//                                 <span>Período: {NOMBRE_MESES[ultimoConsumo.mes - 1] || "N/A"} / {ultimoConsumo.ano}</span>
//                             </div>

//                             <div className="modal-indicators-grid">
//                                 <div className="indicator-card energy">
//                                     <Zap size={16} />
//                                     <div className="info">
//                                         <span className="title">Energía</span>
//                                         <span className="val">{getLuzValue(ultimoConsumo)} kWh</span>
//                                     </div>
//                                 </div>
//                                 <div className="indicator-card water">
//                                     <Droplet size={16} />
//                                     <div className="info">
//                                         <span className="title">Agua Potable</span>
//                                         <span className="val">{getAguaValue(ultimoConsumo)} m³</span>
//                                     </div>
//                                 </div>
//                                 <div className="indicator-card gas">
//                                     <Flame size={16} />
//                                     <div className="info">
//                                         <span className="title">Gas Gasoducto</span>
//                                         <span className="val">{getGasValue(ultimoConsumo)} m³</span>
//                                     </div>
//                                 </div>
//                                 <div className="indicator-card staff">
//                                     <Users size={16} />
//                                     <div className="info">
//                                         <span className="title">Nómina Personal</span>
//                                         <span className="val">{ultimoConsumo.empleados ?? 0} operarios</span>
//                                     </div>
//                                 </div>
//                                 <div className="indicator-card vehicles">
//                                     <Truck size={16} />
//                                     <div className="info">
//                                         <span className="title">Flota Activa</span>
//                                         <span className="val">{ultimoConsumo.vehiculos ?? 0} unidades</span>
//                                     </div>
//                                 </div>
//                             </div>
//                         </div>
//                     ) : (
//                         <p className="text-muted text-sm">No existen registros analíticos cargados para la firma en este periodo.</p>
//                     )}
//                 </section>
//             </div>
//         </div>
//     );
// }