import { Satellite, RotateCw, RotateCcw } from "lucide-react";
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
                <h3 className="menu-title">Vista del Mapa</h3>

                <button
                    className={`menu-item ${isSatellite ? "active" : ""}`}
                    onClick={() => setIsSatellite(!isSatellite)}
                >
                    <Satellite size={18} />
                    {isSatellite ? "Quitar Satélite" : "Ver Satélite"}
                </button>

                <button
                    className={`menu-item ${rotationEnabled ? "active" : ""}`}
                    onClick={() => setRotationEnabled(!rotationEnabled)}
                >
                    {rotationEnabled ? <RotateCcw size={18} /> : <RotateCw size={18} />}
                    {rotationEnabled ? "Pausar Giro" : "Activar Giro"}
                </button>
            </div>
        </aside>
    );
}