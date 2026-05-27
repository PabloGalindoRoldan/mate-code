import { Clock } from "lucide-react";
import "./EnRevision.css";

export default function EnRevision() {
    return (
        <div className="en-revision-container">
            <div className="card-revision">
                <div className="icon-box">
                    <Clock size={48} className="text-amber-500" />
                </div>

                <h2>Proyecto en Revisión</h2>
                <p>
                    Tu documentación ha sido recibida correctamente y está siendo analizada por el equipo técnico.
                    Este proceso puede demorar unos días hábiles.
                </p>

                <div className="info-box">
                    <p>Puedes verificar el estado y cualquier novedad en tu panel de mensajes.</p>
                </div>
            </div>
        </div>
    )
}