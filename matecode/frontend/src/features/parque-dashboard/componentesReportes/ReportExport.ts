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
        rows.push(["Empresas Radicadas", formatNumber(data.empresasActivas)]);
    }

    if (data.empresasPendientes !== undefined) {
        rows.push(["Empresas No Radicadas", formatNumber(data.empresasPendientes)]);
    }

    if (data.proyectosEnRevision !== undefined) {
        rows.push(["Proyectos en revisión", formatNumber(data.proyectosEnRevision)]);
    }

    if (data.proyectosAprobados !== undefined) {
        rows.push(["Proyectos aprobados", formatNumber(data.proyectosAprobados)]);
    }

    if (data.proyectosRectificar !== undefined) {
        rows.push(["Proyectos para rectificar", formatNumber(data.proyectosRectificar)]);
    }

    if (data.proyectosRechazados !== undefined) {
        rows.push(["Proyectos rechazados", formatNumber(data.proyectosRechazados)]);
    }

    if (data.consumosTotales) {
        rows.push(["Consumo total de luz", formatNumber(data.consumosTotales.luz)]);
        rows.push(["Consumo total de gas", formatNumber(data.consumosTotales.gas)]);
        rows.push(["Consumo total de agua", formatNumber(data.consumosTotales.agua)]);
    }

    if (data.empleadosUltimoRegistro !== undefined) {
        rows.push(["Empleados totales (último registro)", formatNumber(data.empleadosUltimoRegistro)]);
    }

    if (data.vehiculosUltimoRegistro !== undefined) {
        rows.push(["Vehículos totales (último registro)", formatNumber(data.vehiculosUltimoRegistro)]);
    }

    if (data.ultimaFechaRegistro) {
        rows.push(["Última fecha de registro", data.ultimaFechaRegistro]);
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

    if (data.inventarioActivosTotal !== undefined) {
        rows.push(["Total de elementos activos", formatNumber(data.inventarioActivosTotal)]);
    }

    if (data.inventarioCategorias && data.inventarioCategorias.length > 0) {
        rows.push(["Categorias activas", ""]);
        data.inventarioCategorias.forEach((categoria) => {
            rows.push([`${categoria.label}`, formatNumber(categoria.cantidad)]);
        });
    }

    if (data.presupuestoResumen) {
        rows.push([
            "Presupuesto anual",
            formatCurrency(data.presupuestoResumen.presupuestoAnual),
        ]);
        if (data.presupuestoResumen.creditoVigente !== undefined) {
            rows.push([
                "Crédito vigente",
                formatCurrency(data.presupuestoResumen.creditoVigente),
            ]);
        }
        if (data.presupuestoResumen.comprometido !== undefined) {
            rows.push([
                "Comprometido",
                formatCurrency(data.presupuestoResumen.comprometido),
            ]);
        }
        if (data.presupuestoResumen.devengado !== undefined) {
            rows.push([
                "Devengado",
                formatCurrency(data.presupuestoResumen.devengado),
            ]);
        }
        if (data.presupuestoResumen.pagado !== undefined) {
            rows.push([
                "Pagado",
                formatCurrency(data.presupuestoResumen.pagado),
            ]);
        }
        rows.push([
            "Disponible",
            formatCurrency(data.presupuestoResumen.disponible),
        ]);
        if (data.presupuestoResumen.totalPartidas !== undefined) {
            rows.push([
                "Total de partidas presupuestarias",
                formatNumber(data.presupuestoResumen.totalPartidas),
            ]);
        }
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

function addSectionHeader(doc: jsPDF, title: string, y: number, color: [number, number, number] = [140, 198, 63]): number {
    doc.setFillColor(color[0], color[1], color[2]);
    doc.rect(40, y, 515, 22, "F");
    doc.setFontSize(12);
    doc.setTextColor(245, 246, 244);
    doc.setFont("Asap", "bold");
    doc.text(title, 50, y + 15);
    doc.setTextColor(48, 51, 45);
    doc.setFont("Asap", "normal");
    return y + 30;
}

function addSectionTable(
    doc: jsPDF,
    y: number,
    data: Array<[string, string]>
): number {
    if (data.length === 0) return y;

    autoTable(doc, {
        head: [["Campo", "Valor"]],
        body: data,
        startY: y,
        theme: "grid",
        headStyles: {
            fillColor: [140, 198, 63],
            textColor: [48, 51, 45],
            fontStyle: "bold",
            fontSize: 10,
        },
        bodyStyles: {
            fontSize: 9,
            textColor: [48, 51, 45],
        },
        styles: {
            cellPadding: 5,
            font: "Asap",
        },
        columnStyles: {
            0: { cellWidth: 300 },
            1: { cellWidth: 215 },
        },
        margin: { left: 40, right: 40 },
    });

    return (doc as any).lastAutoTable.finalY + 10;
}

export async function exportReport(
    reportType: ReportType,
    data: ReportData,
    format: ExportFormat
) {
    const title = REPORT_CONFIG[reportType]?.title ?? "Reporte";
    const normalizedFileName = `${title.replace(/\s+/g, "_")}_${reportType}`;

    if (format === "txt") {
        const rows = flattenReportData(data);
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
    let currentY = 30;
    // Colores de la paleta de variables.css
    const colors: Record<string, [number, number, number]> = {
        lotes: [140, 198, 63],          // verde1 #8CC63F
        empresas: [117, 148, 77],       // verde2 #75944D
        inventario: [85, 97, 70],       // verde3 #556146
        presupuesto: [140, 199, 64],    // verde4 #8CC740
        proyectos: [48, 51, 45],        // gris1 #30332D
        consumos: [96, 102, 90],        // texto-secundario #60665a
    };

    // Encabezado
    doc.setFontSize(18);
    doc.setFont("Asap", "bold");
    doc.setTextColor(48, 51, 45);
    doc.text(title, 40, currentY);
    currentY += 25;

    doc.setFontSize(10);
    doc.setFont("Asap", "normal");
    doc.setTextColor(96, 102, 90);
    doc.text(`Generado: ${new Date().toLocaleDateString('es-AR')} a las ${new Date().toLocaleTimeString('es-AR')}`, 40, currentY);
    currentY += 20;

    // Sección Lotes
    if (data.datosLotes) {
        currentY = addSectionHeader(doc, "ESTADO DE LOTES", currentY, colors.lotes);
        const lotesData: Array<[string, string]> = [
            ["Lotes disponibles", formatNumber(data.datosLotes.disponibles)],
            ["Lotes ocupados", formatNumber(data.datosLotes.ocupados)],
            ["Lotes reservados", formatNumber(data.datosLotes.reservados)],
            ["Superficie asignada (m²)", formatNumber(data.datosLotes.superficieAsignadaM2)],
            ["Superficie total (m²)", formatNumber(data.datosLotes.superficieTotalM2)],
        ];
        currentY = addSectionTable(doc, currentY, lotesData);
    }

    // Sección Empresas
    if (data.empresasActivas !== undefined || data.empresasPendientes !== undefined) {
        currentY = addSectionHeader(doc, "EMPRESAS ACTIVAS", currentY, colors.empresas);
        const empresasData: Array<[string, string]> = [];
        if (data.empresasActivas !== undefined) {
            empresasData.push(["Empresas activas", formatNumber(data.empresasActivas)]);
        }
        if (data.empresasPendientes !== undefined) {
            empresasData.push(["Empresas pendientes", formatNumber(data.empresasPendientes)]);
        }
        currentY = addSectionTable(doc, currentY, empresasData);
    }

    // Sección Inventario
    if (data.inventarioResumen || data.inventarioCategorias) {
        currentY = addSectionHeader(doc, "INVENTARIO DEL PARQUE", currentY, colors.inventario);
        const inventarioData: Array<[string, string]> = [];
        if (data.inventarioActivosTotal !== undefined) {
            inventarioData.push(["Total de elementos activos", formatNumber(data.inventarioActivosTotal)]);
        }
        currentY = addSectionTable(doc, currentY, inventarioData);

        // Tabla de categorías
        if (data.inventarioCategorias && data.inventarioCategorias.length > 0) {
            doc.setFontSize(11);
            doc.setFont("Asap", "bold");
            doc.setTextColor(48, 51, 45);
            doc.text("Detalle por categorías:", 40, currentY);
            currentY += 12;

            const categoriasBody = data.inventarioCategorias.map((cat) => [
                cat.label,
                formatNumber(cat.cantidad),
            ]);

            autoTable(doc, {
                head: [["Categoría", "Cantidad"]],
                body: categoriasBody,
                startY: currentY,
                theme: "grid",
                headStyles: {
                    fillColor: [85, 97, 70],
                    textColor: [245, 246, 244],
                    fontSize: 9,
                },
                bodyStyles: {
                    fontSize: 8,
                    textColor: [48, 51, 45],
                },
                styles: {
                    cellPadding: 4,
                    font: "Asap",
                },
                columnStyles: {
                    0: { cellWidth: 350 },
                    1: { cellWidth: 165 },
                },
                margin: { left: 40, right: 40 },
            });

            currentY = (doc as any).lastAutoTable.finalY + 10;
        }
    }

    // Sección Presupuesto
    if (data.presupuestoResumen) {
        currentY = addSectionHeader(doc, "PRESUPUESTO Y EJECUCIÓN", currentY, colors.presupuesto);
        const presupuestoData: Array<[string, string]> = [
            ["Presupuesto anual", formatCurrency(data.presupuestoResumen.presupuestoAnual)],
        ];
        if (data.presupuestoResumen.creditoVigente !== undefined) {
            presupuestoData.push(["Crédito vigente", formatCurrency(data.presupuestoResumen.creditoVigente)]);
        }
        if (data.presupuestoResumen.comprometido !== undefined) {
            presupuestoData.push(["Comprometido", formatCurrency(data.presupuestoResumen.comprometido)]);
            const porcentajeComprometido = Math.round(
                (data.presupuestoResumen.comprometido / (data.presupuestoResumen.creditoVigente || 1)) * 100
            );
            presupuestoData.push(["% Comprometido", `${porcentajeComprometido}%`]);
        }
        if (data.presupuestoResumen.devengado !== undefined) {
            presupuestoData.push(["Devengado", formatCurrency(data.presupuestoResumen.devengado)]);
        }
        if (data.presupuestoResumen.pagado !== undefined) {
            presupuestoData.push(["Pagado", formatCurrency(data.presupuestoResumen.pagado)]);
            const porcentajePagado = Math.round(
                (data.presupuestoResumen.pagado / data.presupuestoResumen.presupuestoAnual) * 100
            );
            presupuestoData.push(["% Ejecución pagada", `${porcentajePagado}%`]);
        }
        presupuestoData.push(["Disponible", formatCurrency(data.presupuestoResumen.disponible)]);
        if (data.presupuestoResumen.totalPartidas !== undefined) {
            presupuestoData.push(["Total de partidas", formatNumber(data.presupuestoResumen.totalPartidas)]);
        }
        currentY = addSectionTable(doc, currentY, presupuestoData);

        // Tabla de partidas presupuestarias
        if (data.presupuestoPartidas && data.presupuestoPartidas.length > 0) {
            doc.setFontSize(11);
            doc.setFont("Asap", "bold");
            doc.setTextColor(48, 51, 45);
            doc.text("Detalle de partidas presupuestarias:", 40, currentY);
            currentY += 12;

            const partidasBody = data.presupuestoPartidas.map((partida) => [
                partida.codigo,
                partida.nombre,
                partida.fuenteFinanciamiento,
                formatCurrency(partida.creditoVigente),
                formatCurrency(partida.comprometido),
                formatCurrency(partida.pagado),
                formatCurrency(partida.saldoDisponible),
            ]);

            autoTable(doc, {
                head: [["Código", "Partida", "Fuente", "Crédito vigente", "Comprometido", "Pagado", "Saldo"]],
                body: partidasBody,
                startY: currentY,
                theme: "grid",
                headStyles: {
                    fillColor: [140, 198, 63],
                    textColor: [48, 51, 45],
                    fontSize: 8,
                },
                bodyStyles: {
                    fontSize: 7,
                    textColor: [48, 51, 45],
                },
                styles: {
                    cellPadding: 3,
                    halign: "right",
                    font: "Asap",
                },
                columnStyles: {
                    0: { cellWidth: 50, halign: "left" },
                    1: { cellWidth: 100, halign: "left" },
                    2: { cellWidth: 70, halign: "left" },
                    3: { cellWidth: 85 },
                    4: { cellWidth: 85 },
                    5: { cellWidth: 75 },
                    6: { cellWidth: 75 },
                },
                margin: { left: 40, right: 40 },
            });

            currentY = (doc as any).lastAutoTable.finalY + 10;
        }
    }

    // Sección Proyectos
    if (
        data.proyectosEnRevision !== undefined ||
        data.proyectosAprobados !== undefined ||
        data.proyectosRectificar !== undefined ||
        data.proyectosRechazados !== undefined
    ) {
        currentY = addSectionHeader(doc, "PROYECTOS Y SOLICITUDES", currentY, colors.proyectos);
        const proyectosData: Array<[string, string]> = [];
        if (data.proyectosAprobados !== undefined) {
            proyectosData.push(["Proyectos aprobados", formatNumber(data.proyectosAprobados)]);
        }
        if (data.proyectosEnRevision !== undefined) {
            proyectosData.push(["Proyectos en revisión", formatNumber(data.proyectosEnRevision)]);
        }
        if (data.proyectosRectificar !== undefined) {
            proyectosData.push(["Proyectos para rectificar", formatNumber(data.proyectosRectificar)]);
        }
        if (data.proyectosRechazados !== undefined) {
            proyectosData.push(["Proyectos rechazados", formatNumber(data.proyectosRechazados)]);
        }
        currentY = addSectionTable(doc, currentY, proyectosData);
    }

    // Sección Consumos
    if (
        data.consumosTotales ||
        data.empleadosUltimoRegistro !== undefined ||
        data.vehiculosUltimoRegistro !== undefined
    ) {
        currentY = addSectionHeader(doc, "CONSUMOS Y RECURSOS", currentY, colors.consumos);
        const consumosData: Array<[string, string]> = [];
        if (data.consumosTotales) {
            consumosData.push(["Consumo total de luz (kWh)", formatNumber(data.consumosTotales.luz)]);
            consumosData.push(["Consumo total de gas (m³)", formatNumber(data.consumosTotales.gas)]);
            consumosData.push(["Consumo total de agua (m³)", formatNumber(data.consumosTotales.agua)]);
        }
        if (data.empleadosUltimoRegistro !== undefined) {
            consumosData.push(["Empleados totales (último registro)", formatNumber(data.empleadosUltimoRegistro)]);
        }
        if (data.vehiculosUltimoRegistro !== undefined) {
            consumosData.push(["Vehículos totales (último registro)", formatNumber(data.vehiculosUltimoRegistro)]);
        }
        if (data.ultimaFechaRegistro) {
            consumosData.push(["Última fecha de registro", data.ultimaFechaRegistro]);
        }
        currentY = addSectionTable(doc, currentY, consumosData);
    }

    // Footer
    if (currentY > doc.internal.pageSize.getHeight() - 40) {
        doc.addPage();
    }
    doc.setFontSize(8);
    doc.setFont("Asap", "normal");
    doc.setTextColor(227, 229, 224);
    doc.text(
        `Reporte generado automáticamente por el sistema de parque industrial. Creditos: Mate&Code 2026`,
        40,
        doc.internal.pageSize.getHeight() - 15
    );

    doc.save(`${normalizedFileName}.pdf`);
}
