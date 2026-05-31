import type { ReportProps } from "./ReportRegistry";

export function LotesReport({ data }: ReportProps) {
    return (
        <section className="preview-section">
            <h3>Estado de Lotes</h3>
            <p className="section-desc">
                Muestra un resumen de disponibilidad y ocupación de las parcelas.
            </p>

            <div className="stats-grid-preview">
                <div className="stat-box green">
                    <span className="stat-val">{data.datosLotes?.disponibles ?? 0}</span>
                    <span className="stat-lbl">Disponibles</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-val">{data.datosLotes?.ocupados ?? 0}</span>
                    <span className="stat-lbl">Ocupados</span>
                </div>
                <div className="stat-box normal">
                    <span className="stat-val">{data.datosLotes?.reservados ?? 0}</span>
                    <span className="stat-lbl">Reservados</span>
                </div>
            </div>

            <div className="budget-summary">
                <div className="budget-row">
                    <span>Superficie asignada (m²)</span>
                    <strong>{data.datosLotes ? new Intl.NumberFormat("es-AR").format(data.datosLotes.superficieAsignadaM2) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Superficie total (m²)</span>
                    <strong>{data.datosLotes ? new Intl.NumberFormat("es-AR").format(data.datosLotes.superficieTotalM2) : "-"}</strong>
                </div>
            </div>
        </section>
    )
}