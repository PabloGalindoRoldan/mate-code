import { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { proyectosApi } from '../../api/axios';

import "./EmpresaNoRadicadaView.css";

import FormularioPreliminar from './FormularioPreliminar';
import EnRevision from './EnRevision';
import FormularioDefinitivo from './FormularioDefinitivo';

import NavBar from '../../ui/navBar/NavBar';
import Footer from '../../ui/footer/Footer';

import { MessageSquare } from 'lucide-react';
import MensajeriaPanel from '../mensajeria/MensajeriaPanel';

import { ESTADOS_PROYECTO } from './estadosProyecto';
import Rechazado from './Rechazado';

export default function EmpresaNoRadicadaView() {

    const { user } = useAuth();

    const [status, setStatus] = useState(ESTADOS_PROYECTO.NINGUNO);
    const [loading, setLoading] = useState(true);
    const [mensajesOpen, setMensajesOpen] = useState(false);
    const [preliminarActual, setPreliminarActual] = useState<any>(null);
    const [definitivoActual, setDefinitivoActual] = useState<any>(null);


    const refreshProyecto = async (cuit: string) => {
        try {
            setLoading(true);

            const data = await proyectosApi.proyectosPorCuit(cuit);

            console.log("DEBUG API:", data);

            const definitivo = data?.definitivos?.at(-1);
            const preliminar = data?.preliminares?.at(-1);

            if (definitivo?.estado) {
                setDefinitivoActual(definitivo);
                const estado = String(definitivo.estado).trim().toLowerCase();

                switch (estado) {
                    case 'en_revision':
                        setStatus(ESTADOS_PROYECTO.DEFINITIVO_EN_REVISION);
                        return;
                    case 'rectificar':
                        setStatus(ESTADOS_PROYECTO.DEFINITIVO_RECTIFICAR);
                        return;
                    case 'aprobado':
                        setStatus(ESTADOS_PROYECTO.DEFINITIVO_APROBADO);
                        return;
                    case 'rechazado':
                        setStatus(ESTADOS_PROYECTO.RECHAZADO);
                        return;
                }
            }

            if (preliminar?.estado) {
                setPreliminarActual(preliminar);

                const estado = String(preliminar.estado).trim().toLowerCase();

                switch (estado) {
                    case 'en_revision':
                        setStatus(ESTADOS_PROYECTO.PRELIMINAR_EN_REVISION);
                        return;
                    case 'rectificar':
                        setStatus(ESTADOS_PROYECTO.PRELIMINAR_RECTIFICAR);
                        return;
                    case 'aprobado':
                        setStatus(ESTADOS_PROYECTO.PRELIMINAR_APROBADO);
                        return;
                    case 'rechazado':
                        setStatus(ESTADOS_PROYECTO.RECHAZADO);
                        return;
                }
            }

            setStatus(ESTADOS_PROYECTO.NINGUNO);

        } catch (error) {
            console.error("Error al obtener estado:", error);
            setStatus(ESTADOS_PROYECTO.NINGUNO);
        } finally {
            setLoading(false);
        }
    };



    useEffect(() => {
        const cuit = user?.empresa?.cuit;

        if (!cuit) {
            setLoading(false);
            return;
        }

        refreshProyecto(cuit);
    }, [user?.empresa?.cuit]);

    // =========================================
    // DEBUG
    // =========================================

    console.log("STATUS FINAL:", status);

    // =========================================
    // LOADING
    // =========================================

    if (loading) {
        return <div>Cargando...</div>;
    }

    // =========================================
    // RENDER
    // =========================================

    const renderView = () => {

        switch (status) {

            case ESTADOS_PROYECTO.NINGUNO:

                return (
                    <FormularioPreliminar
                        isRectifying={false}
                        onProyectoEnviado={() => {
                            const cuit = user?.empresa?.cuit;
                            if (cuit) refreshProyecto(cuit);
                        }}
                        proyectoExistente={null}
                    />
                );

            case ESTADOS_PROYECTO.RECHAZADO:

                return <Rechazado />

            case ESTADOS_PROYECTO.PRELIMINAR_EN_REVISION:

                return <EnRevision />;

            case ESTADOS_PROYECTO.PRELIMINAR_RECTIFICAR:

                return (
                    <FormularioPreliminar
                        isRectifying={true}
                        onProyectoEnviado={() => {
                            const cuit = user?.empresa?.cuit;
                            if (cuit) refreshProyecto(cuit);
                        }}
                        proyectoExistente={preliminarActual}
                    />
                );

            case ESTADOS_PROYECTO.PRELIMINAR_APROBADO:

                return (
                    <FormularioDefinitivo
                        isRectifying={false}
                        onProyectoEnviado={() => {
                            const cuit = user?.empresa?.cuit;
                            if (cuit) refreshProyecto(cuit);
                        }}
                        proyectoExistente={definitivoActual} />
                );

            case ESTADOS_PROYECTO.DEFINITIVO_EN_REVISION:

                return <EnRevision />;

            case ESTADOS_PROYECTO.DEFINITIVO_RECTIFICAR:

                return (
                    <FormularioDefinitivo
                        isRectifying={true}
                        onProyectoEnviado={() => {
                            const cuit = user?.empresa?.cuit;
                            if (cuit) refreshProyecto(cuit);
                        }}
                        proyectoExistente={definitivoActual} />
                );

            case ESTADOS_PROYECTO.DEFINITIVO_APROBADO:

                return (
                    <div className="empresaNoRadicadaView">
                        <h1 style={{ color: "var(--gris1)", padding: "5rem" }}>Proyecto Definitivo Aprobado. Si ud. esta viendo este mensaje, contacte al administrador para que manualmente radique la empresa en el parque.</h1>
                    </div>
                );

            default:

                return <div>Error!</div>;
        }
    };

    return (
        <div className="empresaNoRadicadaView">
            <button
                className="btn-flotante-mensajes"
                onClick={() => setMensajesOpen(!mensajesOpen)}
            >
                <MessageSquare size={20} />
                {mensajesOpen ? "Regresar" : "Mensajes"}
            </button>

            <NavBar />
            {mensajesOpen ?
                <div className='noRadicadaBody noRadicadaMensajeria'>
                    <MensajeriaPanel />
                </div> :
                <div className='noRadicadaBody'>
                    {renderView()}
                </div>
            }

            <Footer />

        </div>
    );
}