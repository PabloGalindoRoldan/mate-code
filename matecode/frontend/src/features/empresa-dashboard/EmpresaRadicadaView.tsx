import { useState, useEffect } from "react";
import { Settings, ChevronLeft } from "lucide-react";
import EmpresaRadicadaBody from "./EmpresaRadicadaBody";
import Footer from "../../ui/footer/Footer";
import NavBar from "../../ui/navBar/NavBar";
import MenuEmpresa from "./MenuEmpresa";
import "./EmpresaRadicadaView.css";

export default function EmpresaRadicadaView() {
    const [empresa, setEmpresa] = useState<any>(null);
    const [usuario, setUsuario] = useState<any>(null);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [activeTab, setActiveTab] = useState("info");

    useEffect(() => {
        // Recuperamos el string del sessionStorage usando la clave "user"
        const sessionUser = sessionStorage.getItem("user");
        if (sessionUser) {
            try {
                const parsedUser = JSON.parse(sessionUser);

                if (parsedUser) {
                    // Guardamos el usuario completo (contiene nombre, apellido, cuit, nombreUsuario, etc.)
                    setUsuario(parsedUser);

                    // Extraemos y guardamos la empresa anidada
                    if (parsedUser.empresa) {
                        setEmpresa(parsedUser.empresa);
                    }
                }
            } catch (err) {
                console.error("Error al parsear el usuario del sessionStorage", err);
            }
        }
    }, []);

    const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

    return (
        <div className={`empresaRadicadaView ${isMenuOpen ? "menu-open" : "menu-closed"}`}>
            <NavBar />

            <button
                className="menu-toggle-btn"
                onClick={toggleMenu}
                aria-label="Toggle Menu"
            >
                {isMenuOpen ? <ChevronLeft size={20} /> : <Settings size={20} />}
            </button>

            <div className="main-layout">
                <MenuEmpresa isOpen={isMenuOpen} setActiveTab={setActiveTab} activeTab={activeTab} />
                <main className="content-area">
                    <EmpresaRadicadaBody
                        empresa={empresa}
                        isMenuOpen={isMenuOpen}
                        activeTab={activeTab}
                        usuario={usuario}
                    />
                </main>
            </div>
            <Footer />
        </div>
    );
}