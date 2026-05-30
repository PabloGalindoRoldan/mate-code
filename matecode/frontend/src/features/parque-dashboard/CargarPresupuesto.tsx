import { useState, useEffect } from "react";
import { toast } from 'sonner';
import { Save, AlertCircle } from "lucide-react";
import { presupuestoApi } from "../../api/axios";
import "./CargarPresupuesto.css";

export default function CargarPresupuesto({ onCargaExitosa, ejercicio }: { onCargaExitosa: () => void, ejercicio: number }) {
    const [partidas, setPartidas] = useState<{ id: number; codigo: string; nombre: string }[]>([]);

    // NUEVO: Agregamos documentoOrigen y fuenteFinanciamiento al estado
    const [formData, setFormData] = useState({
        partidaId: "",
        monto: "",
        documentoOrigen: "",
        fuente: "Tesoro Provincial"
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchPartidas = async () => {
            try {
                const data = await presupuestoApi.getCatalogo();
                setPartidas(data);
            } catch (err) {
                console.error("Error al cargar catálogo", err);
            }
        };
        fetchPartidas();
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const payload = [{
            partidaId: parseInt(formData.partidaId),
            monto: parseFloat(formData.monto),
            fuenteFinanciamiento: formData.fuente,
            documentoOrigen: formData.documentoOrigen, // Nuevo campo
            ejercicioFiscal: ejercicio
        }];

        setLoading(true);
        try {
            await presupuestoApi.cargarPresupuesto(payload);
            toast.success("Presupuesto cargado correctamente.");
            onCargaExitosa();
        } catch (err: any) {
            setError(err.response?.data?.error || "Error al registrar el presupuesto.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="carga-presupuesto-container">
            <h3><Save size={20} /> Asignación de Crédito Inicial ({ejercicio})</h3>

            {error && <div className="error-banner"><AlertCircle size={16} /> {error}</div>}

            <form onSubmit={handleSubmit} className="form-carga">
                <div className="field-group">
                    <label>Partida Presupuestaria:</label>
                    <select
                        required
                        value={formData.partidaId}
                        onChange={(e) => setFormData({ ...formData, partidaId: e.target.value })}
                    >
                        <option value="">Seleccione una partida...</option>
                        {partidas.map(p => (
                            <option key={p.id} value={p.id}>{p.codigo} - {p.nombre}</option>
                        ))}
                    </select>
                </div>

                <div className="field-group">
                    <label>Documento de Origen (Ej: Ley 5763 / Resol. Interna):</label>
                    <input
                        type="text"
                        required
                        value={formData.documentoOrigen}
                        onChange={(e) => setFormData({ ...formData, documentoOrigen: e.target.value })}
                        placeholder="Número de Ley o Resolución"
                    />
                </div>

                <div className="field-group">
                    <label>Fuente de Financiamiento:</label>
                    <select
                        value={formData.fuente}
                        onChange={(e) => setFormData({ ...formData, fuente: e.target.value })}
                    >
                        <option value="Tesoro Provincial">Tesoro Provincial</option>
                        <option value="Recursos Propios">Recursos Propios (Venta de tierras)</option>
                        <option value="Otras Fuentes">Otras Fuentes</option>
                    </select>
                </div>

                <div className="field-group">
                    <label>Monto Aprobado ($):</label>
                    <input
                        type="number"
                        step="0.01"
                        required
                        value={formData.monto}
                        onChange={(e) => setFormData({ ...formData, monto: e.target.value })}
                        placeholder="0.00"
                    />
                </div>

                <button type="submit" disabled={loading} className="btn-save">
                    {loading ? "Procesando..." : "Cargar Presupuesto"}
                </button>
            </form>
        </div>
    );
}