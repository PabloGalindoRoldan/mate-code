import type { ReportProps } from "./ReportRegistry";

export function ConsumosReport({ data }: ReportProps) {
    return (
        <section className="preview-section">
            <h3>Consumos del Parque</h3>
            <p className="section-desc">
                Este panel mostrará los datos de consumo energético y de servicios del parque industrial.
            </p>

            <div className="stats-grid-preview">
                <div className="stat-box normal">
                    <span className="stat-val">{data.empresasActivas ?? 0}</span>
                    <span className="stat-lbl">Empresas activas</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-val">{data.empresasPendientes ?? 0}</span>
                    <span className="stat-lbl">Empresas pendientes</span>
                </div>
            </div>

            <div className="pending-integration-notice info-blue">
                <div className="notice-title">Planes de integración</div>
                <p>
                    La sección de consumos se diseñará para mostrar métricas de consumo y comparativas por año.
                </p>
            </div>
        </section>
    );
}