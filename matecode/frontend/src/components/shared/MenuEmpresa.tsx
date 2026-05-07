import "./MenuEmpresa.css";

interface MenuProps {
    isOpen: boolean;
}

export default function MenuEmpresa({ isOpen }: MenuProps) {
    return (
        <aside className={`menu-sidebar ${isOpen ? "open" : "closed"}`}>
            <div className="menu-items">
                <button className="menu-item">Información</button>
                <button className="menu-item">Mensajes</button>
                <button className="menu-item">Cargar Consumos</button>
                <button className="menu-item">Configuración</button>
            </div>
        </aside>
    );
}