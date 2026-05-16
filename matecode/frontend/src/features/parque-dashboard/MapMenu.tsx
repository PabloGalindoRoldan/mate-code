import "./MapMenu.css";

interface MapMenuProps {
    isSatellite: boolean;
    setIsSatellite: (val: boolean) => void;
    rotationEnabled: boolean;
    setRotationEnabled: (val: boolean) => void;
    isOpen: boolean;
    showNuevo: boolean;
    setShowNuevo: (val: boolean) => void;
    showViejo: boolean;
    setShowViejo: (val: boolean) => void;
    showStreets: boolean;
    setShowStreets: (val: boolean) => void;
    showDisponible: boolean;
    setShowDisponible: (val: boolean) => void;
    showOcupado: boolean;
    setShowOcupado: (val: boolean) => void;
}

export default function MapMenu({
    isSatellite, setIsSatellite,
    rotationEnabled, setRotationEnabled,
    isOpen,
    showNuevo, setShowNuevo,
    showViejo, setShowViejo,
    showStreets, setShowStreets,
    showOcupado, setShowOcupado
}: MapMenuProps) {

    const ToggleRow = (label: string, value: boolean, setter: (v: boolean) => void) => (
        <div className="menu-option">
            <span className="option-label">{label}</span>
            <button
                className={`switch-btn ${value ? "on" : "off"}`}
                onClick={() => setter(!value)}
            >
                <span className="switch-slider"></span>
            </button>
        </div>
    );

    return (
        <aside className={`map-menu-sidebar ${isOpen ? "open" : "closed"}`}>
            <div className="menu-items">
                {ToggleRow("Auto Rotación", rotationEnabled, setRotationEnabled)}
                {ToggleRow("Vista Satélite", isSatellite, setIsSatellite)}
                <hr style={{ opacity: 0.1, margin: "10px 0" }} />
                {ToggleRow("Nuevo", showNuevo, setShowNuevo)}
                {ToggleRow("Viejo", showViejo, setShowViejo)}
                {ToggleRow("Calles", showStreets, setShowStreets)}
                {ToggleRow("Disponibilidad", showOcupado, setShowOcupado)}
            </div>
        </aside>
    );
}