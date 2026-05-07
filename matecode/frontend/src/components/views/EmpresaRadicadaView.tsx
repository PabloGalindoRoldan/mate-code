import { useState } from "react";
import { Settings, ChevronLeft } from "lucide-react";
import EmpresaRadicadaBody from "../shared/EmpresaRadicadaBody";
import Footer from "../shared/Footer";
import NavBar from "../shared/NavBar";
import MenuEmpresa from "../shared/MenuEmpresa";
import "./EmpresaRadicadaView.css";
import data from "../../../tmp/empresaInfo.json";

export default function EmpresaRadicadaView() {
    const [empresaInfo] = useState(data);
    const [isMenuOpen, setIsMenuOpen] = useState(false); // Default closed for dashboard feel

    const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

    return (
        <div className={`empresaRadicadaView ${isMenuOpen ? "menu-open" : "menu-closed"}`}>
            <NavBar />
            {/* The Floating Toggle Button */}
            <button
                className="menu-toggle-btn"
                onClick={toggleMenu}
                aria-label="Toggle Menu"
            >
                {isMenuOpen ? <ChevronLeft size={20} /> : <Settings size={20} />}
            </button>

            <div className="main-layout">
                <MenuEmpresa isOpen={isMenuOpen} />
                <main className="content-area">
                    <EmpresaRadicadaBody empresaInfo={empresaInfo} isMenuOpen={isMenuOpen} />
                </main>
            </div>
            <Footer />
        </div>
    );
}