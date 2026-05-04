import "./LandingBody.css";


export default function LandingBody() {
    return (
        <section className="landingBodySection" id="center">
            <h1 className="tituloLanding">ENREPAVI</h1>
            <h2 className="subtituloLanding">Sistema de Gestion</h2>
            <div className="botoneraLanding">
                <button className="buttonIngresarLanding">Ingresar</button>
                <button className="buttonRegistrarseLanding">Registrarse</button>
            </div>
        </section>
    )
}