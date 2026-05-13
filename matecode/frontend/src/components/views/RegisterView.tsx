import React, { useState } from 'react';
import NavBar from '../shared/NavBar';
import Footer from '../shared/Footer';
import api from '../../api/axios';
import Alert from '../shared/Alert';
import './RegisterVierw.css';
import { useNavigate } from 'react-router';

export default function RegisterView() {
    const [formData, setFormData] = useState({
        nombreUsuario: '', nombre: '', apellido: '', email: '',
        cuitUsuario: '', razonSocialEmpresa: '', cuitEmpresa: '',
        password: '', confirmarPassword: ''
    });

    // Track which fields are missing data
    const [formErrors, setFormErrors] = useState<Record<string, boolean>>({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const formatCUIT = (value: string) => {
        const digits = value.replace(/\D/g, '');
        let masked = digits;
        if (digits.length > 2) {
            masked = `${digits.substring(0, 2)}-${digits.substring(2)}`;
        }
        if (digits.length > 10) {
            masked = `${masked.substring(0, 11)}-${digits.substring(10, 11)}`;
        }
        return masked.substring(0, 13);
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        // Clear error for this field as user types
        if (formErrors[name]) {
            setFormErrors(prev => ({ ...prev, [name]: false }));
        }

        if (name === 'cuitUsuario' || name === 'cuitEmpresa') {
            setFormData({ ...formData, [name]: formatCUIT(value) });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const validateForm = () => {
        const newErrors: Record<string, boolean> = {};
        let isValid = true;

        // Check required fields
        Object.keys(formData).forEach((key) => {
            const value = formData[key as keyof typeof formData];
            if (!value || value.trim() === '') {
                newErrors[key] = true;
                isValid = false;
            }
        });

        // Specific Password Check
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

        if (!validateForm()) return;

        setIsLoading(true);
        try {
            const response = await api.post('/login/registrarse', formData);
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
        { label: 'Razon Social de la Empresa', name: 'razonSocialEmpresa', type: 'text' },
        { label: 'CUIT de la Empresa', name: 'cuitEmpresa', type: 'text' },
        { label: 'Contraseña', name: 'password', type: 'password' },
        { label: 'Confirmar Contraseña', name: 'confirmarPassword', type: 'password' },
    ];

    return (
        <div className="loginView">
            <NavBar />
            <div className="loginContainer">
                {isSubmitted ? (
                    <div className="successCard">
                        <Alert type="success" message="¡Registro exitoso! Ya podés iniciar sesión." />
                        <div className="botonera">
                            <button className="botonBotonera" onClick={() => navigate('/login')}>
                                Iniciar sesión
                            </button>
                            <button className="botonBotonera" onClick={() => navigate('/')}>
                                Home
                            </button>
                        </div>
                    </div>
                ) : (
                    <div className="form-wrapper">
                        <form className="loginForm" onSubmit={handleSubmit} noValidate>
                            {error && <Alert type="error" message={error} />}

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
                                        autoComplete="off"
                                    />
                                    {formErrors[field.name] && (
                                        <span className="error-text">Completar este campo</span>
                                    )}
                                </div>
                            ))}

                            <button type="submit" disabled={isLoading}>
                                {isLoading ? "Procesando..." : "Registrarse"}
                            </button>
                        </form>
                    </div>
                )}
            </div>
            <Footer />
        </div>
    );
}