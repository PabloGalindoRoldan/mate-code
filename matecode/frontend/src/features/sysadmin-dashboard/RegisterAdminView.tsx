// RegisterAdminView.tsx
import React, { useState } from 'react';
import api from '../../api/axios';
import Alert from '../../ui/alert/Alert';
import '../auth/RegisterView.css'; // Reutiliza los estilos existentes
import { useNavigate } from 'react-router';

export default function RegisterAdminView() {
    const [formData, setFormData] = useState({
        nombreUsuario: '', nombre: '', apellido: '', email: '',
        cuitUsuario: '', password: '', confirmarPassword: ''
    });

    const [formErrors, setFormErrors] = useState<Record<string, string>>({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const formatCUIT = (value: string) => {
        const digits = value.replace(/\D/g, '');
        let masked = digits;
        if (digits.length > 2) masked = `${digits.substring(0, 2)}-${digits.substring(2)}`;
        if (digits.length > 10) masked = `${masked.substring(0, 11)}-${digits.substring(10, 11)}`;
        return masked.substring(0, 13);
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

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);

        if (!validateForm()) return;

        setIsLoading(true);
        try {
            const response = await api.post('/auth/registerAdminParque', formData);
            if (response.status === 200 || response.status === 201) {
                setIsSubmitted(true);
            }
        } catch (err: any) {
            setError(err.response?.data?.message || "Hubo un error en el registro");
        } finally {
            setIsLoading(false);
        }
    };

    const fields = [
        { label: 'Nombre de Usuario', name: 'nombreUsuario', type: 'text' },
        { label: 'Nombre', name: 'nombre', type: 'text' },
        { label: 'Apellido', name: 'apellido', type: 'text' },
        { label: 'Email', name: 'email', type: 'email' },
        { label: 'CUIT Usuario', name: 'cuitUsuario', type: 'text' },
        { label: 'Contraseña', name: 'password', type: 'password' },
        { label: 'Confirmar Contraseña', name: 'confirmarPassword', type: 'password' },
    ];

    return (
        <div className="reg-view-container">
            {isSubmitted ? (
                <div className="reg-success-card">
                    <Alert type="success" message="¡Registro exitoso! Su cuenta de Administrador ha sido creada correctamente." />
                    <div className="reg-success-actions">
                        <button className="reg-btn-action-primary" onClick={() => navigate('/login')}>Iniciar sesión</button>
                        <button className="reg-btn-action-secondary" onClick={() => navigate('/')}>Ir al Inicio</button>
                    </div>
                </div>
            ) : (
                <div className="reg-form-card">
                    <div className="reg-form-header">
                        <h2>Registro de Administrador</h2>
                        <p>Configure las credenciales de gestión para el Parque Industrial</p>
                    </div>

                    <form className="reg-form-element" onSubmit={handleSubmit} noValidate>
                        <div className="reg-form-stack">
                            {fields.map((field) => (
                                <div className="reg-form-group" key={field.name}>
                                    <label className="reg-form-label" htmlFor={field.name}>{field.label}</label>
                                    <input
                                        id={field.name}
                                        className={`reg-form-input ${formErrors[field.name] ? 'reg-input-has-error' : ''}`}
                                        type={field.type}
                                        name={field.name}
                                        value={(formData as any)[field.name]}
                                        onChange={handleChange}
                                        disabled={isLoading}
                                        autoComplete="off"
                                    />
                                    {formErrors[field.name] && (
                                        <span className="reg-error-text-span">{formErrors[field.name]}</span>
                                    )}
                                </div>
                            ))}
                        </div>

                        {error && (
                            <div className="reg-alert-wrapper">
                                <Alert type="error" message={error} />
                            </div>
                        )}

                        <button className="reg-submit-btn-form" type="submit" disabled={isLoading}>
                            {isLoading ? "Procesando..." : "Registrar Administrador"}
                        </button>
                    </form>
                </div>
            )}
        </div>
    );
}