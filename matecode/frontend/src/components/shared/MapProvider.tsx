import { createContext, useContext, useState, type ReactNode } from "react";

// Definimos la estructura de los datos del contexto
interface MapContextType {
    isSatellite: boolean;
    setIsSatellite: (val: boolean) => void;
    rotationEnabled: boolean;
    setRotationEnabled: (val: boolean) => void;
    isMapMenuOpen: boolean;
    setIsMapMenuOpen: (val: boolean) => void;
}

const MapContext = createContext<MapContextType | undefined>(undefined);

export const MapProvider = ({ children }: { children: ReactNode }) => {
    const [isSatellite, setIsSatellite] = useState(true);
    const [rotationEnabled, setRotationEnabled] = useState(true);
    const [isMapMenuOpen, setIsMapMenuOpen] = useState(false);

    return (
        <MapContext.Provider
            value={{
                isSatellite,
                setIsSatellite,
                rotationEnabled,
                setRotationEnabled,
                isMapMenuOpen,
                setIsMapMenuOpen,
            }}
        >
            {children}
        </MapContext.Provider>
    );
};

// Hook personalizado para usar el contexto fácilmente
export const useMap = () => {
    const context = useContext(MapContext);
    if (!context) {
        throw new Error("useMap debe usarse dentro de un MapProvider");
    }
    return context;
};