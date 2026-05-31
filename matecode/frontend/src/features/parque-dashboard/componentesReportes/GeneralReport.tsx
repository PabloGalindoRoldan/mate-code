import type { ReportProps } from "./ReportRegistry";

export function GeneralReport({ data }: ReportProps) {
    return (
        <section className="preview-section">
            <h3>Estado General del Parque</h3>
            <p className="section-desc">
                Resumen consolidado de ocupación, empresas, inventario y presupuesto.
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
                <div className="stat-box blue">
                    <span className="stat-val">{data.empleadosUltimoRegistro ?? 0}</span>
                    <span className="stat-lbl">Empleados totales</span>
                </div>
                <div className="stat-box purple">
                    <span className="stat-val">{data.vehiculosUltimoRegistro ?? 0}</span>
                    <span className="stat-lbl">Vehículos totales</span>
                </div>
            </div>

            <div className="info-note">
                <strong>Nota:</strong> los valores de empleados y vehículos se toman del último registro disponible del parque. {data.ultimaFechaRegistro ? `Último mes registrado: ${data.ultimaFechaRegistro}.` : ""}
            </div>

            <div className="budget-summary">
                <div className="budget-row">
                    <span>Superficie asignada (m²)</span>
                    <strong>{data.datosLotes ? new Intl.NumberFormat('es-AR').format(data.datosLotes.superficieAsignadaM2) : '-'}</strong>
                </div>
                <div className="budget-row">
                    <span>Presupuesto disponible</span>
                    <strong>{data.presupuestoResumen ? new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 0 }).format(data.presupuestoResumen.disponible) : '-'}</strong>
                </div>
            </div>

            <div className="pending-integration-notice info-blue">
                <div className="notice-title">Resumen operativo</div>
                <p>
                    Este reporte consolida los datos principales del parque industrial y sirve como panel de control rápido para tomar decisiones.
                </p>
            </div>
        </section>
    );
}
