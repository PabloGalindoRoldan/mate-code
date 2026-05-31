import { useState } from "react";
import { toast } from 'sonner';
import { presupuestoApi } from "../../api/axios";
import "./CrearPartida.css"
import "./CargarPresupuesto.css"

export default function CrearPartida({ onGuardadoExitoso }: { onGuardadoExitoso: () => void }) {
    // Inicializamos con los nuevos campos requeridos
    const [formData, setFormData] = useState({
        codigo: "",
        nombre: "",
        nivel: "PRINCIPAL" as 'PRINCIPAL' | 'PARCIAL' | 'SUBPARCIAL',
        parentId: null as number | null
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await presupuestoApi.crearPartida(formData);
            toast.success("Partida creada correctamente");
            onGuardadoExitoso();
            setFormData({ codigo: "", nombre: "", nivel: "PRINCIPAL", parentId: null });
        } catch (err: any) {
            // Mostramos el mensaje de error real del backend si existe
            const mensaje = err.response?.data?.error || "Error al crear la partida";
            toast.error(mensaje);
        }
    };

    return (
        <div className="carga-presupuesto-container">
            <h3> Nueva Partida</h3>

            <form onSubmit={handleSubmit} className="form-carga">
                <div className="field-group">
                    <label>Código:</label>
                    <input
                        placeholder="Ej: 1.1.0"
                        value={formData.codigo}
                        onChange={e => setFormData({ ...formData, codigo: e.target.value })}
                        required
                    />
                </div>

                <div className="field-group">
                    <label>Nombre de la partida:</label>
                    <input
                        placeholder="Descripción de la partida"
                        value={formData.nombre}
                        onChange={e => setFormData({ ...formData, nombre: e.target.value })}
                        required
                    />
                </div>

                <div className="field-group">
                    <label>Nivel:</label>
                    <select
                        value={formData.nivel}
                        onChange={e => setFormData({ ...formData, nivel: e.target.value as any })}
                    >
                        <option value="PRINCIPAL">PRINCIPAL</option>
                        <option value="PARCIAL">PARCIAL</option>
                        <option value="SUBPARCIAL">SUBPARCIAL</option>
                    </select>
                </div>

                <button type="submit" className="btn-save">Guardar Partida</button>
            </form>
        </div>
    );
}