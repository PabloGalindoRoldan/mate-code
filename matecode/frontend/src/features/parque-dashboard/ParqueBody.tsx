import MensajeriaPanel from "../mensajeria/MensajeriaPanel";
import MapPanel from "./MapPanel";
import PublicacionesPanel from "./PublicacionesPanel";
import ReportesPanel from "./ReportesPanel";
import "./ParqueBody.css";
import InventarioPanel from "./InventarioPanel";
import PresupuestoPanel from "./PresupuestoPanel";
import EmpresasPanel from "./EmpresasPanel";
import SettingsPanel from "./SettingsPanel";


export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {

    const { isMenuOpen, activeTab } = params;

    return (
        <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "map" && <MapPanel />}
            {activeTab === "messages" && <MensajeriaPanel />}
            {activeTab === "companies" && <EmpresasPanel />}
            {activeTab === "publications" && <PublicacionesPanel />}
            {activeTab === "reports" && <ReportesPanel />}
            {activeTab === "inventory" && <InventarioPanel />}
            {activeTab === "budget" && <PresupuestoPanel />}
            {activeTab === "settings" && <SettingsPanel />}
        </div>
    );
}