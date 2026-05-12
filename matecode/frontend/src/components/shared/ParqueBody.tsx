import MapPanel from "./MapPanel";
import "./ParqueBody.css";


export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean, activeTab: string }) {

    const { isMenuOpen } = params;
    const { activeTab } = params;

    return (
        <>
            <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
                {activeTab === "map" && <MapPanel />}
                {activeTab === "messages" && <p>Mensajes - Esta sección está en construcción.</p>}
                {activeTab === "companies" && <p>Empresas - Esta sección está en construcción.</p>}
                {activeTab === "others" && <p>Otros - Esta sección está en construcción.</p>}
                {activeTab === "reports" && <p>Reportes - Esta sección está en construcción.</p>}
                {activeTab === "settings" && <p>Configuración - Esta sección está en construcción.</p>}
            </div>
        </>
    )
}