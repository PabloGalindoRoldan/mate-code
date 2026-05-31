import { useState } from "react";
import { authApi } from "../../api/axios";
import { Save } from "lucide-react";
import { toast } from "sonner";
import "./SettingsPanel.css";

export default function SettingsPanel() {
    const [formData, setFormData] = useState({ currentPassword: "", newPassword: "", confirmPassword: "" });
    const [loading, setLoading] = useState(false);

    const LABELS: Record<string, string> = {
        currentPassword: "CONTRASEÑA ACTUAL",
        newPassword: "NUEVA CONTRASEÑA",
        confirmPassword: "CONFIRMAR CONTRASEÑA"
    };

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();
        if (formData.newPassword !== formData.confirmPassword) {
            return toast.error("Las nuevas contraseñas no coinciden.");
        }

        setLoading(true);
        try {
            await authApi.changePassword(formData);
            toast.success("Seguridad actualizada correctamente");
            setFormData({ currentPassword: "", newPassword: "", confirmPassword: "" });
        } catch (err: any) {
            toast.error(err?.response?.data || "Error al actualizar la contraseña.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="settings-panel-container">
            <div className="settings-card">
                <div className="settings-header">
                    <div>
                        <h2>Seguridad de Cuenta</h2>
                        <p>Cambiar contraseña</p>
                    </div>
                </div>

                <form onSubmit={handleSubmit} className="password-form">
                    {['currentPassword', 'newPassword', 'confirmPassword'].map((field) => (
                        <div className="form-group" key={field}>
                            <label>{LABELS[field]}</label>
                            <input
                                type="password"
                                placeholder="••••••••"
                                value={formData[field as keyof typeof formData]}
                                onChange={(e) => setFormData({ ...formData, [field]: e.target.value })}
                                required
                            />
                        </div>
                    ))}

                    <button type="submit" className="save-btn" disabled={loading}>
                        {loading ? "Procesando..." : <><Save size={18} /> Actualizar Contraseña</>}
                    </button>
                </form>
            </div>
        </div>
    );
}