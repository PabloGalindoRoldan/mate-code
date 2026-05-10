import { createContext, useContext, useState, type ReactNode } from "react";

interface MapContextType {
    isSatellite: boolean;
    setIsSatellite: (val: boolean) => void;
    rotationEnabled: boolean;
    setRotationEnabled: (val: boolean) => void;
    isMapMenuOpen: boolean;
    setIsMapMenuOpen: (val: boolean) => void;
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

const MapContext = createContext<MapContextType | undefined>(undefined);

export const MapProvider = ({ children }: { children: ReactNode }) => {
    const [isSatellite, setIsSatellite] = useState(true);
    const [rotationEnabled, setRotationEnabled] = useState(true);
    const [isMapMenuOpen, setIsMapMenuOpen] = useState(false);
    const [showNuevo, setShowNuevo] = useState(true);
    const [showViejo, setShowViejo] = useState(true);
    const [showStreets, setShowStreets] = useState(false);
    const [showDisponible, setShowDisponible] = useState(false);
    const [showOcupado, setShowOcupado] = useState(false);

    return (
        <MapContext.Provider
            value={{
                isSatellite, setIsSatellite,
                rotationEnabled, setRotationEnabled,
                isMapMenuOpen, setIsMapMenuOpen,
                showNuevo, setShowNuevo,
                showViejo, setShowViejo,
                showStreets, setShowStreets,
                showDisponible, setShowDisponible,
                showOcupado, setShowOcupado
            }}
        >
            {children}
        </MapContext.Provider>
    );
};

export const useMap = () => {
    const context = useContext(MapContext);
    if (!context) {
        throw new Error("useMap debe usarse dentro de un MapProvider");
    }
    return context;
};