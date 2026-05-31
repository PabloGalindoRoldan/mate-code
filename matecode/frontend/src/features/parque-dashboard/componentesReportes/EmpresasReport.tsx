import type { ReportProps } from "./ReportRegistry";

export function EmpresasReport({ data }: ReportProps) {
    return (
        <section className="preview-section">
            <h3>Empresas Activas y Estado</h3>
            <p className="section-desc">
                Esta vista presentará el estado de radicación, lotes asignados y empresas registradas.
            </p>

            <div className="stats-grid-preview">
                <div className="stat-box green">
                    <span className="stat-val">{data.empresasActivas ?? 0}</span>
                    <span className="stat-lbl">Empresas activas</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-val">{data.empresasPendientes ?? 0}</span>
                    <span className="stat-lbl">Empresas pendientes</span>
                </div>
            </div>

        </section>
    );
}