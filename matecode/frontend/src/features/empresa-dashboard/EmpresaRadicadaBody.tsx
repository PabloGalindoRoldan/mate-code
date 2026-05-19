import "./EmpresaRadicadaBody.css";
import InfoPanel from "./InfoPanel";
import CargarConsumoPanel from "./CargarConsumoPanel";
import MensajeriaPanel from "../mensajeria/MensajeriaPanel";
import ConfiguracionPanel from "./ConfiguracionPanel";

interface EmpresaRadicadaBodyProps {
    empresa: any;
    isMenuOpen: boolean;
    activeTab: string;
    usuario: any;
}

export default function EmpresaRadicadaBody({
    empresa,
    isMenuOpen,
    activeTab,
    usuario
}: EmpresaRadicadaBodyProps) {

    return (
        <div className={`empresaRadicadaBody ${isMenuOpen ? "shrunk" : "full"}`}>
            {activeTab === "info" && <InfoPanel empresa={empresa} usuario={usuario} />}
            {activeTab === "consumos" && <CargarConsumoPanel />}
            {activeTab === "messages" && <MensajeriaPanel />}
            {activeTab === "settings" && <ConfiguracionPanel empresa={empresa} />}
        </div>
    );
}