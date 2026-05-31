import React, { useState } from 'react';
import api from '../../api/axios';
import Alert from '../../ui/alert/Alert';
import './ConfiguracionPanel.css';

interface ConfiguracionPanelProps {
    empresa: {
        cuit: string;
        razonSocial?: string;
    };
}

export default function ConfiguracionPanel({ empresa }: ConfiguracionPanelProps) {
    // El estado del formulario solo maneja lo que el administrador escribe
    const [formData, setFormData] = useState({
        nombreUsuario: '',
        nombre: '',
        apellido: '',
        email: '',
        cuitUsuario: '',
        password: '',
        confirmarPassword: ''
    });

    const [passwordData, setPasswordData] = useState({
        currentPassword: "",
        newPassword: "",
        confirmPassword: ""
    });

    const [pwLoading, setPwLoading] = useState(false);
    const [pwError, setPwError] = useState<string | null>(null);
    const [pwSuccess, setPwSuccess] = useState<string | null>(null);

    const [formErrors, setFormErrors] = useState<Record<string, string>>({});
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const formatCUIT = (value: string) => {
        const digits = value.replace(/\D/g, '');
        let masked = digits;
        if (digits.length > 2) masked = `${digits.substring(0, 2)}-${digits.substring(2)}`;
        if (digits.length > 10) masked = `${masked.substring(0, 11)}-${digits.substring(10, 11)}`;
        return masked.substring(0, 13);
    };

    const handlePasswordChange = async (e: React.FormEvent) => {
        e.preventDefault();

        setPwError(null);
        setPwSuccess(null);

        if (passwordData.newPassword !== passwordData.confirmPassword) {
            setPwError("Las contraseñas no coinciden.");
            return;
        }

        if (passwordData.newPassword.length < 6) {
            setPwError("La nueva contraseña debe tener al menos 6 caracteres.");
            return;
        }

        try {
            setPwLoading(true);

            await api.post("/auth/change-password", {
                currentPassword: passwordData.currentPassword,
                newPassword: passwordData.newPassword,
                confirmPassword: passwordData.confirmPassword
            });

            setPwSuccess("Contraseña actualizada correctamente.");

            setPasswordData({
                currentPassword: "",
                newPassword: "",
                confirmPassword: ""
            });

        } catch (err: any) {
            setPwError(err.response?.data || "Error al cambiar la contraseña.");
        } finally {
            setPwLoading(false);
        }
    };

    const handlePasswordInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setPasswordData(prev => ({ ...prev, [name]: value }));
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        if (formErrors[name]) {
            setFormErrors(prev => {
                const next = { ...prev };
                delete next[name];
                return next;
            });
        }

        if (name === 'cuitUsuario') {
            setFormData({ ...formData, [name]: formatCUIT(value) });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const validateForm = () => {
        const newErrors: Record<string, string> = {};
        let isValid = true;

        // Validamos solo los campos visibles que pertenecen al formulario actual
        Object.keys(formData).forEach((key) => {
            const value = formData[key as keyof typeof formData];
            if (!value || value.trim() === '') {
                newErrors[key] = "Este campo es obligatorio";
                isValid = false;
            }
        });

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (formData.email && !emailRegex.test(formData.email)) {
            newErrors.email = "Formato de email no válido";
            isValid = false;
        }

        if (formData.password !== formData.confirmarPassword) {
            setError("Las contraseñas no coinciden");
            isValid = false;
        }

        setFormErrors(newErrors);
        return isValid;
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        if (!validateForm()) return;

        setIsLoading(true);

        // Construimos el payload acoplando los datos de la empresa actual de forma transparente
        const payload = {
            nombreUsuario: formData.nombreUsuario,
            nombre: formData.nombre,
            apellido: formData.apellido,
            email: formData.email,
            cuitUsuario: formData.cuitUsuario,
            password: formData.password,
            confirmarPassword: formData.confirmarPassword,
            cuitEmpresa: empresa.cuit,
            razonSocialEmpresa: empresa.razonSocial || ""
        };

        try {
            const response = await api.post('/auth/registerExtraRepresentanteEmpresa', payload);
            if (response.status === 200 || response.status === 201) {
                setSuccess("Nuevo representante registrado con éxito en la empresa.");
                setFormData({
                    nombreUsuario: '', nombre: '', apellido: '', email: '',
                    cuitUsuario: '', password: '', confirmarPassword: ''
                });
            }
        } catch (err: any) {
            setError(err.response?.data?.message || err.response?.data || "Hubo un error al registrar el usuario");
        } finally {
            setIsLoading(false);
        }
    };

    // Campos del formulario mapeados para mantener el renderizado limpio
    const fields = [
        { label: 'Nombre de Usuario', name: 'nombreUsuario', type: 'text' },
        { label: 'Nombre', name: 'nombre', type: 'text' },
        { label: 'Apellido', name: 'apellido', type: 'text' },
        { label: 'Email', name: 'email', type: 'email' },
        { label: 'CUIT de Usuario', name: 'cuitUsuario', type: 'text' },
        { label: 'Contraseña', name: 'password', type: 'password' },
        { label: 'Confirmar Contraseña', name: 'confirmarPassword', type: 'password' },
    ];

    return (
        <div className="configuracionPanel">
            <header className="panel-header panel-header-configuracion">
                <h2>Configuración del Sistema</h2>
                <p>Gestión interna de credenciales, accesos y parámetros de la organización.</p>
            </header>
            {/* SECCIÓN 2: ESPACIO RESERVADO PARA LA SEGUNDA FUNCIÓN */}
            <section className="password-section">
                <h3>Cambiar Contraseña</h3>
                <p>Actualice sus credenciales de acceso al sistema.</p>

                <form onSubmit={handlePasswordChange} className="password-form-grid">

                    <div className="form-group">
                        <label>Contraseña Actual</label>
                        <input
                            type="password"
                            name="currentPassword"
                            value={passwordData.currentPassword}
                            onChange={handlePasswordInput}
                            disabled={pwLoading}
                        />
                    </div>

                    <div className="form-group">
                        <label>Nueva Contraseña</label>
                        <input
                            type="password"
                            name="newPassword"
                            value={passwordData.newPassword}
                            onChange={handlePasswordInput}
                            disabled={pwLoading}
                        />
                    </div>

                    <div className="form-group">
                        <label>Confirmar Contraseña</label>
                        <input
                            type="password"
                            name="confirmPassword"
                            value={passwordData.confirmPassword}
                            onChange={handlePasswordInput}
                            disabled={pwLoading}
                        />
                    </div>

                    {pwError && <div className="alert-danger">{pwError}</div>}
                    {pwSuccess && <div className="alert-success">{pwSuccess}</div>}

                    <button type="submit" className="submit-settings-btn" disabled={pwLoading}>
                        {pwLoading ? "Actualizando..." : "Cambiar Contraseña"}
                    </button>
                </form>
            </section>


            <div className="configuracion-sections-grid">
                {/* SECCIÓN 1: ALTA DE NUEVOS REPRESENTANTES */}
                <section className="configuracion-card">
                    <div className="card-header">
                        <h3>Registrar Co-Administrador / Representante</h3>
                        <p>Completar el formulario para asignar a la empresa un representante adicional</p>
                    </div>

                    <form onSubmit={handleSubmit} noValidate className="configuracion-form">
                        <div className="form-grid-settings">
                            {fields.map((field) => (
                                <div className="form-group" key={field.name}>
                                    <label htmlFor={field.name}>{field.label}</label>
                                    <input
                                        id={field.name}
                                        className={formErrors[field.name] ? 'input-error' : ''}
                                        type={field.type}
                                        name={field.name}
                                        value={(formData as any)[field.name]}
                                        onChange={handleChange}
                                        disabled={isLoading}
                                        autoComplete="new-password"
                                    />
                                    {formErrors[field.name] && (
                                        <span className="error-text">{formErrors[field.name]}</span>
                                    )}
                                </div>
                            ))}
                        </div>

                        {error && <Alert type="error" message={error} />}
                        {success && <Alert type="success" message={success} />}

                        <button type="submit" className="submit-settings-btn" disabled={isLoading}>
                            {isLoading ? "Registrando..." : "Dar de Alta Representante"}
                        </button>
                    </form>
                </section>

            </div>
        </div>
    );
}