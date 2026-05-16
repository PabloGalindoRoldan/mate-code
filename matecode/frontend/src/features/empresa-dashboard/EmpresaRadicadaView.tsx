import { useState } from "react";
import { Settings, ChevronLeft } from "lucide-react";
import EmpresaRadicadaBody from "./EmpresaRadicadaBody";
import Footer from "../../ui/footer/Footer";
import NavBar from "../../ui/navBar/NavBar";
import MenuEmpresa from "./MenuEmpresa";
import "./EmpresaRadicadaView.css";
import data from "../../../tmp/empresaInfo.json";

export default function EmpresaRadicadaView() {
    const [empresaInfo] = useState(data);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [activeTab, setActiveTab] = useState("info");

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
                <MenuEmpresa isOpen={isMenuOpen} setActiveTab={setActiveTab} activeTab={activeTab} />
                <main className="content-area">
                    <EmpresaRadicadaBody empresaInfo={empresaInfo} isMenuOpen={isMenuOpen} activeTab={activeTab} />
                </main>
            </div>
            <Footer />
        </div>
    );
}