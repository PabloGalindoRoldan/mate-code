import NavBar from "../shared/NavBar";
import Footer from "../shared/Footer";
import ParqueBody from "../shared/ParqueBody"
import MenuParque from "../shared/MenuParque";
import { useState } from "react";
import { Settings, ChevronLeft } from "lucide-react";
import data from "../../../tmp/empresaInfo.json";
import "./AdministradorParqueView.css";
import "./EmpresaRadicadaView.css";
import { MapProvider } from "../shared/MapProvider";


export default function AdministradorParqueView() {

    const [empresaInfo] = useState(data);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const toggleMenu = () => setIsMenuOpen(!isMenuOpen);



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
                    <MenuParque isOpen={isMenuOpen} />
                    <main className="content-area">
                        <ParqueBody empresaInfo={empresaInfo} isMenuOpen={isMenuOpen} />
                    </main>
                </div>
                <Footer />
            </div>
        </MapProvider>
    )
}