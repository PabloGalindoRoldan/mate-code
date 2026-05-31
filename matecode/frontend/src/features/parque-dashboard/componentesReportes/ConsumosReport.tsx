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
                    <span className="stat-lbl">Empresas Radicadas</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-val">{data.empresasPendientes ?? 0}</span>
                    <span className="stat-lbl">Empresas no Radicadas</span>
                </div>
                <div className="stat-box blue">
                    <span className="stat-val">{data.empleadosUltimoRegistro ?? 0}</span>
                    <span className="stat-lbl">Empleados totales</span>
                </div>
                <div className="stat-box purple">
                    <span className="stat-val">{data.vehiculosUltimoRegistro ?? 0}</span>
                    <span className="stat-lbl">Vehículos totales</span>
                </div>
                <div className="stat-box green">
                    <span className="stat-val">{data.consumosTotales?.luz ? new Intl.NumberFormat('es-AR').format(data.consumosTotales.luz) : 0}</span>
                    <span className="stat-lbl">kWh consumidos</span>
                </div>
                <div className="stat-box aqua">
                    <span className="stat-val">{data.consumosTotales?.agua ? new Intl.NumberFormat('es-AR').format(data.consumosTotales.agua) : 0}</span>
                    <span className="stat-lbl">m³ de agua</span>
                </div>
                <div className="stat-box normal">
                    <span className="stat-val">{data.consumosTotales?.gas ? new Intl.NumberFormat('es-AR').format(data.consumosTotales.gas) : 0}</span>
                    <span className="stat-lbl">m³ de gas</span>
                </div>
            </div>

            <div className="pending-integration-notice info-blue">
                <div className="notice-title">Nota:</div>
                <p>
                    Los valores de empleados y vehículos se calculan a partir del último registro disponible del parque. {data.ultimaFechaRegistro ? `Último mes registrado: ${data.ultimaFechaRegistro}.` : ""}
                </p>
            </div>
        </section>
    );
}