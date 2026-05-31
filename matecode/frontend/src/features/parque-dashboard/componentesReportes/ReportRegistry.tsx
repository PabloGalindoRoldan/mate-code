import type { ComponentType } from "react";
import {
    LayoutGrid,
    BarChart3,
    Building2,
    Boxes,
    Wallet,
    FileText,
    MapPin,
} from "lucide-react";

import { GeneralReport } from "./GeneralReport";
import { LotesReport } from "./LotesReport";
import { ConsumosReport } from "./ConsumosReport";
import { EmpresasReport } from "./EmpresasReport";
import { InventarioReport } from "./InventarioReport";
import { PresupuestoReport } from "./PresupuestoReport";
import { ProyectosReport } from "./ProyectosReport";

export type ReportType =
    | "general"
    | "consumos"
    | "empresas"
    | "inventario"
    | "presupuesto"
    | "proyectos"
    | "lotes";

export interface ResumenLotes {
    disponibles: number;
    ocupados: number;
    reservados: number;
    superficieAsignadaM2: number;
    superficieTotalM2: number;
}

export interface InventarioResumen {
    equipos: number;
    vehiculos: number;
    herramientas: number;
    valorEstimado: number;
}

export interface InventarioCategoriaResumen {
    categoria: string;
    label: string;
    cantidad: number;
}

export interface InventarioItem {
    id: number;
    nombre: string;
    categoria: string;
    detalle: string;
    activo: boolean;
}

export interface PresupuestoPartida {
    presupuestoId: number;
    codigo: string;
    nombre: string;
    nivel: 'PRINCIPAL' | 'PARCIAL' | 'SUBPARCIAL';
    fuenteFinanciamiento: string;
    creditoOriginal: number;
    creditoVigente: number;
    comprometido: number;
    devengado: number;
    pagado: number;
    saldoDisponible: number;
}

export interface PresupuestoResumen {
    presupuestoAnual: number;
    creditoVigente?: number;
    comprometido?: number;
    devengado?: number;
    pagado?: number;
    ejecutado: number;
    disponible: number;
    totalPartidas?: number;
}

export interface ReportData {
    datosLotes?: ResumenLotes;
    empresasActivas?: number;
    empresasPendientes?: number;
    inventarioResumen?: InventarioResumen;
    inventarioCategorias?: InventarioCategoriaResumen[];
    inventarioActivosTotal?: number;
    inventarioItems?: InventarioItem[];
    presupuestoResumen?: PresupuestoResumen;
    presupuestoPartidas?: PresupuestoPartida[];
    proyectosEnRevision?: number;
    proyectosAprobados?: number;
    proyectosRectificar?: number;
    proyectosRechazados?: number;
    consumosTotales?: {
        luz: number;
        gas: number;
        agua: number;
    };
    empleadosUltimoRegistro?: number;
    vehiculosUltimoRegistro?: number;
    ultimaFechaRegistro?: string;
}

export interface ReportProps {
    data: ReportData;
    reportType: ReportType;
}

export interface ReportMetadata {
    title: string;
    description: string;
    icon: ComponentType<{ size?: number }>;
}

export interface ReportDefinition {
    meta: ReportMetadata;
    Component: ComponentType<ReportProps>;
}

export const REPORT_DEFINITIONS: Record<ReportType, ReportDefinition> = {
    general: {
        meta: {
            title: "Ocupación y Generales",
            description:
                "Resumen de ocupación, lotes activos y estado general del parque industrial.",
            icon: LayoutGrid,
        },
        Component: GeneralReport,
    },
    consumos: {
        meta: {
            title: "Consumos del Parque",
            description:
                "Consumos energéticos y de servicios del parque industrial.",
            icon: BarChart3,
        },
        Component: ConsumosReport,
    },
    empresas: {
        meta: {
            title: "Empresas",
            description:
                "Estado de radicación y actividad de las empresas instaladas.",
            icon: Building2,
        },
        Component: EmpresasReport,
    },
    inventario: {
        meta: {
            title: "Inventario",
            description:
                "Detalle de activos, equipos y recursos disponibles en el parque.",
            icon: Boxes,
        },
        Component: InventarioReport,
    },
    presupuesto: {
        meta: {
            title: "Presupuesto",
            description:
                "Ejecución financiera y saldo disponible del presupuesto anual.",
            icon: Wallet,
        },
        Component: PresupuestoReport,
    },
    proyectos: {
        meta: {
            title: "Solicitudes y Proyectos",
            description:
                "Avance y estado de solicitudes, proyectos preliminares y definitivos.",
            icon: FileText,
        },
        Component: ProyectosReport,
    },
    lotes: {
        meta: {
            title: "Lotes",
            description:
                "Disponibilidad y ocupación de los lotes del parque industrial.",
            icon: MapPin,
        },
        Component: LotesReport,
    },
};

export const REPORT_CONFIG: Record<ReportType, ReportMetadata> = Object.fromEntries(
    Object.entries(REPORT_DEFINITIONS).map(([key, reportDefinition]) => [
        key,
        reportDefinition.meta,
    ])
) as Record<ReportType, ReportMetadata>;

export const REPORT_COMPONENTS: Record<ReportType, ComponentType<ReportProps>> = Object.fromEntries(
    Object.entries(REPORT_DEFINITIONS).map(([key, reportDefinition]) => [
        key,
        reportDefinition.Component,
    ])
) as Record<ReportType, ComponentType<ReportProps>>;
