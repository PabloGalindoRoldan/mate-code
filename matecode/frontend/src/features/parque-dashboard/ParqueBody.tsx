import MensajeriaPanel from "../mensajeria/MensajeriaPanel";
import MapPanel from "./MapPanel";
import PublicacionesPanel from "./PublicacionesPanel";
import ReportesPanel from "./ReportesPanel";
import "./ParqueBody.css";
import LoadingSpinner from "../../ui/loading/LoadingSpinner";
import InventarioPanel from "./InventarioPanel";
import PresupuestoPanel from "./PresupuestoPanel";

export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {

    const { isMenuOpen, activeTab } = params;

    return (
        <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "map" && <MapPanel />}
            {activeTab === "messages" && <MensajeriaPanel />}
            {activeTab === "companies" && <LoadingSpinner text="Empresas - Esta sección está en construcción." />}
            {activeTab === "publications" && <PublicacionesPanel />}
            {activeTab === "reports" && <ReportesPanel />}
            {activeTab === "inventory" && <InventarioPanel />}
            {activeTab === "budget" && <PresupuestoPanel />}
            {activeTab === "settings" && <LoadingSpinner text="Configuración - Esta sección está en construcción." />}
        </div>
    );
}