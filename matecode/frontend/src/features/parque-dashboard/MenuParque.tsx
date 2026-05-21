import "./MenuParque.css"

export default function MenuParque(params: { isOpen: boolean, setActiveTab: (tab: string) => void }) {
    const { isOpen, setActiveTab } = params;

    return (
        <>
            <aside className={`menu-sidebar ${isOpen ? "open" : "closed"}`}>
                <div className="menu-items">
                    <button className="menu-item" onClick={() => setActiveTab("map")}>
                        Mapa de Lotes
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("messages")}>
                        Mensajes
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("companies")}>
                        Empresas
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("publications")}>
                        Administrar Publicaciones
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("reports")}>
                        Generar Reportes
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("inventory")}>
                        Gestion de Inventario
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("budget")}>
                        Gestion de Presupuesto
                    </button>
                    <button className="menu-item" onClick={() => setActiveTab("settings")}>
                        Configuración
                    </button>
                </div>
            </aside>
        </>
    )
}