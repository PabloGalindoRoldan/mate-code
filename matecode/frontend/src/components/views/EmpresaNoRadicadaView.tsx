import "./EmpresaNoRadicadaView.css";
import NavBar from "../shared/NavBar";
import Footer from "../shared/Footer";

export default function EmpresaNoRadicadaView() {
    return (

        <div className="empresaNoRadicadaView">
            <NavBar />
            <div className="empresaNoRadicadaBody">
                <h1>Presentar Proyecto Preliminar</h1>
                <p>Ingresar todos los datos solicitados</p>
                <form className="project-form">
                    <label htmlFor="nombre" className="form-label">Nombre del proyecto:</label>
                    <input type="text" id="nombre" name="nombre" className="form-input" required />
                    <label htmlFor="descripcion" className="form-label">Descripción del proyecto:</label>
                    <textarea id="descripcion" name="descripcion" className="form-textarea" required></textarea>
                    <label htmlFor="actividadPrincipal" className="form-label">Actividad Principal:</label>
                    <input type="text" id="actividadPrincipal" name="actividadPrincipal" className="form-input" required />
                    <label htmlFor="superficieRequerida" className="form-label">Superficie Requerida (mt2):</label>
                    <input type="number" id="superficieRequerida" name="superficieRequerida" className="form-input" required />
                    <label htmlFor="energiaRequerida" className="form-label">Energía Requerida (KWh):</label>
                    <input type="number" id="energiaRequerida" name="energiaRequerida" className="form-input" required />
                    <label htmlFor="personalAOcupar" className="form-label">Personal a Ocupar:</label>
                    <input type="number" id="personalAOcupar" name="personalAOcupar" className="form-input" required />
                </form>
            </div>
            <Footer />
        </div>
    );
}