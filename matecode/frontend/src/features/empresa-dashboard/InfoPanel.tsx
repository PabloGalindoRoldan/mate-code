import { useState, useEffect, useMemo } from "react";
import api from "../../api/axios";
import "./InfoPanel.css";
import {
    Chart as ChartJS,
    registerables,
    type ChartData,
    type ChartOptions,
} from "chart.js";
import { Chart } from "react-chartjs-2";

ChartJS.register(...registerables);

interface ConsumoData {
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

interface InfoPanelProps {
    empresa: any;
    usuario: any; // Objeto directo del usuario logueado en la sesión activa
}

export default function InfoPanel({ empresa, usuario }: InfoPanelProps) {
    // Estados para la lógica asíncrona de los gráficos
    const [consumos, setConsumos] = useState<ConsumoData[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedYear, setSelectedYear] = useState<string>("Todos");

    // 1. Efecto para buscar el historial real de consumos
    useEffect(() => {
        const fetchConsumos = async () => {
            try {
                setLoading(true);
                const response = await api.get("/api/consumos/historial");
                // Invertimos el orden para que la cronología en el gráfico sea de izquierda a derecha
                setConsumos([...response.data].reverse());
                setError(null);
            } catch (err: any) {
                console.error("Error al traer estadísticas:", err);
                setError("No se pudieron cargar las métricas analíticas.");
            } finally {
                setLoading(false);
            }
        };

        fetchConsumos();
    }, []);

    // 2. Extraer los años únicos disponibles dinámicamente
    const availableYears = useMemo(() => {
        const years = consumos.map((c) => String(c.ano));
        return ["Todos", ...Array.from(new Set(years))];
    }, [consumos]);

    // 3. Filtrar consumos según el año elegido en el select
    const filteredConsumos = useMemo(() => {
        return selectedYear === "Todos"
            ? consumos
            : consumos.filter((c) => String(c.ano) === selectedYear);
    }, [consumos, selectedYear]);

    // Eje X del gráfico: Formato "Mes/Año"
    const labels = filteredConsumos.map((c) => `${c.mes}/${c.ano}`);

    // 4. Configurar el set de datos mapeando las columnas unificadas de la Base de Datos
    const data: ChartData<"bar" | "line"> = {
        labels,
        datasets: [
            {
                type: "bar" as const,
                label: "Electricidad (kWh)",
                data: filteredConsumos.map((c) => c.luz),
                backgroundColor: "rgba(140, 198, 63, 0.8)", // --verde1
                borderRadius: 4,
            },
            {
                type: "bar" as const,
                label: "Agua (m³)",
                data: filteredConsumos.map((c) => c.agua),
                backgroundColor: "rgba(117, 148, 77, 0.8)", // --verde2
                borderRadius: 4,
            },
            {
                type: "bar" as const,
                label: "Gas (m³)",
                data: filteredConsumos.map((c) => c.gas),
                backgroundColor: "rgba(85, 97, 70, 0.8)", // --verde3
                borderRadius: 4,
            },
            {
                type: "line" as const,
                label: "Empleados",
                data: filteredConsumos.map((c) => c.empleados),
                borderColor: "#444841", // --gris1
                backgroundColor: "#444841",
                borderWidth: 3,
                pointRadius: 4,
                yAxisID: "y1", // Asigna el eje secundario derecho
            },
            {
                type: "line" as const,
                label: "Vehículos",
                data: filteredConsumos.map((c) => c.vehiculos),
                borderColor: "#757872",
                backgroundColor: "#757872",
                borderWidth: 3,
                pointRadius: 4,
                yAxisID: "y1", // Comparte el mismo eje numérico derecho
            },
        ],
    };

    const options: ChartOptions<"bar" | "line"> = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { position: "top" },
            title: { display: false },
        },
        scales: {
            x: { grid: { display: false } },
            y: {
                type: "linear",
                display: true,
                position: "left",
                title: { display: true, text: "Consumo (Unidades)" },
            },
            y1: {
                type: "linear",
                display: true,
                position: "right",
                grid: { drawOnChartArea: false },
                title: { display: true, text: "Cantidad (Empleados / Vehículos)" },
            },
        },
    };

    return (
        <div className="dashboardUnificado">
            {/* SECCIÓN SUPERIOR: Ficha Institucional de la Empresa */}
            <section className="profile-section">
                <div className="profile-header">
                    <h2>Ficha Institucional</h2>
                    <span className="badge-radicada">
                        {empresa?.esRadicada ? "Empresa Radicada" : "Empresa no Radicada"}
                    </span>
                </div>
                <div className="profile-grid">
                    {/* --- BLOQUE EMPRESA --- */}
                    <div className="info-card">
                        <span className="card-label">Razón Social</span>
                        <strong className="card-value">{empresa?.razonSocial || "No disponible"}</strong>
                    </div>

                    <div className="info-card">
                        <span className="card-label">CUIT de la Firma</span>
                        <strong className="card-value">{empresa?.cuit || "No disponible"}</strong>
                    </div>

                    {/* --- BLOQUE OPERADOR (Directo de la sesión activa) --- */}
                    <div className="info-card">
                        <span className="card-label">Contacto Administrativo</span>
                        <strong className="card-value">
                            {usuario ? `${usuario.nombre} ${usuario.apellido}` : "No asignado"}
                        </strong>
                    </div>

                    <div className="info-card">
                        <span className="card-label">CUIT del Operador</span>
                        <strong className="card-value">
                            {usuario?.cuit || "No disponible"}
                        </strong>
                    </div>

                    {/* --- BLOQUE CREDENCIALES --- */}
                    <div className="info-card">
                        <span className="card-label">Cuenta de Usuario</span>
                        <strong className="card-value" style={{ color: "var(--verde2)" }}>
                            {usuario ? `@${usuario.nombreUsuario}` : "No disponible"}
                        </strong>
                    </div>

                    <div className="info-card">
                        <span className="card-label">Correo Electrónico</span>
                        <strong className="card-value">
                            {usuario?.email || "No disponible"}
                        </strong>
                    </div>
                </div>
            </section>

            <hr className="divider" />

            {/* SECCIÓN INFERIOR: Métricas Analíticas de Consumo (API) */}
            <section className="metrics-section">
                <header className="metrics-header">
                    <div className="metrics-title">
                        <h3>Métricas de Rendimiento</h3>
                        <p>Evolución histórica de servicios industriales y recursos activos.</p>
                    </div>

                    {consumos.length > 0 && !loading && !error && (
                        <div className="filter-group">
                            <label htmlFor="year-filter">Filtrar por Año:</label>
                            <select
                                id="year-filter"
                                value={selectedYear}
                                onChange={(e) => setSelectedYear(e.target.value)}
                            >
                                {availableYears.map((year) => (
                                    <option key={String(year)} value={String(year)}>
                                        {String(year)}
                                    </option>
                                ))}
                            </select>
                        </div>
                    )}
                </header>

                {/* Renderizados Condicionales Internos de la Gráfica */}
                {loading && (
                    <div className="chart-status-container">
                        <p className="loading-text">Cargando datos métricos desde el servidor de Railway...</p>
                    </div>
                )}

                {error && (
                    <div className="chart-status-container error-box">
                        <p className="error-text">{error}</p>
                    </div>
                )}

                {!loading && !error && consumos.length === 0 && (
                    <div className="chart-status-container empty-box">
                        <p>No se registran declaraciones de consumo previas para graficar.</p>
                    </div>
                )}

                {!loading && !error && consumos.length > 0 && (
                    <div className="chart-wrapper">
                        <Chart type="bar" data={data} options={options} />
                    </div>
                )}
            </section>
        </div>
    );
}