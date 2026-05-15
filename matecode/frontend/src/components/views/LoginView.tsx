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
            navigate('/empresa-radicada');
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
                            disabled={isSubmitting} // Good practice to disable during fetch
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