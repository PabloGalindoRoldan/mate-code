import { useEffect, useState, useMemo } from 'react';
import { empresasApi, consumosApi } from '../../api/axios';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';
import { Building2, Users, Zap, Layers, RefreshCw, Calendar, Flame, Truck } from 'lucide-react';
import './EmpresasPanel.css';
import LoadingSpinner from '../../ui/loading/LoadingSpinner';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

interface Empresa {
    identificacion: string;
    razonSocial: string;
    esRadicada: boolean;
    idlote?: number | null;
    cantidadEmpleados?: number;
}

// Directly mirrors your Java ConsumoResponseDTO backend model
interface ConsumoRecord {
    id: number;
    mes: number;
    ano: number;
    gas: number;       // BigDecimal deserializes as a native number
    luz: number;       // BigDecimal deserializes as a native number
    agua: number;      // BigDecimal deserializes as a native number
    empleados: number;
    vehiculos: number;
    fechaCarga: string;
}

export default function EmpresasPanel() {
    const [empresas, setEmpresas] = useState<Empresa[]>([]);
    const [consumos, setConsumos] = useState<ConsumoRecord[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Active filters
    const [selectedCuit, setSelectedCuit] = useState<string>('ALL');
    const [selectedYear, setSelectedYear] = useState<number>(2026);
    const [selectedMonth, setSelectedMonth] = useState<string>('ALL');

    // Form Mutation Buffers
    const [editingCuit, setEditingCuit] = useState<string | null>(null);
    const [newLote, setNewLote] = useState<string>('');

    // 1. Carga inicial de empresas (Solo se ejecuta al montar el componente)
    useEffect(() => {
        const cargarEmpresasInicial = async () => {
            try {
                const empData = await empresasApi.listarEmpresas();
                setEmpresas(empData);
            } catch (err) {
                console.error("Error al cargar el padrón de empresas:", err);
            }
        };
        cargarEmpresasInicial();
    }, []);

    // 2. Orquesta la extracción de datos según los filtros dinámicos (Año y Empresa seleccionada)
    const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);
            let consData: ConsumoRecord[] = [];

            if (selectedCuit === 'ALL') {
                // Si está en 'ALL', consumimos el reporte global anualizado actual
                consData = await consumosApi.getReporteGlobal(selectedYear);
            } else {
                // Si hay una empresa puntual, consumimos su historial específico.
                // NOTA: Si tu API requiere pasar el CUIT en la URL de administración, asegúrate
                // de que consumosApi.getHistorialPorEmpresa reciba (selectedCuit, selectedYear) o filtre localmente.
                const historialCompleto = await consumosApi.getHistorialPorEmpresa(selectedCuit);

                // Filtramos en el cliente para quedarnos solo con el año del selector
                consData = historialCompleto.filter((c: ConsumoRecord) => c.ano === selectedYear);
            }

            // Ordenamos cronológicamente de Enero a Diciembre para que impacte bien en la UI
            const sortedConsumos = [...consData].sort((a, b) => {
                if (a.ano !== b.ano) return a.ano - b.ano;
                return a.mes - b.mes;
            });

            setConsumos(sortedConsumos);
        } catch (err: any) {
            setError('Error cargando la matriz de consumos del período o empresa seleccionada.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // 3. Disparador reactivo: Cada vez que cambie el año o la empresa en el select, se actualizan los datos
    useEffect(() => {
        fetchData();
    }, [selectedYear, selectedCuit]);

    const handleToggleRadicacion = async (empresa: Empresa) => {
        try {
            const nuevoEstado = !empresa.esRadicada;
            await empresasApi.actualizarEstadoRadicacion(empresa.identificacion, nuevoEstado);

            setEmpresas(prev => prev.map(e =>
                e.identificacion === empresa.identificacion
                    ? { ...e, esRadicada: nuevoEstado }
                    : e
            ));
        } catch (err) {
            alert('Error al modificar el estado de radicación corporativa.');
        }
    };

    const handleSaveLote = async (cuit: string) => {
        try {
            const loteIdParsed = newLote.trim() === '' ? null : Number(newLote);
            if (loteIdParsed !== null && isNaN(loteIdParsed)) {
                alert('ID de lote inválido');
                return;
            }
            await empresasApi.asignarLote(cuit, loteIdParsed);
            setEmpresas(prev => prev.map(e => e.identificacion === cuit ? { ...e, idlote: loteIdParsed } : e));
            setEditingCuit(null);
            setNewLote('');
        } catch (err) {
            alert('Error al actualizar la asignación parcelaria del lote.');
        }
    };

    // Filtered dynamic list context builder
    const filteredConsumos = useMemo(() => {
        return consumos.filter(c => {
            const matchesMonth = selectedMonth === 'ALL' || String(c.mes) === selectedMonth;
            return matchesMonth;
        });
    }, [consumos, selectedMonth]);

    // Fixed calculation matrix using direct numeric properties
    const metrics = useMemo(() => {
        // Resource consumptions are cumulative totals based on filters
        const totalPower = filteredConsumos.reduce((acc, curr) => acc + (curr.luz || 0), 0);
        const totalGas = filteredConsumos.reduce((acc, curr) => acc + (curr.gas || 0), 0);
        const totalWater = filteredConsumos.reduce((acc, curr) => acc + (curr.agua || 0), 0);

        // Reference helper array for display strings
        const baseMonths = [
            'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
            'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
        ];

        // Identify the last registered record within the current filtered array context
        // (consumos are already pre-sorted chronologically in fetchData)
        const lastRecord = filteredConsumos.length > 0 ? filteredConsumos[filteredConsumos.length - 1] : null;

        const latestStaff = lastRecord ? (lastRecord.empleados || 0) : 0;
        const latestVehicles = lastRecord ? (lastRecord.vehiculos || 0) : 0;
        const activeMonthLabel = lastRecord ? baseMonths[lastRecord.mes - 1] : 'N/A';

        return {
            totalPower,
            totalGas,
            totalWater,
            latestStaff,
            latestVehicles,
            activeMonthLabel
        };
    }, [filteredConsumos]);

    // Dataset generation mapping metrics cleanly across intervals
    const chartData = useMemo(() => {
        const baseMonths = [
            'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
            'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
        ];

        const aggregatedLuz = Array(12).fill(0);
        const aggregatedGas = Array(12).fill(0);
        const aggregatedAgua = Array(12).fill(0);
        const aggregatedStaff = Array(12).fill(0);
        const aggregatedVehiculos = Array(12).fill(0);

        consumos.forEach(c => {
            if (c.mes >= 1 && c.mes <= 12) {
                const idx = c.mes - 1;
                aggregatedLuz[idx] += (c.luz || 0);
                aggregatedGas[idx] += (c.gas || 0);
                aggregatedAgua[idx] += (c.agua || 0);
                aggregatedStaff[idx] += (c.empleados || 0);
                aggregatedVehiculos[idx] += (c.vehiculos || 0);
            }
        });

        let labels = baseMonths;
        let dataLuz = aggregatedLuz;
        let dataGas = aggregatedGas;
        let dataAgua = aggregatedAgua;
        let dataStaff = aggregatedStaff;
        let dataVehiculos = aggregatedVehiculos;

        if (selectedMonth !== 'ALL') {
            const mIdx = Number(selectedMonth) - 1;
            labels = [baseMonths[mIdx]];
            dataLuz = [aggregatedLuz[mIdx]];
            dataGas = [aggregatedGas[mIdx]];
            dataAgua = [aggregatedAgua[mIdx]];
            dataStaff = [aggregatedStaff[mIdx]];
            dataVehiculos = [aggregatedVehiculos[mIdx]];
        }

        return {
            labels,
            datasets: [
                {
                    label: 'Electricidad (kWh)',
                    data: dataLuz,
                    backgroundColor: 'rgba(46, 125, 50, 0.75)',
                    borderColor: 'rgb(46, 125, 50)',
                    borderWidth: 1,
                },
                {
                    label: 'Gas (m³)',
                    data: dataGas,
                    backgroundColor: 'rgba(239, 108, 0, 0.75)',
                    borderColor: 'rgb(239, 108, 0)',
                    borderWidth: 1,
                },
                {
                    label: 'Agua (m³)',
                    data: dataAgua,
                    backgroundColor: 'rgba(0, 188, 212, 0.75)',
                    borderColor: 'rgb(0, 188, 212)',
                    borderWidth: 1,
                },
                {
                    label: 'Empleados',
                    data: dataStaff,
                    backgroundColor: 'rgba(21, 101, 192, 0.75)',
                    borderColor: 'rgb(21, 101, 192)',
                    borderWidth: 1,
                },
                {
                    label: 'Vehículos',
                    data: dataVehiculos,
                    backgroundColor: 'rgba(106, 27, 154, 0.75)',
                    borderColor: 'rgb(106, 27, 154)',
                    borderWidth: 1,
                }
            ]
        };
    }, [consumos, selectedMonth]);

    if (loading) return <LoadingSpinner text="Cargando métricas de control y seguimiento corporativo..." />
    if (error) return <div className="panel-error-card">{error}</div>;

    return (
        <div className="empresas-panel-container">
            <header className="panel-dashboard-header">
                <h2>Monitoreo de Empresas y Actividad Industrial en el Parque</h2>
                <button className="refresh-action-btn" onClick={fetchData} title="Sincronizar Datos">
                    <RefreshCw size={16} /> Actualizar
                </button>
            </header>

            {/* CONTROL PANEL FILTERS SECTION */}
            <section className="filter-dashboard-card">
                <div className="filter-input-group">
                    <label><Building2 size={14} /> Filtrar Empresa</label>
                    <select value={selectedCuit} onChange={(e) => setSelectedCuit(e.target.value)}>
                        <option value="ALL">--- Todas las Empresas (Consolidado) ---</option>
                        {empresas.map(emp => (
                            <option key={emp.identificacion} value={emp.identificacion}>
                                {emp.razonSocial} ({emp.identificacion})
                            </option>
                        ))}
                    </select>
                </div>

                <div className="filter-input-group">
                    <label><Calendar size={14} /> Ejercicio Anual</label>
                    <select value={selectedYear} onChange={(e) => setSelectedYear(Number(e.target.value))}>
                        {[2024, 2025, 2026, 2027].map(yr => (
                            <option key={yr} value={yr}>{yr}</option>
                        ))}
                    </select>
                </div>

                <div className="filter-input-group">
                    <label><Calendar size={14} /> Filtro Período Mensual</label>
                    <select value={selectedMonth} onChange={(e) => setSelectedMonth(e.target.value)}>
                        <option value="ALL">Ver Año Completo</option>
                        <option value="1">Enero</option>
                        <option value="2">Febrero</option>
                        <option value="3">Marzo</option>
                        <option value="4">Abril</option>
                        <option value="5">Mayo</option>
                        <option value="6">Junio</option>
                        <option value="7">Julio</option>
                        <option value="8">Agosto</option>
                        <option value="9">Septiembre</option>
                        <option value="10">Octubre</option>
                        <option value="11">Noviembre</option>
                        <option value="12">Diciembre</option>
                    </select>
                </div>
            </section>

            {/* DYNAMIC METRICS SUMMARY ROW */}
            <section className="analytics-metrics-grid">
                <div className="metric-panel-card">
                    <div className="icon-badge power"><Zap size={22} /></div>
                    <div className="metric-data">
                        <span className="metric-title">Electricidad Total</span>
                        <h3>{metrics.totalPower.toLocaleString()} <small>kWh</small></h3>
                    </div>
                </div>

                <div className="metric-panel-card">
                    <div className="icon-badge gas"><Flame size={22} style={{ color: '#ef6c00' }} /></div>
                    <div className="metric-data">
                        <span className="metric-title">Gas Total</span>
                        <h3>{metrics.totalGas.toLocaleString()} <small>m³</small></h3>
                    </div>
                </div>

                <div className="metric-panel-card">
                    <div className="icon-badge staff"><Users size={22} /></div>
                    <div className="metric-data">
                        <span className="metric-title">
                            Fuerza Laboral <span className="metric-subtitle-badge">({metrics.activeMonthLabel})</span>
                        </span>
                        <h3>{metrics.latestStaff.toLocaleString()} <small>Empleados</small></h3>
                    </div>
                </div>

                <div className="metric-panel-card">
                    <div className="icon-badge vehicles"><Truck size={22} style={{ color: '#6a1b9a' }} /></div>
                    <div className="metric-data">
                        <span className="metric-title">
                            Flota Activa <span className="metric-subtitle-badge">({metrics.activeMonthLabel})</span>
                        </span>
                        <h3>{metrics.latestVehicles.toLocaleString()} <small>Vehículos</small></h3>
                    </div>
                </div>
            </section>

            {/* CHART LAYER */}
            <section className="chart-visualization-container">
                <h3>Historial de Demanda y Recursos Operacionales</h3>
                <div className="chart-box-render" style={{ height: '380px', position: 'relative' }}>
                    <Bar
                        data={chartData}
                        options={{
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: { display: true, position: 'top' as const }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true
                                }
                            }
                        }}
                    />
                </div>
            </section>

            {/* DATA INVENTORY LISTING */}
            <section className="management-table-card">
                <h3>Padrón Técnico de Firmas e Infraestructura Asignada</h3>
                <div className="table-responsive-wrapper">
                    <table className="admin-companies-table">
                        <thead>
                            <tr>
                                <th>Razón Social</th>
                                <th>CUIT</th>
                                <th>Lote Vinculado</th>
                                <th>Estatus Radicación</th>
                                <th>Acciones Administrativas</th>
                            </tr>
                        </thead>
                        <tbody>
                            {empresas.map((emp) => (
                                <tr key={emp.identificacion}>
                                    <td className="company-name-bold">{emp.razonSocial}</td>
                                    <td><code>{emp.identificacion}</code></td>
                                    <td>
                                        {editingCuit === emp.identificacion ? (
                                            <div className="inline-lote-editor">
                                                <input
                                                    type="text"
                                                    placeholder="N° Lote"
                                                    value={newLote}
                                                    onChange={(e) => setNewLote(e.target.value)}
                                                />
                                                <button className="btn-save" onClick={() => handleSaveLote(emp.identificacion)}>Guardar</button>
                                                <button className="btn-cancel" onClick={() => setEditingCuit(null)}>X</button>
                                            </div>
                                        ) : (
                                            <span className="lote-display-badge">
                                                <Layers size={12} />{' '}
                                                {emp.idlote !== null && emp.idlote !== undefined
                                                    ? `Lote ${emp.idlote}`
                                                    : 'Sin Lote Asignado'}
                                            </span>
                                        )}
                                    </td>
                                    <td>
                                        <span className={`radicada-toggle-pill ${emp.esRadicada ? 'true' : 'false'}`}>
                                            {emp.esRadicada ? 'Radicada' : 'No Radicada'}
                                        </span>
                                    </td>
                                    <td>
                                        <div className="action-row-buttons">
                                            {editingCuit !== emp.identificacion && (
                                                <button className="action-link-btn" onClick={() => {
                                                    setEditingCuit(emp.identificacion);
                                                    setNewLote(emp.idlote !== null && emp.idlote !== undefined ? String(emp.idlote) : '');
                                                }}>
                                                    Modificar Lote
                                                </button>
                                            )}
                                            <button
                                                className={`action-toggle-btn ${emp.esRadicada ? 'danger' : 'success'}`}
                                                onClick={() => handleToggleRadicacion(emp)}
                                            >
                                                {emp.esRadicada ? 'Cambiar a No Radicada' : 'Establecer Radicada'}
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    );
}