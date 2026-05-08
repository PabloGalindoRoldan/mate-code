import "./MenuParque.css"

export default function MenuParque(params: { isOpen: boolean }) {
    const { isOpen } = params;

    return (
        <>
            <aside className={`menu-sidebar ${isOpen ? "open" : "closed"}`}>
                <div className="menu-items">
                    <button className="menu-item">Mapa de Lotes</button>
                    <button className="menu-item">Mensajes</button>
                    <button className="menu-item">Empresas</button>
                    <button className="menu-item">Otros</button>
                    <button className="menu-item">Reportes</button>
                    <button className="menu-item">Configuración</button>
                </div>
            </aside>
        </>
    )
}