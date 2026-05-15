import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useAuth } from '../../context/AuthContext';
import Footer from '../shared/Footer';
import NavBar from '../shared/NavBar';
import './LoginView.css';

export default function LoginView() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: SubmitEvent) => {
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
            // Since context stored it in sessionStorage, we read it to grab the role instantly
            const storedUserRaw = sessionStorage.getItem('user');
            if (storedUserRaw) {
                const userDetails = JSON.parse(storedUserRaw);
                // Route dynamically depending on the user's role
                switch (userDetails.rol) {
                    case 'ADMINISTRADOR_SISTEMA':
                        navigate('/admin');
                        break;
                    case 'ADMINISTRADOR_PARQUE':
                        navigate('/parque');
                        break;
                    case 'REPRESENTANTE_EMPRESA':
                        navigate('/empresa-radicada');
                        break;
                    default:
                        navigate('/'); // Global fallback
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
        <div className="loginView">
            <NavBar />
            <div className="loginContainer">
                <form className="loginForm" onSubmit={handleSubmit as any}>
                    <div className="form-group">
                        <label htmlFor="username">Nombre de Usuario</label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            disabled={isSubmitting} // Good practice to disable during fetch
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
                        />
                    </div>
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Ingresando...' : 'Ingresar'}
                    </button>
                    {errorMsg && (
                        <div className="form-error-message">
                            {errorMsg}
                        </div>
                    )}
                </form>
            </div>
            <Footer />
        </div>
    );
}