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

    const [isSubmitted, setIsSubmitted] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        if (name === 'cuitUsuario' || name === 'cuitEmpresa') {
            const maskedValue = formatCUIT(value);
            setFormData({ ...formData, [name]: maskedValue });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);

        if (formData.password !== formData.confirmarPassword) {
            setError("Las contraseñas no coinciden");
            return;
        }

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

    // Helper for clean input rendering
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

    //helper to format CUIT
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

    return (
        <div className="loginView">
            <NavBar />
            <div className="loginContainer">
                {isSubmitted ? (
                    /* Success State: Replaces the form */
                    <div className="successCard">
                        <Alert type="success" message="¡Registro exitoso! Ya podés iniciar sesión." />
                        <div className="botonera">
                            <button
                                className="botonBotonera"
                                onClick={() => navigate('/login')}
                            >
                                Iniciar sesion
                            </button>
                            <button
                                className="botonBotonera"
                                onClick={() => navigate('/')}
                            >
                                Home
                            </button>
                        </div>
                    </div>
                ) : (
                    /* Form State: Form is visible, errors appear inside */
                    <div className="form-wrapper">

                        <form className="loginForm" onSubmit={handleSubmit}>
                            {/* Render error alert here if it exists */}
                            {error && <Alert type="error" message={error} />}
                            {fields.map((field) => (
                                <div className="form-group" key={field.name}>
                                    <label htmlFor={field.name}>{field.label}</label>
                                    <input
                                        id={field.name}
                                        type={field.type}
                                        name={field.name}
                                        value={(formData as any)[field.name]}
                                        onChange={handleChange}
                                        disabled={isLoading}
                                        required
                                    />
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