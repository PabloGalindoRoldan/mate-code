import type { ReportProps } from "./ReportRegistry";

export function InventarioReport({ data }: ReportProps) {
    const categorias = data.inventarioCategorias ?? [];
    const totalActivos = data.inventarioActivosTotal ?? 0;
    const elementosActivos = data.inventarioItems ?? [];

    return (
        <section className="preview-section">
            <h3>Inventario del Parque</h3>
            <p className="section-desc">
                Muestra la cantidad de elementos activos por categoría, el total de elementos disponibles en inventario y una lista de elementos activos.
            </p>

            <div className="budget-summary">
                <div className="budget-row">
                    <span>Total de elementos activos</span>
                    <strong>{totalActivos}</strong>
                </div>
            </div>

            <div className="stats-grid-preview">
                {categorias.length > 0 ? (
                    categorias.map((categoria) => (
                        <div key={categoria.categoria} className="stat-box normal">
                            <span className="stat-val">{categoria.cantidad}</span>
                            <span className="stat-lbl">{categoria.label}</span>
                        </div>
                    ))
                ) : (
                    <div className="stat-box normal">
                        <span className="stat-val">0</span>
                        <span className="stat-lbl">No hay elementos activos</span>
                    </div>
                )}
            </div>

            <div className="inventory-table-section">
                <h4>Lista de elementos activos</h4>
                <div className="table-responsive">
                    <table className="inventario-report-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Categoría</th>
                                <th>Detalle</th>
                            </tr>
                        </thead>
                        <tbody>
                            {elementosActivos.length > 0 ? (
                                elementosActivos.map((item) => (
                                    <tr key={item.id}>
                                        <td>{item.id}</td>
                                        <td>{item.nombre}</td>
                                        <td>{item.categoria}</td>
                                        <td>{item.detalle}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={4} className="empty-row">
                                        No hay elementos activos para mostrar.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="pending-integration-notice info-blue">
                <div className="notice-title">Nota:</div>
                <p>
                    Solo se muestran elementos activos. Los elementos dados de baja no se incluyen en este reporte.
                </p>
            </div>
        </section>
    )
}