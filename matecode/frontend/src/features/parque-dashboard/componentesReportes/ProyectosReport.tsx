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
                    <span className="stat-val">{data.proyectosAprobados ?? 0}</span>
                    <span className="stat-lbl">Proyectos aprobados</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-val">{data.proyectosEnRevision ?? 0}</span>
                    <span className="stat-lbl">Proyectos en revisión</span>
                </div>
                <div className="stat-box normal">
                    <span className="stat-val">{data.proyectosRectificar ?? 0}</span>
                    <span className="stat-lbl">Rectificaciones</span>
                </div>
                <div className="stat-box normal">
                    <span className="stat-val">{data.proyectosRechazados ?? 0}</span>
                    <span className="stat-lbl">Proyectos rechazados</span>
                </div>
            </div>
        </section>
    )
}