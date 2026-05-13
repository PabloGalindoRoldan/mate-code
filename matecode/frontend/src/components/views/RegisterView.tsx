import React, { useState } from 'react';
import axios from 'axios';
import NavBar from '../shared/NavBar';
import Footer from '../shared/Footer';
import './RegisterVierw.css';
import api from '../../api/axios';

export default function RegisterView() {
    const [formData, setFormData] = useState({
        nombreUsuario: '',
        nombre: '',
        apellido: '',
        email: '',
        cuitUsuario: '',
        razonSocialEmpresa: '',
        cuitEmpresa: '',
        password: '',
        confirmarPassword: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (formData.password !== formData.confirmarPassword) {
            alert("Las contraseñas no coinciden");
            return;
        }

        try {
            const response = await api.post('/login/registrarse', formData);
            if (response.status === 200 || response.status === 201) {
                alert("¡Registro exitoso!");
            }
        } catch (error: unknown) {
            if (axios.isAxiosError(error)) {
                if (error.response) {
                    console.error("Error del servidor:", error.response.data);
                    alert(`Error: ${error.response.status}`);
                } else {
                    console.error("Error de red:", error.message);
                }
            } else {
                console.error("Unexpected error:", error);
            }
        }
    };

    return (
        <div className="loginView">
            <NavBar />
            <div className="loginContainer">
                <form className="loginForm" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Nombre de Usuario</label>
                        <input type="text" name="nombreUsuario" value={formData.nombreUsuario} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Nombre</label>
                        <input type="text" name="nombre" value={formData.nombre} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Apellido</label>
                        <input type="text" name="apellido" value={formData.apellido} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Email</label>
                        <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>CUIT Usuario</label>
                        <input type="text" name="cuitUsuario" value={formData.cuitUsuario} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Razon Social de la Empresa</label>
                        <input type="text" name="razonSocialEmpresa" value={formData.razonSocialEmpresa} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>CUIT de la Empresa</label>
                        <input type="text" name="cuitEmpresa" value={formData.cuitEmpresa} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Contraseña</label>
                        <input type="password" name="password" value={formData.password} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Confirmar Contraseña</label>
                        <input type="password" name="confirmarPassword" value={formData.confirmarPassword} onChange={handleChange} required />
                    </div>
                    <button type="submit">Registrarse</button>
                </form>
            </div>
            <Footer />
        </div>
    );
}