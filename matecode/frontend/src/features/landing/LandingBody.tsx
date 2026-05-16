import "./LandingBody.css";
import { useNavigate } from "react-router";


export default function LandingBody() {
    const navigate = useNavigate();

    return (
        <section className="landingBodySection" id="center">
            <h1 className="tituloLanding">ENREPAVI</h1>
            <h2 className="subtituloLanding">Sistema de Gestión</h2>
            <div className="botoneraLanding">
                <button className="buttonIngresarLanding" onClick={() => navigate("/login")}>Ingresar</button>
                <button className="buttonRegistrarseLanding" onClick={() => navigate("/register")}>Registrarse</button>
            </div>
        </section>
    )
}