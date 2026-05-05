import Footer from '../shared/Footer';
import NavBar from '../shared/NavBar';
import './LoginView.css';

export default function LoginView() {
    return (
        <div className="loginView">
            <NavBar />
            <div className="loginContainer">
                <form className="loginForm">
                    <div className="form-group">
                        <label htmlFor="username">Nombre de Usuario</label>
                        <input type="text" id="username" name="username" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Contraseña</label>
                        <input type="password" id="password" name="password" />
                    </div>
                    <button type="submit">Ingresar</button>
                </form>
            </div>
            <Footer />
        </div>
    );
}