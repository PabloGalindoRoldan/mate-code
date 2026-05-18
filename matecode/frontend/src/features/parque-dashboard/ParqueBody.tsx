import MensajeriaPanel from "../mensajeria/MensajeriaPanel";
import MapPanel from "./MapPanel";
import PublicacionesPanel from "./PublicacionesPanel";
import ReportesPanel from "./ReportesPanel";
import "./ParqueBody.css";

export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {

    const { isMenuOpen, activeTab } = params;

    return (
        <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "map" && <MapPanel />}
            {activeTab === "messages" && <MensajeriaPanel />}
            {activeTab === "companies" && <p>Empresas - Esta sección está en construcción.</p>}
            {activeTab === "publications" && <PublicacionesPanel />}
            {activeTab === "reports" && <ReportesPanel />}
            {activeTab === "settings" && <p>Configuración - Esta sección está en construcción.</p>}
        </div>
    );
}