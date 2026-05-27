import { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { proyectosApi } from '../../api/axios';

import "./EmpresaNoRadicadaView.css";

import FormularioPreliminar from './FormularioPreliminar';
import EnRevision from './EnRevision';
import ErrorView from './ErrorView';
import FormularioDefinitivo from './FormularioDefinitivo';

import NavBar from '../../ui/navBar/NavBar';
import Footer from '../../ui/footer/Footer';

import { MessageSquare } from 'lucide-react';
import MensajeriaPanel from '../mensajeria/MensajeriaPanel';

import { ESTADOS_PROYECTO } from './estadosProyecto';

export default function EmpresaNoRadicadaView() {

    const { user } = useAuth();

    const [status, setStatus] = useState(ESTADOS_PROYECTO.NINGUNO);
    const [loading, setLoading] = useState(true);
    const [mensajesOpen, setMensajesOpen] = useState(false);
    const [preliminarActual, setPreliminarActual] = useState<any>(null);

    useEffect(() => {

        const cuit = user?.empresa?.cuit;

        if (!cuit) {
            setLoading(false);
            return;
        }

        let isMounted = true;

        const normalizeEstado = (estado?: string) =>
            String(estado || '')
                .trim()
                .toLowerCase();

        const checkStatus = async () => {

            try {

                setLoading(true);

                const data = await proyectosApi.proyectosPorCuit(cuit);

                if (!isMounted) return;

                console.log("DEBUG API:", data);

                const definitivo = data?.definitivos?.[0];
                const preliminar = data?.preliminares?.[0];

                if (definitivo?.estado) {

                    const estado = normalizeEstado(definitivo.estado);

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
                    }
                }

                if (preliminar?.estado) {
                    setPreliminarActual(preliminar);
                    const estado = normalizeEstado(preliminar.estado);
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
                    }
                }

                setStatus(ESTADOS_PROYECTO.NINGUNO);

            } catch (error) {

                console.error("Error al obtener estado:", error);

                if (isMounted) {
                    setStatus(ESTADOS_PROYECTO.NINGUNO);
                }

            } finally {

                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        checkStatus();

        return () => {
            isMounted = false;
        };

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
                        onProyectoEnviado={() =>
                            setStatus(ESTADOS_PROYECTO.PRELIMINAR_EN_REVISION)
                        }
                        proyectoExistente={null}
                    />
                );

            case ESTADOS_PROYECTO.PRELIMINAR_EN_REVISION:

                return <EnRevision />;

            case ESTADOS_PROYECTO.PRELIMINAR_RECTIFICAR:

                return (
                    <FormularioPreliminar
                        isRectifying={true}
                        onProyectoEnviado={() =>
                            setStatus(ESTADOS_PROYECTO.PRELIMINAR_EN_REVISION)
                        }
                        proyectoExistente={preliminarActual}
                    />
                );

            case ESTADOS_PROYECTO.PRELIMINAR_APROBADO:

                return (
                    <FormularioDefinitivo isRectifying={false} />
                );

            case ESTADOS_PROYECTO.DEFINITIVO_EN_REVISION:

                return <EnRevision />;

            case ESTADOS_PROYECTO.DEFINITIVO_RECTIFICAR:

                return (
                    <FormularioDefinitivo isRectifying={true} />
                );

            case ESTADOS_PROYECTO.DEFINITIVO_APROBADO:

                return (
                    <div>
                        Proyecto Definitivo Aprobado, contacte al administrador.
                    </div>
                );

            default:

                return <ErrorView />;
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

            {mensajesOpen && <MensajeriaPanel />}
            <div className='noRadicadaBody'>
                {renderView()}
            </div>

            <Footer />

        </div>
    );
}