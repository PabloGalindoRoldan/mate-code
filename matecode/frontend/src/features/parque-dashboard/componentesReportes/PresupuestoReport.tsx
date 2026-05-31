import type { ReportProps } from "./ReportRegistry";

export function PresupuestoReport({ data }: ReportProps) {
    const presupuesto = data.presupuestoResumen;

    return (
        <section className="preview-section">
            <h3>Resumen Presupuestario</h3>
            <p className="section-desc">
                Vista previa del estado de ejecución presupuestaria y saldo disponible.
            </p>

            <div className="budget-summary">
                <div className="budget-row">
                    <span>Presupuesto anual</span>
                    <strong>{presupuesto ? new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", maximumFractionDigits: 0 }).format(presupuesto.presupuestoAnual) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Ejecutado</span>
                    <strong>{presupuesto ? new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", maximumFractionDigits: 0 }).format(presupuesto.ejecutado) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Disponible</span>
                    <strong>{presupuesto ? new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", maximumFractionDigits: 0 }).format(presupuesto.disponible) : "-"}</strong>
                </div>
            </div>
        </section>
    )
}