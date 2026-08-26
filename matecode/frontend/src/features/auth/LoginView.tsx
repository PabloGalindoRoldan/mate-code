// LoginView.tsx
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useAuth } from '../../context/AuthContext';
import NavBar from '../../ui/navBar/NavBar';
import './LoginView.css';

export default function LoginView() {
    const [username, setUsername] = useState('adminparque');
    const [password, setPassword] = useState('Admin123');
    const [errorMsg, setErrorMsg] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setErrorMsg('');

        if (!username.trim() || !password.trim()) {
            setErrorMsg('Por favor, complete todos los campos.');
            return;
        }

        setIsSubmitting(true);
        const result = await login(username, password);
        setIsSubmitting(false);

        if (result.success) {
            const storedUserRaw = sessionStorage.getItem('user');
            if (storedUserRaw) {
                const userDetails = JSON.parse(storedUserRaw);
                switch (userDetails.rol) {
                    case 'ADMINISTRADOR_SISTEMA':
                        navigate('/admin');
                        break;
                    case 'ADMINISTRADOR_PARQUE':
                        navigate('/parque');
                        break;
                    case 'REPRESENTANTE_EMPRESA':
                        if (userDetails.empresa.esRadicada) {
                            navigate('/empresa-radicada');
                        } else {
                            navigate('/nueva-empresa');
                        }
                        break;
                    default:
                        navigate('/');
                        break;
                }
            } else {
                navigate('/');
            }
        } else {
            setErrorMsg(result.message || 'Ocurrió un error inesperado.');
        }
    };

    return (
        <div className="loginViewWrapper">
            {/* Sincronizado con la barra transparente del landing */}
            <NavBar variant="transparent" />

            <div className="loginContainer">
                <form className="loginForm" onSubmit={handleSubmit}>
                    <div className="loginHeader">
                        <h2>Iniciar Sesión</h2>
                        <p>Ingresá tus credenciales para acceder al sistema</p>
                    </div>

                    <div className="form-group">
                        <label htmlFor="username">Nombre de Usuario</label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            disabled={isSubmitting}
                            placeholder="Tu nombre de usuario"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Contraseña</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            disabled={isSubmitting}
                            placeholder="••••••••"
                        />
                    </div>

                    <button className="loginSubmitButton" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Ingresando...' : 'Ingresar al Portal'}
                    </button>

                    {errorMsg && (
                        <div className="form-error-message">
                            {errorMsg}
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
}