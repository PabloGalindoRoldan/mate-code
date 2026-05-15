import './SysAdminBody.css';
import { useState } from 'react';
import RegisterAdminView from '../views/RegisterAdminView';
import { ChevronLeft } from "lucide-react";

export default function SysAdminBody() {
    const [registrar, setRegistrar] = useState(false);

    function handleClick() {
        setRegistrar((prev) => !prev);
    }

    return (
        <div className="sysAdminBody">
            {!registrar ? <h2 className="adminTitle">Vista de administrador del Sistema</h2> : ""}
            {registrar ? <button className="menu-toggle-btn" onClick={handleClick}><ChevronLeft /></button> : ""}
            {!registrar ? (
                <button onClick={handleClick} className="buttonRegistrarseLanding">
                    Crear Nuevo Administrador de Parque
                </button>
            ) : (
                <RegisterAdminView />
            )}
        </div>
    );
}