import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

import { REPORT_CONFIG, type ReportData, type ReportType } from "./ReportRegistry";

export type ExportFormat = "pdf" | "txt";

function formatNumber(value: number): string {
    return new Intl.NumberFormat("es-AR").format(value);
}

function formatCurrency(value: number): string {
    return new Intl.NumberFormat("es-AR", {
        style: "currency",
        currency: "ARS",
        maximumFractionDigits: 0,
    }).format(value);
}

function flattenReportData(data: ReportData): Array<[string, string]> {
    const rows: Array<[string, string]> = [];

    if (data.datosLotes) {
        rows.push(["Lotes disponibles", formatNumber(data.datosLotes.disponibles)]);
        rows.push(["Lotes ocupados", formatNumber(data.datosLotes.ocupados)]);
        rows.push(["Lotes reservados", formatNumber(data.datosLotes.reservados)]);
        rows.push([
            "Superficie asignada (m²)",
            formatNumber(data.datosLotes.superficieAsignadaM2),
        ]);
        rows.push([
            "Superficie total (m²)",
            formatNumber(data.datosLotes.superficieTotalM2),
        ]);
    }

    if (data.empresasActivas !== undefined) {
        rows.push(["Empresas activas", formatNumber(data.empresasActivas)]);
    }

    if (data.empresasPendientes !== undefined) {
        rows.push(["Empresas pendientes", formatNumber(data.empresasPendientes)]);
    }

    if (data.inventarioResumen) {
        rows.push(["Equipos", formatNumber(data.inventarioResumen.equipos)]);
        rows.push(["Vehículos", formatNumber(data.inventarioResumen.vehiculos)]);
        rows.push(["Herramientas", formatNumber(data.inventarioResumen.herramientas)]);
        rows.push([
            "Valor estimado del inventario",
            formatCurrency(data.inventarioResumen.valorEstimado),
        ]);
    }

    if (data.presupuestoResumen) {
        rows.push([
            "Presupuesto anual",
            formatCurrency(data.presupuestoResumen.presupuestoAnual),
        ]);
        rows.push([
            "Ejecutado",
            formatCurrency(data.presupuestoResumen.ejecutado),
        ]);
        rows.push([
            "Disponible",
            formatCurrency(data.presupuestoResumen.disponible),
        ]);
    }

    return rows;
}

function triggerDownload(blob: Blob, filename: string) {
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement("a");

    anchor.href = url;
    anchor.download = filename;
    document.body.appendChild(anchor);
    anchor.click();
    document.body.removeChild(anchor);
    URL.revokeObjectURL(url);
}

export async function exportReport(
    reportType: ReportType,
    data: ReportData,
    format: ExportFormat
) {
    const title = REPORT_CONFIG[reportType]?.title ?? "Reporte";
    const rows = flattenReportData(data);
    const normalizedFileName = `${title.replace(/\s+/g, "_")}_${reportType}`;

    if (format === "txt") {
        const lines: string[] = [title, "------------------------"];

        if (rows.length === 0) {
            lines.push("No hay datos disponibles para este reporte.");
        } else {
            rows.forEach(([label, value]) => {
                lines.push(`${label}: ${value}`);
            });
        }

        const blob = new Blob([lines.join("\n")], {
            type: "text/plain;charset=utf-8",
        });
        triggerDownload(blob, `${normalizedFileName}.txt`);
        return;
    }

    const doc = new jsPDF({ unit: "pt", format: "a4" });
    const titleY = 40;

    doc.setFontSize(18);
    doc.text(title, 40, titleY);
    doc.setFontSize(11);
    doc.text(`Generado: ${new Date().toLocaleDateString()}`, 40, titleY + 20);

    if (rows.length === 0) {
        doc.text("No hay datos disponibles para este reporte.", 40, titleY + 60);
    } else {
        autoTable(doc, {
            head: [["Campo", "Valor"]],
            body: rows,
            startY: titleY + 50,
            theme: "grid",
            headStyles: {
                fillColor: [140, 198, 63],
                textColor: 255,
            },
            styles: {
                fontSize: 10,
                cellPadding: 6,
            },
            columnStyles: {
                0: { cellWidth: 220 },
                1: { cellWidth: 260 },
            },
        });
    }

    doc.save(`${normalizedFileName}.pdf`);
}
