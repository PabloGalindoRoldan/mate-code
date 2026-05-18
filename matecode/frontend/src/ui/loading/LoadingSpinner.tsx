import { Loader2 } from "lucide-react";
import "./LoadingSpinner.css";

interface LoadingSpinnerProps {
    text?: string;
    fullScreen?: boolean;
}

export default function LoadingSpinner({
    text = "Cargando...",
    fullScreen = false
}: LoadingSpinnerProps) {

    return (
        <div className={`loading-container ${fullScreen ? "fullscreen" : "inline"}`}>
            <div className="loading-content">
                <Loader2 className="spinner-icon" size={40} />
                <p className="loading-text">{text}</p>
            </div>
        </div>
    );
}