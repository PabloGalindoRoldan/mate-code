import "./MenuEmpresa.css";

interface MenuProps {
    isOpen: boolean;
    activeTab: string;
    setActiveTab: (tab: string) => void;
}

export default function MenuEmpresa({ isOpen, activeTab, setActiveTab }: MenuProps) {
    const menuItems = [
        { id: "info", label: "Información" },
        { id: "messages", label: "Mensajes" },
        { id: "consumos", label: "Consumos" },
        { id: "settings", label: "Configuración" },
    ];

    return (
        <aside className={`menu-sidebar ${isOpen ? "open" : "closed"}`}>
            <div className="menu-items">
                {menuItems.map((item) => (
                    <button
                        key={item.id}
                        className={`menu-item ${activeTab === item.id ? "active" : ""}`}
                        onClick={() => setActiveTab(item.id)}
                    >
                        {item.label}
                    </button>
                ))}
            </div>
        </aside>
    );
}
