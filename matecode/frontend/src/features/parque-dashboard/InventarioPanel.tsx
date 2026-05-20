import { useState, useEffect } from "react";
import { Trash2, Plus, RefreshCw, AlertTriangle } from "lucide-react";
import API from "../../api/axios"; // Ajustá la ruta según dónde tengas guardado tu archivo de Axios
import LoadingSpinner from "../../ui/loading/LoadingSpinner"; // Importación de tu Spinner
import "./InventarioPanel.css";

interface ElementoResponse {
    id: number;
    nombre: string;
    categoria: string;
    activo: boolean;
    bajaRazonCategoria: string | null;
    bajaObservacion: string | null;
}

export default function InventarioPanel() {
    const [elementos, setElementos] = useState<ElementoResponse[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Filtro y Formulario de Alta
    const [soloActivos, setSoloActivos] = useState(true);
    const [nuevoNombre, setNuevoNombre] = useState("");
    const [nuevaCategoria, setNuevaCategoria] = useState("MAQUINARIA_PESADA");

    // Estado del Modal de Baja
    const [elementoSeleccionado, setElementoSeleccionado] = useState<ElementoResponse | null>(null);
    const [razonBaja, setRazonBaja] = useState("OBSOLESCENCIA");
    const [observacionBaja, setObservacionBaja] = useState("");

    const cargarInventario = async () => {
        setLoading(true);
        setError(null);
        try {
            // Pasamos los query params de forma limpia usando la configuración de Axios
            const response = await API.get<ElementoResponse[]>("/api/inventario", {
                params: { soloActivos }
            });
            setElementos(response.data);
        } catch (err: any) {
            const msg = err.response?.data || "Error al cargar los datos del inventario.";
            setError(typeof msg === "string" ? msg : JSON.stringify(msg));
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        cargarInventario();
    }, [soloActivos]);

    const handleCrearElemento = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!nuevoNombre.trim()) return;

        try {
            await API.post("/api/inventario", {
                nombre: nuevoNombre,
                categoria: nuevaCategoria
            });

            setNuevoNombre("");
            cargarInventario();
        } catch (err: any) {
            const msg = err.response?.data || "Error al crear el elemento.";
            alert(typeof msg === "string" ? msg : "Error al crear el elemento.");
        }
    };

    const handleProcesarBaja = async () => {
        if (!elementoSeleccionado) return;

        try {
            await API.put(`/api/inventario/${elementoSeleccionado.id}/baja`, {
                razon: razonBaja,
                observacion: observacionBaja
            });

            setElementoSeleccionado(null);
            setObservacionBaja("");
            cargarInventario();
        } catch (err: any) {
            const msg = err.response?.data || "No se pudo procesar la baja.";
            alert(typeof msg === "string" ? msg : "No se pudo procesar la baja.");
        }
    };

    return (
        <div className="inventario-panel">
            <div className="panel-header">
                <h2>Gestión de Inventario del Predio</h2>
                <button className="btn-refresh" onClick={cargarInventario} title="Refrescar">
                    <RefreshCw size={18} className={loading ? "spin" : ""} />
                </button>
            </div>

            {/* Formulario de Alta */}
            <form className="alta-form" onSubmit={handleCrearElemento}>
                <div className="form-group">
                    <label htmlFor="nombre">Nuevo Elemento</label>
                    <input
                        id="nombre"
                        type="text"
                        placeholder="Ej: Notebook, Bomba de agua..."
                        value={nuevoNombre}
                        onChange={(e) => setNuevoNombre(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="categoria">Categoría</label>
                    <select id="categoria" value={nuevaCategoria} onChange={(e) => setNuevaCategoria(e.target.value)}>
                        <option value="MAQUINARIA_PESADA">Maquinaria Pesada</option>
                        <option value="HERRAMIENTAS_MANUALES">Herramientas Manuales</option>
                        <option value="EQUIPO_MEDICION">Equipo de Medición</option>
                        <option value="VEHICULOS_UTILITARIOS">Vehículos Utilitarios</option>
                        <option value="MOBILIARIO_OFICINA">Mobiliario de Oficina</option>
                        <option value="EQUIPO_INFORMATICO">Equipo Informático</option>
                        <option value="DISPOSITIVOS_RED">Dispositivos de Red</option>
                        <option value="SEGURIDAD_VIGILANCIA">Seguridad y Vigilancia</option>
                        <option value="PREVENCION_INCENDIOS">Prevención de Incendios</option>
                        <option value="ILUMINACION_ELECTRICIDAD">Iluminación y Electricidad</option>
                        <option value="REPUESTOS_INSUMOS">Repuestos e Insumos</option>
                        <option value="MATERIAL_CONSTRUCCION">Material de Construcción</option>
                        <option value="LIMPIEZA_MANTENIMIENTO">Limpieza y Mantenimiento</option>
                        <option value="PAPELERIA_ESCRITORIO">Papelería y Escritorio</option>
                        <option value="EQUIPO_PROTECCION_PERSONAL">Equipo de Protección Personal</option>
                        <option value="OTROS">Otros</option>
                    </select>
                </div>
                <button type="submit" className="btn-registrarElemento">
                    <Plus size={18} /> Registrar
                </button>
            </form>

            {/* Barra de Filtros */}
            <div className="filters-bar">
                <label className="checkbox-container">
                    <input
                        type="checkbox"
                        checked={soloActivos}
                        onChange={(e) => setSoloActivos(e.target.checked)}
                    />
                    <span>Mostrar solo elementos activos en el predio</span>
                </label>
            </div>

            {/* Listado / Tabla */}
            {error && <div className="error-message">{error}</div>}

            <div className="table-responsive">
                <table className="inventario-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Estado</th>
                            <th>Detalles de Baja</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            /* Bloque de carga activa insertado armónicamente en la tabla */
                            <tr>
                                <td colSpan={6} style={{ padding: "2.5rem 0", textAlign: "center" }}>
                                    <LoadingSpinner text="Buscando inventario..." />
                                </td>
                            </tr>
                        ) : (
                            <>
                                {elementos.map((item) => (
                                    <tr key={item.id} className={!item.activo ? "row-inactive" : ""}>
                                        <td><strong>#{item.id}</strong></td>
                                        <td>{item.nombre}</td>
                                        <td><span className={`badge cat-${item.categoria.toLowerCase()}`}>{item.categoria}</span></td>
                                        <td>
                                            <span className={`status-dot ${item.activo ? "active" : "inactive"}`}></span>
                                            {item.activo ? "Disponible" : "De Baja"}
                                        </td>
                                        <td className="baja-info-cell">
                                            {!item.activo && item.bajaRazonCategoria ? (
                                                <div>
                                                    <span className="reason-text">{item.bajaRazonCategoria}</span>
                                                    {item.bajaObservacion && <p className="obs-text">"{item.bajaObservacion}"</p>}
                                                </div>
                                            ) : (
                                                <span className="text-muted">-</span>
                                            )}
                                        </td>
                                        <td>
                                            {item.activo ? (
                                                <button
                                                    className="btn-delete"
                                                    onClick={() => setElementoSeleccionado(item)}
                                                    title="Dar de baja lógica"
                                                >
                                                    <Trash2 size={16} />
                                                </button>
                                            ) : (
                                                <span className="text-muted">Inmutable</span>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                {elementos.length === 0 && (
                                    <tr>
                                        <td colSpan={6} className="empty-row">No se encontraron elementos en el inventario.</td>
                                    </tr>
                                )}
                            </>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Modal de Baja Lógica */}
            {elementoSeleccionado && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h3><AlertTriangle size={20} className="warning-icon" /> Confirmar Baja Lógica</h3>
                        <p>Vas a retirar del estado activo al elemento: <strong>{elementoSeleccionado.nombre}</strong>.</p>

                        <div className="modal-form-group">
                            <label>Razón de la baja:</label>
                            <select value={razonBaja} onChange={(e) => setRazonBaja(e.target.value)}>
                                <option value="DESCARTADO">Descartado</option>
                                <option value="DESTRUIDO">Destruido</option>
                                <option value="DEVUELTO_A_DEPENDENCIA">Devuelto a Dependencia</option>
                                <option value="GUARDADO_EN_ALMACEN">Guardado en Almacén</option>
                                <option value="OTROS">Otros</option>
                            </select>
                        </div>

                        <div className="modal-form-group">
                            <label>Observaciones / Justificación:</label>
                            <textarea
                                rows={3}
                                placeholder="Escribí acá el motivo del retiro..."
                                value={observacionBaja}
                                onChange={(e) => setObservacionBaja(e.target.value)}
                            />
                        </div>

                        <div className="modal-actions">
                            <button className="btn-cancel" onClick={() => setElementoSeleccionado(null)}>Cancelar</button>
                            <button className="btn-confirm-baja" onClick={handleProcesarBaja}>Procesar Retiro</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}