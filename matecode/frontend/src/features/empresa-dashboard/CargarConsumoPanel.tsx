import React, { useState, useEffect } from "react";
import api from "../../api/axios"; // Ajustá la ruta según cómo esté exportado tu cliente de Axios
import "./CargarConsumoPanel.css";

interface ConsumoHistorial {
    id: number;
    mes: number;
    ano: number;
    gas: number;
    luz: number;
    agua: number;
    empleados: number;
    vehiculos: number;
    fechaCarga: string;
}

export default function CargarConsumoPanel() {
    // 1. Estado del Formulario (Arrancamos apuntando al mes/año actual en 2026)
    const [formData, setFormData] = useState({
        mes: new Date().getMonth() + 1,
        ano: 2026,
        gas: "",
        luz: "",
        agua: "",
        empleados: "",
        vehiculos: "",
    });

    const [historial, setHistorial] = useState<ConsumoHistorial[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    // 2. Cargar el historial apenas se monta el componente
    const cargarHistorial = async () => {
        try {
            const response = await api.get("/api/consumos/historial");
            setHistorial(response.data);

            // Opcional: Si hay datos previos, aplicamos el "arrastre de datos" para facilitar la carga
            if (response.data.length > 0) {
                const ultimo = response.data[0]; // Ya viene ordenado descendente desde el DAO
                setFormData(prev => ({
                    ...prev,
                    empleados: String(ultimo.empleados),
                    vehiculos: String(ultimo.vehiculos)
                }));
            }
        } catch (err: any) {
            console.error("Error cargando historial:", err);
        }
    };

    useEffect(() => {
        cargarHistorial();
    }, []);

    // 3. Manejadores de eventos
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);

        // Parseo seguro de inputs a tipos numéricos correctos
        const payload = {
            mes: Number(formData.mes),
            ano: Number(formData.ano),
            gas: Number(formData.gas) || 0,
            luz: Number(formData.luz) || 0,
            agua: Number(formData.agua) || 0,
            empleados: Number(formData.empleados) || 0,
            vehiculos: Number(formData.vehiculos) || 0
        };

        try {
            const response = await api.post("/api/consumos", payload);
            setSuccess(response.data); // Mensaje del backend

            // Limpiamos las variables de consumo pero conservamos empleados/vehículos (arrastre)
            setFormData(prev => ({
                ...prev,
                gas: "",
                luz: "",
                agua: ""
            }));

            // Refrescamos la tabla inferior con el nuevo registro
            cargarHistorial();
        } catch (err: any) {
            // Capturamos el mensaje de error provisto por el GlobalExceptionHandler del Backend
            const msg = err.response?.data || "Ocurrió un error al registrar el consumo.";
            setError(typeof msg === "string" ? msg : "El período ya se encuentra declarado.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="cargarConsumoPanel">
            <header className="panel-header">
                <h2>Declaración Mensual de Consumos</h2>
                <p>Cargue los registros métricos y de personal correspondientes a su empresa.</p>
            </header>

            {/* Alertas de Feedback */}
            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            {/* Formulario de Carga */}
            <form onSubmit={handleSubmit} className="consumo-form">
                <div className="form-grid">
                    <div className="form-group">
                        <label htmlFor="mes">Mes</label>
                        <select id="mes" name="mes" value={formData.mes} onChange={handleChange}>
                            {Array.from({ length: 12 }, (_, i) => (
                                <option key={i + 1} value={i + 1}>
                                    {new Date(2026, i).toLocaleString("es-AR", { month: "long" })}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="ano">Año</label>
                        <select id="ano" name="ano" value={formData.ano} onChange={handleChange}>
                            <option value={2026}>2026</option>
                            <option value={2027}>2027</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="luz">Electricidad (kWh)</label>
                        <input type="number" id="luz" name="luz" step="0.01" required value={formData.luz} onChange={handleChange} placeholder="0.00" />
                    </div>

                    <div className="form-group">
                        <label htmlFor="gas">Gas (m³)</label>
                        <input type="number" id="gas" name="gas" step="0.01" required value={formData.gas} onChange={handleChange} placeholder="0.00" />
                    </div>

                    <div className="form-group">
                        <label htmlFor="agua">Agua (m³)</label>
                        <input type="number" id="agua" name="agua" step="0.01" required value={formData.agua} onChange={handleChange} placeholder="0.00" />
                    </div>

                    <div className="form-group">
                        <label htmlFor="empleados">Nº de Empleados</label>
                        <input type="number" id="empleados" name="empleados" required value={formData.empleados} onChange={handleChange} placeholder="0" />
                    </div>

                    <div className="form-group">
                        <label htmlFor="vehiculos">Vehículos en Uso</label>
                        <input type="number" id="vehiculos" name="vehiculos" required value={formData.vehiculos} onChange={handleChange} placeholder="0" />
                    </div>
                </div>

                <button type="submit" className="submit-btn" disabled={loading}>
                    {loading ? "Registrando..." : "Guardar Declaración"}
                </button>
            </form>

            {/* Historial Declarado */}
            <section className="historial-section">
                <h3>Historial de Declaraciones</h3>
                {historial.length === 0 ? (
                    <p className="no-data">No se registran consumos declarados previamente.</p>
                ) : (
                    <div className="table-responsive">
                        <table className="historial-table">
                            <thead>
                                <tr>
                                    <th>Período</th>
                                    <th>Electricidad</th>
                                    <th>Gas</th>
                                    <th>Agua</th>
                                    <th>Empleados</th>
                                    <th>Vehículos</th>
                                    <th>Fecha Carga</th>
                                </tr>
                            </thead>
                            <tbody>
                                {historial.map((c) => (
                                    <tr key={c.id}>
                                        <td><strong>{c.mes}/{c.ano}</strong></td>
                                        <td>{c.luz} kWh</td>
                                        <td>{c.gas} m³</td>
                                        <td>{c.agua} m³</td>
                                        <td>{c.empleados}</td>
                                        <td>{c.vehiculos}</td>
                                        <td>{new Date(c.fechaCarga).toLocaleDateString("es-AR")}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </section>
        </div>
    );
}