import { useState } from "react";
import { authApi } from "../../api/axios";
import { Lock, ShieldCheck } from "lucide-react";
import "./SettingsPanel.css";

export default function SettingsPanel() {

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        setError("");
        setSuccess("");

        if (newPassword !== confirmPassword) {
            setError("Las nuevas contraseñas no coinciden.");
            return;
        }

        if (newPassword.length < 6) {
            setError("La nueva contraseña debe tener al menos 6 caracteres.");
            return;
        }

        try {
            setLoading(true);

            await authApi.changePassword({
                currentPassword,
                newPassword,
                confirmPassword
            });

            setSuccess("Contraseña actualizada correctamente.");

            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");

        } catch (err: any) {
            setError(
                err?.response?.data ||
                "No se pudo actualizar la contraseña."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="settings-panel-container">

            <div className="settings-card">
                <div className="settings-header">
                    <ShieldCheck size={22} />
                    <div>
                        <h2>Seguridad de la Cuenta</h2>
                        <p>Actualice sus credenciales de acceso.</p>
                    </div>
                </div>

                <form onSubmit={handleSubmit} className="password-form">

                    <div className="form-group">
                        <label>
                            <Lock size={14} />
                            Contraseña Actual
                        </label>

                        <input
                            type="password"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>
                            <Lock size={14} />
                            Nueva Contraseña
                        </label>

                        <input
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>
                            <Lock size={14} />
                            Confirmar Nueva Contraseña
                        </label>

                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>

                    {error && (
                        <div className="settings-error">
                            {error}
                        </div>
                    )}

                    {success && (
                        <div className="settings-success">
                            {success}
                        </div>
                    )}

                    <button
                        type="submit"
                        className="save-password-btn"
                        disabled={loading}
                    >
                        {loading ? "Actualizando..." : "Actualizar Contraseña"}
                    </button>

                </form>
            </div>
        </div>
    );
}