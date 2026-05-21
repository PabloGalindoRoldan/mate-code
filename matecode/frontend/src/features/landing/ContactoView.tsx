// ContactoView.tsx
import NavBar from "../../ui/navBar/NavBar";
import "./ContactoView.css";

export default function ContactoView() {
    return (
        <div className="contactoViewWrapper">
            {/* Navbar transparente consistente con el Landing y Login */}
            <NavBar variant="transparent" />

            <div className="contactoContainer">
                <div className="contactoCard">
                    <div className="contactoHeader">
                        <h2 className="contactoTitle">Contacto</h2>
                        <p className="contactoSubtitle">Estamos a tu disposición para resolver cualquier duda o consulta</p>
                    </div>

                    <div className="contactoContent">
                        <div className="contactoRow">
                            <span className="contactoLabel">Teléfono</span>
                            <a href="tel:02920292029" className="contactoValue">02920 - 29202920</a>
                        </div>

                        <div className="contactoRow">
                            <span className="contactoLabel">Correo Electrónico</span>
                            <a href="mailto:info@info.com" className="contactoValue">info@info.com</a>
                        </div>

                        <div className="contactoRow">
                            <span className="contactoLabel">Dirección Presencial</span>
                            <span className="contactoValue">Av. Siempre Viva 123, Viedma</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}