import Footer from "../shared/Footer"
import NavBar from "../shared/NavBar"
import "./ContactoView.css"

export default function ContactoView() {
    return (
        <>
            <NavBar />
            <div className="contactoSection">
                <h2 className="contactoTitle">Contacto</h2>
                <div className="contactoContent">
                    <p className="contactoItem"><strong>Tel:</strong> 02920 - 29202920</p>
                    <p className="contactoItem"><strong>Email:</strong> info@info.com</p>
                    <p className="contactoItem"><strong>Dirección:</strong> Av. Siempre Viva 123, Viedma</p>
                </div>
            </div>
            <Footer />
        </>
    )
}