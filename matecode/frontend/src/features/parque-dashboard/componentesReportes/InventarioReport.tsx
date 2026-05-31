import type { ReportProps } from "./ReportRegistry";

export function InventarioReport({ data }: ReportProps) {
    const inventario = data.inventarioResumen;

    return (
        <section className="preview-section">
            <h3>Inventario del Parque</h3>
            <p className="section-desc">
                Muestra un resumen inicial de activos y valor estimado del inventario.
            </p>

            <div className="stats-grid-preview">
                <div className="stat-box normal">
                    <span className="stat-val">{inventario?.equipos ?? 0}</span>
                    <span className="stat-lbl">Equipos</span>
                </div>
                <div className="stat-box normal">
                    <span className="stat-val">{inventario?.vehiculos ?? 0}</span>
                    <span className="stat-lbl">Vehículos</span>
                </div>
                <div className="stat-box normal">
                    <span className="stat-val">{inventario?.herramientas ?? 0}</span>
                    <span className="stat-lbl">Herramientas</span>
                </div>
            </div>

            <div className="budget-summary">
                <div className="budget-row">
                    <span>Valor estimado del inventario</span>
                    <strong>{inventario ? new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", maximumFractionDigits: 0 }).format(inventario.valorEstimado) : "-"}</strong>
                </div>
            </div>
        </section>
    )
}