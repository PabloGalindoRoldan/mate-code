import "./EmpresaRadicadaBody.css";
import InfoPanel from "./InfoPanel";
import StatisticsPanel from "./StatisticsPanel";
import CargarConsumoPanel from "./CargarConsumoPanel";
import MensajeriaPanel from "../mensajeria/MensajeriaPanel";


export default function EmpresaRadicadaBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {
    const { isMenuOpen } = params;
    const { empresaInfo } = params;
    const { activeTab } = params;

    return (
        <div className={`empresaRadicadaBody ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "info" && <InfoPanel empresaInfo={empresaInfo} />}
            {activeTab === "info" && <StatisticsPanel empresaInfo={empresaInfo} />}
            {activeTab === "consumos" && <CargarConsumoPanel />}
            {activeTab === "messages" && <MensajeriaPanel />}
            {activeTab === "settings" && <p>Configuración - Esta sección está en construcción.</p>}
        </div>
    );
}