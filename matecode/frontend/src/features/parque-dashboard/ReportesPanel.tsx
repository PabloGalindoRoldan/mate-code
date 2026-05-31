import { useEffect, useState } from "react";
import { toast } from "sonner";
import { Download } from "lucide-react";

import "./ReportesPanel.css";

import { REPORT_CONFIG } from "./componentesReportes/ReportConfig";
import { REPORT_COMPONENTS } from "./componentesReportes/ReportRenderer";
import { type ReportData } from "./componentesReportes/ReportRegistry";
import { type ReportType } from "./ReportType";
import { exportReport } from "./componentesReportes/ReportExport";
import { lotesApi, empresasApi, consumosApi, presupuestoApi, proyectosApi, inventarioApi } from "../../api/axios";

export default function ReportesPanel() {
    const [reportType, setReportType] =
        useState<ReportType>("general");

    const [exportFormat, setExportFormat] =
        useState<"pdf" | "txt">("pdf");

    const [isExporting, setIsExporting] =
        useState(false);

    const [reportData, setReportData] = useState<ReportData>({});
    const [isLoading, setIsLoading] = useState(false);
    const [loadError, setLoadError] = useState<string | null>(null);

    const ReportComponent = REPORT_COMPONENTS[reportType];

    const currentYear = new Date().getFullYear();

    const handleExport = async () => {
        setIsExporting(true);

        try {
            await exportReport(reportType, reportData, exportFormat);
            toast.success(
                `Reporte exportado exitosamente en formato .${exportFormat.toUpperCase()}`
            );
        } catch (error) {
            console.error("Export error", error);
            toast.error("No se pudo exportar el reporte. Intente nuevamente.");
        } finally {
            setIsExporting(false);
        }
    };

    useEffect(() => {
        const loadReportData = async () => {
            setIsLoading(true);
            setLoadError(null);

            try {
                const [empresas, lotes, presupuesto, proyectosResponse, inventarioItems, consumosResult] = await Promise.all([
                    empresasApi.listarEmpresas(),
                    lotesApi.getMapaLotes(),
                    presupuestoApi.getBalance(currentYear),
                    proyectosApi.listarProyectos(),
                    inventarioApi.listarInventario(true),
                    consumosApi.getReporteGlobal(currentYear),
                ]);

                const empresasActivas = empresas.filter((item: any) => item.esRadicada).length;
                const empresasPendientes = empresas.filter((item: any) => !item.esRadicada).length;

                const lotesFeatures = Array.isArray(lotes?.features) ? lotes.features : [];
                const datosLotes = {
                    disponibles: lotesFeatures.filter((feature: any) => feature?.properties?.estado === 'disponible').length,
                    ocupados: lotesFeatures.filter((feature: any) => feature?.properties?.estado === 'ocupado').length,
                    reservados: lotesFeatures.filter((feature: any) => feature?.properties?.estado === 'reservado').length,
                    superficieAsignadaM2: lotesFeatures
                        .filter((feature: any) => ['ocupado', 'reservado'].includes(feature?.properties?.estado))
                        .reduce((total: number, feature: any) => total + Number(feature?.properties?.sup ?? 0), 0),
                    superficieTotalM2: lotesFeatures.reduce(
                        (total: number, feature: any) => total + Number(feature?.properties?.sup ?? 0),
                        0
                    ),
                };

                const inventarioCategoriasData: Record<string, number> = {};
                const categoriaLabels: Record<string, string> = {
                    MAQUINARIA_PESADA: 'Maquinaria Pesada',
                    HERRAMIENTAS_MANUALES: 'Herramientas Manuales',
                    EQUIPO_MEDICION: 'Equipo de Medición',
                    VEHICULOS_UTILITARIOS: 'Vehículos Utilitarios',
                    MOBILIARIO_OFICINA: 'Mobiliario de Oficina',
                    EQUIPO_INFORMATICO: 'Equipo Informático',
                    DISPOSITIVOS_RED: 'Dispositivos de Red',
                    SEGURIDAD_VIGILANCIA: 'Seguridad y Vigilancia',
                    PREVENCION_INCENDIOS: 'Prevención de Incendios',
                    ILUMINACION_ELECTRICIDAD: 'Iluminación y Electricidad',
                    REPUESTOS_INSUMOS: 'Repuestos e Insumos',
                    MATERIAL_CONSTRUCCION: 'Material de Construcción',
                    LIMPIEZA_MANTENIMIENTO: 'Limpieza y Mantenimiento',
                    PAPELERIA_ESCRITORIO: 'Papelería y Escritorio',
                    EQUIPO_PROTECCION_PERSONAL: 'Equipo de Protección Personal',
                    OTROS: 'Otros',
                };

                inventarioItems.forEach((item: any) => {
                    const key = String(item.categoria ?? 'OTROS');
                    inventarioCategoriasData[key] = (inventarioCategoriasData[key] ?? 0) + 1;
                });

                const inventarioCategorias = Object.entries(inventarioCategoriasData)
                    .filter(([, cantidad]) => cantidad > 0)
                    .map(([categoria, cantidad]) => ({
                        categoria,
                        label: categoriaLabels[categoria] ?? categoria,
                        cantidad,
                    }))
                    .sort((a, b) => a.label.localeCompare(b.label, 'es'));

                const inventarioActivosTotal = inventarioItems.length;

                const inventarioResumen = {
                    equipos: inventarioItems.filter((item: any) => !item.categoria.includes('VEHICULOS') && !item.categoria.includes('HERRAMIENTAS')).length,
                    vehiculos: inventarioItems.filter((item: any) => item.categoria.includes('VEHICULOS')).length,
                    herramientas: inventarioItems.filter((item: any) => item.categoria.includes('HERRAMIENTAS')).length,
                    valorEstimado: 0,
                };

                const presupuestoResumen = {
                    presupuestoAnual: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.creditoOriginal ?? 0),
                        0
                    ),
                    creditoVigente: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.creditoVigente ?? 0),
                        0
                    ),
                    comprometido: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.comprometido ?? 0),
                        0
                    ),
                    devengado: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.devengado ?? 0),
                        0
                    ),
                    pagado: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.pagado ?? 0),
                        0
                    ),
                    ejecutado: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.pagado ?? 0),
                        0
                    ),
                    disponible: presupuesto.reduce(
                        (total: number, partida: any) => total + Number(partida.saldoDisponible ?? 0),
                        0
                    ),
                    totalPartidas: presupuesto.length,
                };

                const proyectos = [
                    ...(proyectosResponse.preliminares ?? []),
                    ...(proyectosResponse.definitivos ?? []),
                ];

                const proyectosEnRevision = proyectos.filter(
                    (item: any) => String(item.estado).toLowerCase() === 'en_revision'
                ).length;
                const proyectosAprobados = proyectos.filter(
                    (item: any) => String(item.estado).toLowerCase() === 'aprobado'
                ).length;
                const proyectosRectificar = proyectos.filter(
                    (item: any) => String(item.estado).toLowerCase() === 'rectificar'
                ).length;
                const proyectosRechazados = proyectos.filter(
                    (item: any) => String(item.estado).toLowerCase() === 'rechazado'
                ).length;

                const consumosArray = Array.isArray(consumosResult)
                    ? consumosResult
                    : consumosResult
                        ? [consumosResult]
                        : [];

                const consumosTotales = {
                    luz: consumosArray.reduce(
                        (total: number, registro: any) => total + Number(registro.luz ?? 0),
                        0
                    ),
                    gas: consumosArray.reduce(
                        (total: number, registro: any) => total + Number(registro.gas ?? 0),
                        0
                    ),
                    agua: consumosArray.reduce(
                        (total: number, registro: any) => total + Number(registro.agua ?? 0),
                        0
                    ),
                };

                let empleadosUltimoRegistro = 0;
                let vehiculosUltimoRegistro = 0;
                let ultimaFechaRegistro = '';

                if (consumosArray.length > 0) {
                    const latestYear = Math.max(...consumosArray.map((registro: any) => Number(registro.ano ?? 0)));
                    const latestMonth = Math.max(...consumosArray
                        .filter((registro: any) => Number(registro.ano ?? 0) === latestYear)
                        .map((registro: any) => Number(registro.mes ?? 0)));

                    const latestRecords = consumosArray.filter(
                        (registro: any) =>
                            Number(registro.ano ?? 0) === latestYear &&
                            Number(registro.mes ?? 0) === latestMonth
                    );

                    empleadosUltimoRegistro = latestRecords.reduce(
                        (total: number, registro: any) => total + Number(registro.empleados ?? 0),
                        0
                    );
                    vehiculosUltimoRegistro = latestRecords.reduce(
                        (total: number, registro: any) => total + Number(registro.vehiculos ?? 0),
                        0
                    );

                    const monthNames = [
                        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
                        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
                    ];
                    if (latestMonth >= 1 && latestMonth <= 12) {
                        ultimaFechaRegistro = `${monthNames[latestMonth - 1]} ${latestYear}`;
                    }
                }

                setReportData({
                    datosLotes,
                    empresasActivas,
                    empresasPendientes,
                    inventarioResumen,
                    inventarioCategorias,
                    inventarioActivosTotal,
                    inventarioItems,
                    presupuestoResumen,
                    presupuestoPartidas: presupuesto,
                    proyectosEnRevision,
                    proyectosAprobados,
                    proyectosRectificar,
                    proyectosRechazados,
                    consumosTotales,
                    empleadosUltimoRegistro,
                    vehiculosUltimoRegistro,
                    ultimaFechaRegistro,
                });
            } catch (err: any) {
                console.error('Error cargando datos de reportes:', err);
                setLoadError('No se pudieron cargar los datos del reporte. Intente nuevamente.');
            } finally {
                setIsLoading(false);
            }
        };

        loadReportData();
    }, [currentYear]);

    return (
        <div className="reportes-panel">
            <header className="reportes-header">
                <div>
                    <h2>
                        Centro de Reportes y Estadísticas
                    </h2>

                    <p>
                        Genere, visualice y exporte
                        informes operativos del Parque
                        Industrial.
                    </p>
                </div>
            </header>

            <div className="reportes-tabs">
                {Object.entries(REPORT_CONFIG).map(
                    ([key, config]) => {
                        const Icon = config.icon;

                        return (
                            <button
                                key={key}
                                className={`tab-btn ${reportType === key
                                    ? "active"
                                    : ""
                                    }`}
                                onClick={() =>
                                    setReportType(
                                        key as ReportType
                                    )
                                }
                            >
                                <Icon size={18} />
                                {config.title}
                            </button>
                        );
                    }
                )}
            </div>

            <div className="reportes-content-layout">
                <div className="controls-card">
                    <h3>
                        Configuración de Exportación
                    </h3>

                    <div className="control-group">
                        <label>
                            Formato de descarga
                        </label>

                        <div className="radio-group">
                            <label className="radio-label">
                                <input
                                    type="radio"
                                    checked={
                                        exportFormat ===
                                        "pdf"
                                    }
                                    onChange={() =>
                                        setExportFormat(
                                            "pdf"
                                        )
                                    }
                                />
                                Documento PDF (.pdf)
                            </label>

                            <label className="radio-label">
                                <input
                                    type="radio"
                                    checked={
                                        exportFormat ===
                                        "txt"
                                    }
                                    onChange={() =>
                                        setExportFormat(
                                            "txt"
                                        )
                                    }
                                />
                                Texto Plano (.txt)
                            </label>
                        </div>
                    </div>

                    <button
                        className="export-btn"
                        onClick={handleExport}
                        disabled={isExporting || isLoading || !!loadError}
                    >
                        <Download size={18} />

                        {isExporting
                            ? "Generando Archivo..."
                            : `Exportar Reporte .${exportFormat.toUpperCase()}`}
                    </button>
                </div>

                <div className="preview-card">
                    {isLoading ? (
                        <div className="report-loading">
                            Cargando datos del reporte...
                        </div>
                    ) : loadError ? (
                        <div className="error-message">
                            {loadError}
                        </div>
                    ) : (
                        <ReportComponent
                            data={reportData}
                            reportType={reportType}
                        />
                    )}
                </div>
            </div>
        </div>
    );
}