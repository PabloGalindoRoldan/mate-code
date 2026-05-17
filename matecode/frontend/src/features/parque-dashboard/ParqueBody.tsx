import MensajeriaPanel from "../mensajeria/MensajeriaPanel";
import MapPanel from "./MapPanel";
import "./ParqueBody.css";
import PublicacionesPanel from "./PublicacionesPanel";


export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {

    const { isMenuOpen } = params;
    const { activeTab } = params;

    return (
        <>
            <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
                {activeTab === "map" && <MapPanel />}
                {activeTab === "messages" && <MensajeriaPanel />}
                {activeTab === "companies" && <p>Empresas - Esta sección está en construcción.</p>}
                {activeTab === "publications" && <PublicacionesPanel />}
                {activeTab === "reports" && <p>Reportes - Esta sección está en construcción.</p>}
                {activeTab === "settings" && <p>Configuración - Esta sección está en construcción.</p>}
            </div>
        </>
    )
}