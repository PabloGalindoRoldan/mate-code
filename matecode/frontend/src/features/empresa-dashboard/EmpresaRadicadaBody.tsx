import "./EmpresaRadicadaBody.css";
import InfoPanel from "./InfoPanel";
import CargarConsumoPanel from "./CargarConsumoPanel";
import MensajeriaPanel from "../mensajeria/MensajeriaPanel";
import ConfiguracionPanel from "./ConfiguracionPanel";


export default function EmpresaRadicadaBody(params: { empresa: any, isMenuOpen: boolean, activeTab: string }) {
    const { isMenuOpen } = params;
    const { empresa } = params;
    const { activeTab } = params;

    return (
        <div className={`empresaRadicadaBody ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "info" && <InfoPanel empresa={empresa} />}
            {activeTab === "consumos" && <CargarConsumoPanel />}
            {activeTab === "messages" && <MensajeriaPanel />}
            {activeTab === "settings" && <ConfiguracionPanel empresa={empresa} />}
        </div>
    );
}