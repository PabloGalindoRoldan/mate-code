import { CircleX } from "lucide-react";
import "./Rechazado.css";

export default function Rechazado() {
    return (
        <div className="rechazado-container">
            <div className="card-rechazado">

                <div className="icon-box-rechazado">
                    <CircleX size={52} className="icon-rejected" />
                </div>

                <h2>Proyecto Rechazado</h2>

                <p>
                    Lo sentimos, pero el proyecto presentado no ha sido aprobado
                    por el equipo evaluador.
                </p>

                <div className="rechazado-info-box">
                    <p>
                        Puedes consultar las observaciones y detalles del rechazo
                        desde tu panel de mensajes.
                    </p>
                </div>

            </div>
        </div>
    );
}