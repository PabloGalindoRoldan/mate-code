import type { ReportProps, PresupuestoPartida } from "./ReportRegistry";

export function PresupuestoReport({ data }: ReportProps) {
    const presupuesto = data.presupuestoResumen;
    const partidas = data.presupuestoPartidas ?? [];
    const partidasPrincipales = partidas.filter((item) => item.nivel === "PRINCIPAL").length;
    const partidasParciales = partidas.filter((item) => item.nivel === "PARCIAL").length;
    const partidasSubparciales = partidas.filter((item) => item.nivel === "SUBPARCIAL").length;

    const formatCurrency = (value: number) =>
        new Intl.NumberFormat("es-AR", {
            style: "currency",
            currency: "ARS",
            maximumFractionDigits: 0,
        }).format(value);

    const formatNumber = (value: number) =>
        new Intl.NumberFormat("es-AR").format(value);

    const porcentaje = (valor: number | undefined, base: number | undefined) => {
        if (!valor || !base || base <= 0) return 0;
        return Math.min(100, Math.round((valor / base) * 100));
    };

    const ejecucionPercent = porcentaje(presupuesto?.ejecutado, presupuesto?.presupuestoAnual);
    const compromisoPercent = porcentaje(presupuesto?.comprometido, presupuesto?.creditoVigente);

    return (
        <section className="preview-section">
            <h3>Presupuesto anual y ejecución por partidas</h3>
            <p className="section-desc">
                Vista realista de la ejecución financiera, compromisos y saldos disponibles para el ejercicio actual.
            </p>

            <div className="stats-grid-preview">
                <div className="stat-box normal">
                    <span className="stat-lbl">Partidas totales</span>
                    <span className="stat-val">{formatNumber(presupuesto?.totalPartidas ?? partidas.length)}</span>
                </div>
                <div className="stat-box blue">
                    <span className="stat-lbl">Partidas principales</span>
                    <span className="stat-val">{formatNumber(partidasPrincipales)}</span>
                </div>
                <div className="stat-box orange">
                    <span className="stat-lbl">Partidas parciales</span>
                    <span className="stat-val">{formatNumber(partidasParciales)}</span>
                </div>
                <div className="stat-box red">
                    <span className="stat-lbl">Partidas subparciales</span>
                    <span className="stat-val">{formatNumber(partidasSubparciales)}</span>
                </div>
            </div>

            <div className="budget-summary">
                <div className="budget-row">
                    <span>Presupuesto anual</span>
                    <strong>{presupuesto ? formatCurrency(presupuesto.presupuestoAnual) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Crédito vigente</span>
                    <strong>{presupuesto?.creditoVigente !== undefined ? formatCurrency(presupuesto.creditoVigente) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Comprometido</span>
                    <strong>{presupuesto?.comprometido !== undefined ? formatCurrency(presupuesto.comprometido) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Devengado</span>
                    <strong>{presupuesto?.devengado !== undefined ? formatCurrency(presupuesto.devengado) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Pagado</span>
                    <strong>{presupuesto?.pagado !== undefined ? formatCurrency(presupuesto.pagado) : "-"}</strong>
                </div>
                <div className="budget-row">
                    <span>Disponible</span>
                    <strong>{presupuesto ? formatCurrency(presupuesto.disponible) : "-"}</strong>
                </div>
            </div>

            <div className="progress-section">
                <div className="progress-info">
                    <span>Ejecución pagada</span>
                    <strong>{ejecucionPercent}%</strong>
                </div>
                <div className="progress-bar-bg">
                    <div className="progress-bar-fill budget" style={{ width: `${ejecucionPercent}%` }} />
                </div>
                <div className="progress-info" style={{ marginTop: "12px" }}>
                    <span>Compromiso sobre crédito vigente</span>
                    <strong>{compromisoPercent}%</strong>
                </div>
                <div className="progress-bar-bg">
                    <div className="progress-bar-fill orange" style={{ width: `${compromisoPercent}%` }} />
                </div>
            </div>

            {partidas.length > 0 ? (
                <div className="table-responsive inventory-table-section">
                    <h4>Detalle de partidas presupuestarias</h4>
                    <table className="presupuesto-detail-table">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Partida</th>
                                <th>Fuente</th>
                                <th>Crédito original</th>
                                <th>Crédito vigente</th>
                                <th>Comprometido</th>
                                <th>Devengado</th>
                                <th>Pagado</th>
                                <th>Saldo disponible</th>
                            </tr>
                        </thead>
                        <tbody>
                            {partidas.map((item: PresupuestoPartida) => (
                                <tr key={item.presupuestoId}>
                                    <td>{item.codigo}</td>
                                    <td>{item.nombre}</td>
                                    <td>{item.fuenteFinanciamiento}</td>
                                    <td>{formatCurrency(item.creditoOriginal)}</td>
                                    <td>{formatCurrency(item.creditoVigente)}</td>
                                    <td>{formatCurrency(item.comprometido)}</td>
                                    <td>{formatCurrency(item.devengado)}</td>
                                    <td>{formatCurrency(item.pagado)}</td>
                                    <td className={item.saldoDisponible < 0 ? "text-danger" : undefined}>
                                        {formatCurrency(item.saldoDisponible)}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="placeholder-state">
                    <p>No hay datos de partidas presupuestarias disponibles para este ejercicio.</p>
                </div>
            )}

            <div className="pending-integration-notice info-blue">
                <div className="notice-title">Nota de interpretación</div>
                <p>
                    El saldo disponible se calcula a partir del crédito vigente menos los compromisos.
                    Las partidas se ordenan por estructuración jerárquica real del presupuesto.
                </p>
            </div>
        </section>
    );
}
