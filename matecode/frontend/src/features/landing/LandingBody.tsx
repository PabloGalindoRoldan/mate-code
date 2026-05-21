import { useState, useEffect } from "react";
import "./LandingBody.css";
import { useNavigate } from "react-router";
import { ArrowRight, ChevronDown } from "lucide-react";

export default function LandingBody() {
    const navigate = useNavigate();
    const [opacity, setOpacity] = useState(1);

    useEffect(() => {
        const handleScroll = () => {
            const currentScroll = window.scrollY;
            const heroHeight = window.innerHeight;
            const newOpacity = Math.max(0, 1 - (currentScroll / (heroHeight / 2.5)));
            setOpacity(newOpacity);
        };

        window.addEventListener("scroll", handleScroll);

        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    const handleScrollDown = () => {
        window.scrollTo({
            top: window.innerHeight,
            behavior: "smooth"
        });
    };

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
                    Ingresar al Portal <ArrowRight size={"16"} />
                </button>
                <button className="buttonRegistrarseLanding" onClick={() => navigate("/register")}>
                    Solicitar Registro
                </button>
            </div>

            <div
                className={`scrollIndicatorLanding ${opacity === 0 ? 'hidden' : ''}`}
                style={{ opacity: opacity }}
                onClick={handleScrollDown}
                aria-label="Scrollear hacia abajo"
            >
                <ChevronDown size={"40"} color="var(--verde1)" />
            </div>
        </section>
    );
}