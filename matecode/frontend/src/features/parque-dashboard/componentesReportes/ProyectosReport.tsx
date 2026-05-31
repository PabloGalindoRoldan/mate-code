import type { ReportProps } from "./ReportRegistry";

export function ProyectosReport({ data }: ReportProps) {
    return (
        <section className="preview-section">
            <h3>Solicitudes y Proyectos</h3>
            <p className="section-desc">
                Información de solicitudes, proyectos preliminares y proyectos definitivos.
            </p>

            <div className="stats-grid-preview">
                <div className="stat-box green">
                    <span className="stat-val">{data.empresasActivas ?? 0}</span>
                    <span className="stat-lbl">Empresas asociadas</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-val">{data.empresasPendientes ?? 0}</span>
                    <span className="stat-lbl">Proyectos pendientes</span>
                </div>
            </div>

            <div className="pending-integration-notice">
                <div className="notice-title">Próximos pasos</div>
                <p>
                    Se integrarán los datos reales de proyectos y su ciclo de vida para este reporte.
                </p>
            </div>
        </section>
    )
}