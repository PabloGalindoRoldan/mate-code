import NavBar from "../../ui/navBar/NavBar";
import Footer from "../../ui/footer/Footer";
import ParqueBody from "./ParqueBody"
import MenuParque from "./MenuParque";
import { useState } from "react";
import { Settings, ChevronLeft } from "lucide-react";
import data from "../../../tmp/empresaInfo.json";
import "./AdministradorParqueView.css";
import "../empresa-dashboard/EmpresaRadicadaView.css";
import { MapProvider } from "./MapProvider";


export default function AdministradorParqueView() {

    const [empresaInfo] = useState(data);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const toggleMenu = () => setIsMenuOpen(!isMenuOpen);
    const [activeTab, setActiveTab] = useState("map");



    return (
        <MapProvider>
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
                    <MenuParque isOpen={isMenuOpen} setActiveTab={setActiveTab} />
                    <main className="content-area">
                        <ParqueBody empresaInfo={empresaInfo} isMenuOpen={isMenuOpen} activeTab={activeTab} />
                    </main>
                </div>
                <Footer />
            </div>
        </MapProvider>
    )
}