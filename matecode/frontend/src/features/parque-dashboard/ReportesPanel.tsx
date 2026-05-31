import { useState } from "react";
import { toast } from "sonner";
import { Download } from "lucide-react";

import "./ReportesPanel.css";

import { REPORT_CONFIG } from "./componentesReportes/ReportConfig";
import { REPORT_COMPONENTS } from "./componentesReportes/ReportRenderer";
import { type ReportType } from "./ReportType";
import { exportReport } from "./componentesReportes/ReportExport";
import { getMockReportData } from "./componentesReportes/reportData";

export default function ReportesPanel() {
    const [reportType, setReportType] =
        useState<ReportType>("general");

    const [exportFormat, setExportFormat] =
        useState<"pdf" | "txt">("pdf");

    const [isExporting, setIsExporting] =
        useState(false);

    const ReportComponent = REPORT_COMPONENTS[reportType];
    const reportData = getMockReportData(reportType);

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
                        disabled={isExporting}
                    >
                        <Download size={18} />

                        {isExporting
                            ? "Generando Archivo..."
                            : `Exportar Reporte .${exportFormat.toUpperCase()}`}
                    </button>
                </div>

                <div className="preview-card">
                    <ReportComponent
                        data={reportData}
                        reportType={reportType}
                    />
                </div>
            </div>
        </div>
    );
}