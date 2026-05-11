import { Car } from "lucide-react";
import "./EmpresaRadicadaBody.css";
import InfoPanel from "./InfoPanel";
import StatisticsPanel from "./StatisticsPanel";
import CargarConsumoPanel from "./CargarConsumoPanel";


export default function EmpresaRadicadaBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {
    const { isMenuOpen } = params;
    const { empresaInfo } = params;
    const { activeTab } = params;

    return (
        <div className={`empresaRadicadaBody ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "info" && <InfoPanel empresaInfo={empresaInfo} />}
            {activeTab === "info" && <StatisticsPanel empresaInfo={empresaInfo} />}
            {activeTab === "consumos" && <CargarConsumoPanel />}
            {activeTab === "messages" && <p>Mensajes - Esta sección está en construcción.</p>}
            {activeTab === "settings" && <p>Configuración - Esta sección está en construcción.</p>}
        </div>
    );
}