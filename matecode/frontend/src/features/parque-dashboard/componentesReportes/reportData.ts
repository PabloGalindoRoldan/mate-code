import type {
    ReportData,
    ReportType,
    ResumenLotes,
    InventarioResumen,
    PresupuestoResumen,
} from "./ReportRegistry";

const defaultLotes: ResumenLotes = {
    disponibles: 14,
    ocupados: 32,
    reservados: 6,
    superficieAsignadaM2: 125000,
    superficieTotalM2: 180000,
};

const defaultInventario: InventarioResumen = {
    equipos: 148,
    vehiculos: 12,
    herramientas: 86,
    valorEstimado: 18500000,
};

const defaultPresupuesto: PresupuestoResumen = {
    presupuestoAnual: 250000000,
    ejecutado: 142000000,
    disponible: 108000000,
};

export function getMockReportData(reportType: ReportType): ReportData {
    switch (reportType) {
        case "general":
            return {
                datosLotes: defaultLotes,
                empresasActivas: 29,
                empresasPendientes: 3,
                inventarioResumen: defaultInventario,
                presupuestoResumen: defaultPresupuesto,
            };
        case "lotes":
            return {
                datosLotes: defaultLotes,
            };
        case "empresas":
            return {
                empresasActivas: 29,
                empresasPendientes: 3,
            };
        case "inventario":
            return {
                inventarioResumen: defaultInventario,
            };
        case "presupuesto":
            return {
                presupuestoResumen: defaultPresupuesto,
            };
        case "consumos":
            return {
                empresasActivas: 29,
                empresasPendientes: 3,
            };
        case "proyectos":
            return {
                empresasActivas: 29,
                empresasPendientes: 3,
            };
        default:
            return {};
    }
}
