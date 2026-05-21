import { useState } from "react";
import { BarChart3, FileText, Download, LayoutGrid, Info, AlertTriangle } from "lucide-react";
import "./ReportesPanel.css";

// Interfaz para simular los datos disponibles del JSON de Lotes
interface ResumenLotes {
    disponibles: number;
    ocupados: number;
    reservados: number;
    superficieAsignadaM2: number;
    superficieTotalM2: number;
}

export default function ReportesPanel() {
    const [reportType, setReportType] = useState<"general" | "consumos" | "proyectos">("general");
    const [exportFormat, setExportFormat] = useState<"pdf" | "txt">("pdf");
    const [isExporting, setIsExporting] = useState(false);

    // 1. Datos que ya podemos calcular/traer (Mocked basados en tus JSONs actuales)
    const [datosLotes] = useState<ResumenLotes>({
        disponibles: 14,
        ocupados: 32,
        reservados: 6,
        superficieAsignadaM2: 125000,
        superficieTotalM2: 180000,
    });

    const empresasRadicadasCount = 32; // Viene de tu API/JSON actual

    // 2. Manejador del trigger de exportación
    const handleExport = () => {
        setIsExporting(true);
        // Aquí se acoplará la llamada a las librerías de guardado en la Iteración correspondiente
        setTimeout(() => {
            alert(`Reporte exportado exitosamente en formato .${exportFormat.toUpperCase()}`);
            setIsExporting(false);
        }, 1200);
    };

    return (
        <div className="reportes-panel">
            <header className="reportes-header">
                <div>
                    <h2>Centro de Reportes y Estadísticas</h2>
                    <p>Genere, visualice y exporte informes operativos del Parque Industrial.</p>
                </div>
            </header>

            {/* Selector de tipo de reporte estilo Tabs internas */}
            <div className="reportes-tabs">
                <button
                    className={`tab-btn ${reportType === "general" ? "active" : ""}`}
                    onClick={() => setReportType("general")}
                >
                    <LayoutGrid size={18} /> Ocupación y Generales
                </button>
                <button
                    className={`tab-btn ${reportType === "consumos" ? "active" : ""}`}
                    onClick={() => setReportType("consumos")}
                >
                    <BarChart3 size={18} /> Consumos del Parque
                </button>
                <button
                    className={`tab-btn ${reportType === "proyectos" ? "active" : ""}`}
                    onClick={() => setReportType("proyectos")}
                >
                    <FileText size={18} /> Solicitudes y Proyectos
                </button>
            </div>

            <div className="reportes-content-layout">
                {/* Panel lateral de controles de exportación */}
                <div className="controls-card">
                    <h3>Configuración de Exportación</h3>
                    <div className="control-group">
                        <label>Formato de descarga</label>
                        <div className="radio-group">
                            <label className="radio-label">
                                <input
                                    type="radio"
                                    name="format"
                                    value="pdf"
                                    checked={exportFormat === "pdf"}
                                    onChange={() => setExportFormat("pdf")}
                                />
                                Documento PDF (.pdf)
                            </label>
                            <label className="radio-label">
                                <input
                                    type="radio"
                                    name="format"
                                    value="txt"
                                    checked={exportFormat === "txt"}
                                    onChange={() => setExportFormat("txt")}
                                />
                                Texto Plano (.txt)
                            </label>
                        </div>
                    </div>

                    <button
                        className="export-btn"
                        onClick={handleExport}
                        disabled={isExporting}
                    >
                        <Download size={18} />
                        {isExporting ? "Generando Archivo..." : `Exportar Reporte .${exportFormat.toUpperCase()}`}
                    </button>
                </div>

                {/* Área de previsualización dinámica según la Tab activa */}
                <div className="preview-card">
                    {reportType === "general" && (
                        <div className="preview-section animate-fade">
                            <h3>Previsualización: Estado de Ocupación General</h3>

                            <div className="stats-grid-preview">
                                <div className="stat-box green">
                                    <span className="stat-val">{datosLotes.disponibles}</span>
                                    <span className="stat-lbl">Lotes Disponibles</span>
                                </div>
                                <div className="stat-box red">
                                    <span className="stat-val">{datosLotes.ocupados}</span>
                                    <span className="stat-lbl">Lotes Ocupados</span>
                                </div>
                                <div className="stat-box orange">
                                    <span className="stat-val">{datosLotes.reservados}</span>
                                    <span className="stat-lbl">Lotes Reservados</span>
                                </div>
                                <div className="stat-box normal">
                                    <span className="stat-val">{empresasRadicadasCount}</span>
                                    <span className="stat-lbl">Empresas Radicadas</span>
                                </div>
                            </div>

                            <div className="progress-section">
                                <div className="progress-info">
                                    <span>Superficie Asignada: <strong>{datosLotes.superficieAsignadaM2.toLocaleString()} m²</strong></span>
                                    <span>Superficie Total: {datosLotes.superficieTotalM2.toLocaleString()} m²</span>
                                </div>
                                <div className="progress-bar-bg">
                                    <div
                                        className="progress-bar-fill"
                                        style={{ width: `${(datosLotes.superficieAsignadaM2 / datosLotes.superficieTotalM2) * 100}%` }}
                                    ></div>
                                </div>
                            </div>

                            {/* Campos modelados no implementados aún */}
                            <div className="pending-integration-notice">
                                <div className="notice-title">
                                    <Info size={16} />
                                    <span>Métricas en espera de módulos del sistema:</span>
                                </div>
                                <ul>
                                    <li><strong>Identificación de lotes por Empresa:</strong> Pendiente de vinculación en vista 'Empresas'.</li>
                                    <li><strong>Puestos de trabajo totales:</strong> Reclama consolidación de declaraciones juradas de empresas.</li>
                                </ul>
                            </div>
                        </div>
                    )}

                    {reportType === "consumos" && (
                        <div className="preview-section animate-fade">
                            <h3>Previsualización: Métrica de Consumos Totales</h3>
                            <p className="section-desc">Información consolidada proveniente de los medidores activos declarados.</p>

                            {/* Datos mockeados que simulan lo que ya maneja tu backend de consumos */}
                            <table className="reporte-mock-table">
                                <thead>
                                    <tr>
                                        <th>Mes/Año</th>
                                        <th>Electricidad Total</th>
                                        <th>Gas Total</th>
                                        <th>Agua Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><strong>04/2026</strong></td>
                                        <td>45,230.50 kWh</td>
                                        <td>12,400.00 m³</td>
                                        <td>8,920.00 m³</td>
                                    </tr>
                                    <tr>
                                        <td><strong>03/2026</strong></td>
                                        <td>48,110.20 kWh</td>
                                        <td>14,150.00 m³</td>
                                        <td>9,100.00 m³</td>
                                    </tr>
                                </tbody>
                            </table>

                            <div className="pending-integration-notice info-blue">
                                <div className="notice-title">
                                    <Info size={16} />
                                    <span>Próxima mejora de diseño:</span>
                                </div>
                                <p>Al integrar gráficos, se incorporará la apertura analítica detallada por Empresa individual.</p>
                            </div>
                        </div>
                    )}

                    {reportType === "proyectos" && (
                        <div className="preview-section animate-fade placeholder-state">
                            <AlertTriangle size={40} className="warning-icon" />
                            <h3>Módulo de Solicitudes y Proyectos</h3>
                            <p>Esta previsualización estará disponible una vez se configuren los flujos de presentación de proyectos preliminares y formales.</p>
                            <div className="mock-wireframe-box">
                                <span>Estructura Futura: Listado Multiestado (Pendiente | Aprobado | Rechazado) con Fechas de Actualización</span>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}