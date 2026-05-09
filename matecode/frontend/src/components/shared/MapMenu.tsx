import "./MapMenu.css";

interface MapMenuProps {
    isSatellite: boolean;
    setIsSatellite: (val: boolean) => void;
    rotationEnabled: boolean;
    setRotationEnabled: (val: boolean) => void;
    isOpen: boolean;
}

export default function MapMenu({
    isSatellite,
    setIsSatellite,
    rotationEnabled,
    setRotationEnabled,
    isOpen
}: MapMenuProps) {
    return (
        <aside className={`map-menu-sidebar ${isOpen ? "open" : "closed"}`}>

            <div className="menu-items">
                {/* Opción Rotación */}
                <div className="menu-option">
                    <span className="option-label">Auto Rotación</span>
                    <button
                        className={`switch-btn ${rotationEnabled ? "on" : "off"}`}
                        onClick={() => setRotationEnabled(!rotationEnabled)}
                    >
                        <span className="switch-slider"></span>
                    </button>
                </div>
                {/* Opción Satélite */}
                <div className="menu-option">
                    <span className="option-label">Vista Satélite</span>
                    <button
                        className={`switch-btn ${isSatellite ? "on" : "off"}`}
                        onClick={() => setIsSatellite(!isSatellite)}
                    >
                        <span className="switch-slider"></span>
                    </button>
                </div>
            </div>
        </aside>
    );
}