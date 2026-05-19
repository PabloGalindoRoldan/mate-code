// LandingBody.tsx
import "./LandingBody.css";
import { useNavigate } from "react-router";
import { ArrowRight } from "lucide-react";

export default function LandingBody() {
    const navigate = useNavigate();

    return (
        <section className="landingBodySection">
            <div className="heroTextContent">
                <h1 className="tituloLanding">ENREPAVI</h1>
                <h2 className="subtituloLanding">Sistema de Gestión Digital</h2>
                <p className="descripcionLanding">
                    Plataforma de administración y servicios para las empresas radicadas
                    en el Parque Industrial. Optimiza tus consumos, gestiona tus trámites y mantente informado.
                </p>
            </div>
            <div className="botoneraLanding">
                <button className="buttonIngresarLanding" onClick={() => navigate("/login")}>
                    Ingresar al Portal <ArrowRight size={16} />
                </button>
                <button className="buttonRegistrarseLanding" onClick={() => navigate("/register")}>
                    Solicitar Registro
                </button>
            </div>
        </section>
    );
}