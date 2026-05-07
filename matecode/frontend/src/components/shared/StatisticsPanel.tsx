import { useState, useMemo } from "react";
import "./StatisticsPanel.css";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    type ChartData,
    type ChartOptions,
} from "chart.js";
import { Chart } from "react-chartjs-2";

// Register Line elements for the "Empleados" variable
ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

export default function StatisticsPanel({ empresaInfo }: { empresaInfo: any }) {
    const empresa = empresaInfo?.empresas?.[0];
    const consumos = empresa?.data?.consumos ?? [];

    // 1. State for Filters
    const [selectedYear, setSelectedYear] = useState<string>("All");

    // 2. Extract available years for the dropdown
    const availableYears = useMemo(() => {
        const years = consumos.map((c: any) => String(c.año));
        return ["All", ...Array.from(new Set(years))];
    }, [consumos]);

    // 3. Filter Data
    const filteredConsumos = useMemo(() => {
        return selectedYear === "All"
            ? consumos
            : consumos.filter((c: any) => String(c.año) === selectedYear);
    }, [consumos, selectedYear]);

    const labels = filteredConsumos.map((c: any) => `${c.mes}/${c.año}`);

    // 4. Chart Data using CSS Variables
    const data: ChartData<"bar" | "line"> = {
        labels,
        datasets: [
            {
                type: "bar" as const,
                label: "Electricidad (kWh)",
                data: filteredConsumos.map((c: any) => c.electricidad),
                backgroundColor: "rgba(140, 198, 63, 0.8)", // --verde1
                borderRadius: 4,
            },
            {
                type: "bar" as const,
                label: "Agua (m³)",
                data: filteredConsumos.map((c: any) => c.agua),
                backgroundColor: "rgba(117, 148, 77, 0.8)", // --verde2
                borderRadius: 4,
            },
            {
                type: "bar" as const,
                label: "Gas (m³)",
                data: filteredConsumos.map((c: any) => c.gas),
                backgroundColor: "rgba(85, 97, 70, 0.8)", // --verde3
                borderRadius: 4,
            },
            {
                type: "line" as const,
                label: "Empleados",
                data: filteredConsumos.map((c: any) => c.empleados ?? 0),
                borderColor: "#30332D", // --gris1
                backgroundColor: "#30332D",
                borderWidth: 3,
                pointRadius: 4,
                yAxisID: "y1", // Dual Axis
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
                title: { display: true, text: "Cantidad de Empleados" },
            },
        },
    };

    return (
        <div className="statistics-container">
            <header className="statistics-header">
                <h2>Estadísticas de la Empresa</h2>
                <div className="filter-group">
                    <label htmlFor="year-filter">Año:</label>
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
            </header>

            <section className="chart-wrapper">
                <Chart type="bar" data={data} options={options} />
            </section>
        </div>
    );
}