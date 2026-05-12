import NavBar from '../shared/NavBar';
import Footer from '../shared/Footer';
import './RegisterVierw.css';

export default function RegisterView() {
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
                        <label htmlFor="cuitUsuario">CUIT Usuario</label>
                        <input type="text" id="cuitUsuario" name="cuitUsuario" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="companyName">Razon Social de la Empresa</label>
                        <input type="text" id="companyName" name="companyName" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="cuitEmpresa">CUIT de la Empresa</label>
                        <input type="text" id="cuitEmpresa" name="cuitEmpresa" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Contraseña</label>
                        <input type="password" id="password" name="password" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="confirmPassword">Confirmar Contraseña</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" />
                    </div>
                    <button type="submit">Ingresar</button>
                </form>
            </div>
            <Footer />
        </div>
    );
}