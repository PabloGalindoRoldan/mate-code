import "./ProyectosPanel.css";

import { useEffect, useMemo, useState } from "react";

import {
    FolderKanban,
    Search,
    Eye,
    CheckCircle2,
    XCircle,
    AlertTriangle,
    Clock3,
    FileText,
    Building2,
    User,
    X,
} from "lucide-react";

import "./ProyectosPanel.css";
import { proyectosApi, mensajeriaApi, empresasApi } from "../../api/axios";

interface Proyecto {
    id: number;
    nombre: string;
    usuarioNombre: string;
    cuitEmpresa: string;
    estado: string;
    fechaCreacion?: string;
    fechaActualizacion?: string;

    actividadPrincipal?: string;
    actividadSecundaria?: string;
    descripcion?: string;
    personaReferente?: string;
    telefono?: string;
    rubro?: string;

    tipoEmpresa?: string;
    superficieRequerida?: number;
    personalAOcupar?: number;
    potenciaInstalada?: number;

    linkPlanos?: string;
    linkViabilidadFinanciera?: string;
    linkEstudioMercado?: string;
    linkImpactoAmbiental?: string;
    linkHabilitacionMunicipal?: string;
    linkCertificadoInhibiciones?: string;
    tipoProyecto?: "Preliminar" | "Definitivo";
}

interface ProyectosResponse {
    preliminares?: Proyecto[];
    definitivos?: Proyecto[];
}

export default function ProyectosPanel() {

    const [loading, setLoading] = useState(true);
    const [proyectos, setProyectos] = useState<Proyecto[]>([]);
    const [selectedProyecto, setSelectedProyecto] =
        useState<Proyecto | null>(null);
    const [search, setSearch] = useState("");
    const [decisionMessage, setDecisionMessage] = useState("");
    const [sendingDecision, setSendingDecision] = useState(false);
    const [selectedLote, setSelectedLote] = useState("");

    const canTakeDecision =
        selectedProyecto?.estado === "en_revision";

    const cambiarEstado = async (estado: string) => {
        if (!selectedProyecto) return;

        const payload = {
            proyectoId: selectedProyecto.id,
            estado
        };

        if (selectedProyecto.tipoProyecto === "Definitivo") {
            return proyectosApi.cambiarEstadoDefinitivo(payload);
        }

        return proyectosApi.cambiarEstadoPreliminar(payload);
    };

    const solicitarRectificacion = async () => {
        if (!selectedProyecto || !decisionMessage.trim()) return;

        try {
            setSendingDecision(true);

            // 1. Send message
            await mensajeriaApi.enviarMensaje(
                selectedProyecto.usuarioNombre,
                decisionMessage
            );

            // 2. Change state
            await cambiarEstado("RECTIFICAR");

            // 3. update UI locally
            setProyectos(prev =>
                prev.map(p =>
                    p.id === selectedProyecto.id
                        ? { ...p, estado: "rectificar" }
                        : p
                )
            );

            setSelectedProyecto(null);
            setDecisionMessage("");

        } finally {
            setSendingDecision(false);
        }
    };


    const aprobarProyecto = async () => {

        if (!selectedProyecto) return;

        if (!selectedLote.trim()) {
            alert("Debe seleccionar un lote.");
            return;
        }

        if (!decisionMessage.trim()) {
            alert("Debe redactar un mensaje para la empresa.");
            return;
        }

        try {
            setSendingDecision(true);

            const loteIdParsed = Number(selectedLote);

            if (isNaN(loteIdParsed)) {
                alert("ID de lote inválido");
                return;
            }

            // =========================
            // ASIGNAR / OCUPAR LOTE
            // =========================

            if (selectedProyecto.tipoProyecto === "Definitivo") {
                await empresasApi.ocuparLote(
                    selectedProyecto.cuitEmpresa,
                    loteIdParsed
                );
            } else {
                await empresasApi.asignarLote(
                    selectedProyecto.cuitEmpresa,
                    loteIdParsed
                );
            }

            // =========================
            // MENSAJE
            // =========================

            await mensajeriaApi.enviarMensaje(
                selectedProyecto.usuarioNombre,
                decisionMessage
            );

            // =========================
            // CAMBIAR ESTADO PROYECTO
            // =========================

            await cambiarEstado("APROBADO");

            // =========================
            // RADICACIÓN (SOLO DEFINITIVOS)
            // =========================

            if (selectedProyecto.tipoProyecto === "Definitivo") {
                await empresasApi.actualizarEstadoRadicacion(
                    selectedProyecto.cuitEmpresa,
                    true
                );
            }

            // =========================
            // UPDATE LOCAL UI
            // =========================

            setProyectos(prev =>
                prev.map(p =>
                    p.id === selectedProyecto.id
                        ? { ...p, estado: "aprobado" }
                        : p
                )
            );

            // =========================
            // CLEANUP
            // =========================

            setSelectedProyecto(null);
            setDecisionMessage("");
            setSelectedLote("");

        } catch (err) {
            console.error(err);
            alert("Error al aprobar el proyecto.");
        } finally {
            setSendingDecision(false);
        }
    };

    const rechazarProyecto = async () => {

        if (!selectedProyecto) return;

        if (!decisionMessage.trim()) {
            alert("Debe redactar un mensaje para la empresa.");
            return;
        }

        try {

            setSendingDecision(true);

            // =========================
            // LIBERAR LOTE
            // =========================

            await empresasApi.desocuparLote(
                selectedProyecto.cuitEmpresa
            );

            // =========================
            // EMPRESA NO RADICADA
            // =========================

            await empresasApi.actualizarEstadoRadicacion(
                selectedProyecto.cuitEmpresa,
                false
            );

            // =========================
            // MENSAJE
            // =========================

            await mensajeriaApi.enviarMensaje(
                selectedProyecto.usuarioNombre,
                decisionMessage
            );

            // =========================
            // CAMBIAR ESTADO PROYECTO
            // =========================

            await cambiarEstado("RECHAZADO");

            // =========================
            // UPDATE LOCAL UI
            // =========================

            setProyectos(prev =>
                prev.map(p =>
                    p.id === selectedProyecto.id
                        ? { ...p, estado: "rechazado" }
                        : p
                )
            );

            // =========================
            // CLEANUP
            // =========================

            setSelectedProyecto(null);
            setDecisionMessage("");
            setSelectedLote("");

        } catch (err) {

            console.error(err);
            alert("Error al rechazar el proyecto.");

        } finally {

            setSendingDecision(false);

        }
    };

    useEffect(() => {
        cargarProyectos();
    }, []);
    const cargarProyectos = async () => {
        try {
            setLoading(true);
            const response: ProyectosResponse =
                await proyectosApi.listarProyectos();
            const preliminares = (response.preliminares || []).map(p => ({
                ...p,
                tipoProyecto: "Preliminar" as const,
            }));

            const definitivos = (response.definitivos || []).map(p => ({
                ...p,
                tipoProyecto: "Definitivo" as const,
            }));

            setProyectos([
                ...preliminares,
                ...definitivos
            ]);

        } catch (error) {
            console.error("Error cargando proyectos", error);
        } finally {
            setLoading(false);
        }
    };

    const filtered = useMemo(() => {
        return proyectos.filter((p) =>
            `${p.nombre} ${p.usuarioNombre} ${p.cuitEmpresa}`
                .toLowerCase()
                .includes(search.toLowerCase())
        );

    }, [proyectos, search]);
    const grouped = {
        preliminaresEn_revision:
            filtered.filter(
                (p: any) =>
                    p.tipoProyecto === "Preliminar" &&
                    p.estado === "en_revision"
            ),

        definitivosEn_revision:
            filtered.filter(
                (p: any) =>
                    p.tipoProyecto === "Definitivo" &&
                    p.estado === "en_revision"
            ),

        preliminaresRectificar:
            filtered.filter(
                (p: any) =>
                    p.tipoProyecto === "Preliminar" &&
                    p.estado === "rectificar"
            ),

        definitivosRectificar:
            filtered.filter(
                (p: any) =>
                    p.tipoProyecto === "Definitivo" &&
                    p.estado === "rectificar"
            ),

        aprobados:
            filtered.filter(
                (p) => p.estado === "aprobado"
            ),

        rechazados:
            filtered.filter(
                (p) => p.estado === "rechazado"
            ),
    };

    const renderSection = (
        title: string,
        icon: React.ReactNode,
        projects: Proyecto[],
        className: string
    ) => (
        <section className="proyectos-section">

            <div className="section-header">

                <div className={`section-icon ${className}`}>
                    {icon}
                </div>

                <div>
                    <h3>{title}</h3>
                    <span>{projects.length} proyectos</span>
                </div>
            </div>

            <div className="projects-grid">

                {projects.length === 0 && (
                    <div className="empty-projects">
                        No hay proyectos en esta categoría.
                    </div>
                )}

                {projects.map((proyecto: any) => (

                    <div
                        key={`${proyecto.tipoProyecto}-${proyecto.id}`}
                        className="project-card"
                    >

                        <div className="project-card-top">

                            <div>
                                <h4>{proyecto.nombre}</h4>

                                <span className="project-type">
                                    {proyecto.tipoProyecto}
                                </span>
                            </div>

                            <button
                                className="btn-view"
                                onClick={() =>
                                    setSelectedProyecto(proyecto)
                                }
                            >
                                <Eye size={18} />
                            </button>
                        </div>

                        <div className="project-card-content">

                            <div className="project-info-row">
                                <Building2 size={16} />
                                <span>{proyecto.cuitEmpresa}</span>
                            </div>

                            <div className="project-info-row">
                                <User size={16} />
                                <span>{proyecto.usuarioNombre}</span>
                            </div>

                            <div className="project-info-row">
                                <FileText size={16} />
                                <span>
                                    {proyecto.actividadPrincipal || "Sin actividad"}
                                </span>
                            </div>
                        </div>

                    </div>
                ))}
            </div>
        </section>
    );

    return (
        <div className="proyectos-panel">

            <div className="proyectos-header">

                <div>
                    <h1>
                        <FolderKanban size={30} />
                        Gestión de Proyectos
                    </h1>

                    <p>
                        Administra proyectos preliminares y definitivos del parque industrial.
                    </p>
                </div>

                <div className="search-box">

                    <Search size={18} />

                    <input
                        type="text"
                        placeholder="Buscar por nombre, usuario o CUIT..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>
            </div>

            {loading ? (

                <div className="loading-projects">
                    Cargando proyectos...
                </div>

            ) : (

                <>
                    {renderSection(
                        "Preliminares en Revisión",
                        <Clock3 />,
                        grouped.preliminaresEn_revision,
                        "en_revision"
                    )}

                    {renderSection(
                        "Definitivos en Revisión",
                        <Clock3 />,
                        grouped.definitivosEn_revision,
                        "en_revision"
                    )}

                    {renderSection(
                        "Preliminares a Rectificar",
                        <AlertTriangle />,
                        grouped.preliminaresRectificar,
                        "rectificar"
                    )}

                    {renderSection(
                        "Definitivos a Rectificar",
                        <AlertTriangle />,
                        grouped.definitivosRectificar,
                        "rectificar"
                    )}

                    {renderSection(
                        "Proyectos Aprobados",
                        <CheckCircle2 />,
                        grouped.aprobados,
                        "approved"
                    )}

                    {renderSection(
                        "Proyectos Rechazados",
                        <XCircle />,
                        grouped.rechazados,
                        "rejected"
                    )}
                </>
            )}

            {selectedProyecto && (

                <div
                    className="modal-overlay"
                    onClick={() => {
                        setSelectedProyecto(null);
                        setDecisionMessage("");
                        setSelectedLote("");
                    }}
                >

                    <div
                        className="modal-content"
                        onClick={(e) => e.stopPropagation()}
                    >

                        <button
                            className="close-modal"
                            onClick={() => {
                                setSelectedProyecto(null);
                                setDecisionMessage("");
                                setSelectedLote("");
                            }}
                        >
                            <X size={22} />
                        </button>

                        <div className="modal-header">

                            <div>
                                <h2>{selectedProyecto.nombre}</h2>

                                <span>
                                    {selectedProyecto.estado} • {(selectedProyecto as any).tipoProyecto}
                                </span>
                            </div>
                        </div>

                        <div className="modal-grid">

                            {/* ================= IDENTIFICACIÓN ================= */}
                            <fieldset className="form-section">
                                <legend>Identificación</legend>

                                <div className="modal-field">
                                    <label>Nombre</label>
                                    <p>{selectedProyecto.nombre}</p>
                                </div>
                                <div className="modal-field">
                                    <label>Numero de Proyecto</label>
                                    <p>{selectedProyecto.id}</p>
                                </div>

                                <div className="modal-field">
                                    <label>Fecha Creación</label>
                                    <p>{selectedProyecto.fechaCreacion || "-"}</p>
                                </div>

                                <div className="modal-field">
                                    <label>Última Actualización</label>
                                    <p>{selectedProyecto.fechaActualizacion || "-"}</p>
                                </div>

                            </fieldset>

                            {/* ================= CONTACTO ================= */}
                            <fieldset className="form-section">
                                <legend>Contacto</legend>

                                <div className="modal-field">
                                    <label>Usuario</label>
                                    <p>{selectedProyecto.usuarioNombre}</p>
                                </div>
                                <div className="modal-field">
                                    <label>Persona Referente</label>
                                    <p>{selectedProyecto.personaReferente || "-"}</p>
                                </div>
                                <div className="modal-field">
                                    <label>Teléfono</label>
                                    <p>{selectedProyecto.telefono || "-"}</p>
                                </div>
                                <div className="modal-field">
                                    <label>CUIT</label>
                                    <p>{selectedProyecto.cuitEmpresa}</p>
                                </div>
                            </fieldset>

                            {/* ================= ACTIVIDAD ================= */}
                            <fieldset className="form-section">
                                <legend>Actividad Propuesta</legend>

                                <div className="modal-field">
                                    <label>Actividad Principal</label>
                                    <p>{selectedProyecto.actividadPrincipal || "-"}</p>
                                </div>

                                <div className="modal-field">
                                    <label>Actividad Secundaria</label>
                                    <p>{selectedProyecto.actividadSecundaria || "-"}</p>
                                </div>

                                <div className="modal-field full-width">
                                    <label>Descripción</label>
                                    <p>{selectedProyecto.descripcion || "-"}</p>
                                </div>
                            </fieldset>

                            {/* ================= ANTECEDENTES ================= */}
                            <fieldset className="form-section">
                                <legend>ANTECEDENTES</legend>

                                <div className="modal-field">
                                    <label>Tipo Empresa</label>
                                    <p>{selectedProyecto.tipoEmpresa || "-"}</p>
                                </div>

                                {selectedProyecto.tipoEmpresa !== "nueva" && (
                                    <>
                                        <div className="modal-field">
                                            <label>Rubro</label>
                                            <p>{selectedProyecto.rubro || "-"}</p>
                                        </div>

                                        <div className="modal-field full-width">
                                            <label>Dirección</label>
                                            <p>{(selectedProyecto as any).direccion || "-"}</p>
                                        </div>

                                        <div className="modal-field">
                                            <label>Emplazamiento Actual</label>
                                            <p>{(selectedProyecto as any).emplazamientoActual || "-"}</p>
                                        </div>

                                        <div className="modal-field">
                                            <label>Tiempo Radicación</label>
                                            <p>{(selectedProyecto as any).tiempoRadicacion || "-"}</p>
                                        </div>
                                        <div className="modal-field full-width">
                                            <label>Descripción Servicio</label>
                                            <p>{(selectedProyecto as any).descripcionServicio || "-"}</p>
                                        </div>
                                    </>
                                )}
                            </fieldset>

                            {/* ================= SUPERFICIES ================= */}
                            <fieldset className="form-section">
                                <legend>Superficies Requeridas</legend>

                                <div className="modal-field">
                                    <label>Superficie Total</label>
                                    <p>{selectedProyecto.superficieRequerida ?? 0} m²</p>
                                </div>

                                <div className="modal-field">
                                    <label>Superficie Trabajo</label>
                                    <p>{(selectedProyecto as any).superficieTrabajo ?? 0} m²</p>
                                </div>

                                <div className="modal-field">
                                    <label>Superficie Depósito</label>
                                    <p>{(selectedProyecto as any).superficieDeposito ?? 0} m²</p>
                                </div>

                                <div className="modal-field">
                                    <label>Superficie Cubierta</label>
                                    <p>{(selectedProyecto as any).superficieCubierta ?? 0} m²</p>
                                </div>

                                <div className="modal-field">
                                    <label>Superficie Estacionamiento</label>
                                    <p>{(selectedProyecto as any).superficieEstacionamiento ?? 0} m²</p>
                                </div>
                            </fieldset>

                            {/* ================= CONSUMOS ================= */}
                            <fieldset className="form-section">
                                <legend>Consumos y Energía</legend>

                                <div className="modal-field">
                                    <label>Potencia Instalada Prevista</label>
                                    <p>{selectedProyecto.potenciaInstalada ?? 0} kW/mes</p>
                                </div>
                                <div className="modal-field">
                                    <label>Tensión Alimentación</label>
                                    <p>{(selectedProyecto as any).tensionAlimentacion || "-"}</p>
                                </div>
                                <div className="modal-field">
                                    <label>Agua Mensual</label>
                                    <p>{(selectedProyecto as any).aguaMensual ?? 0} m³/mes</p>
                                </div>

                                <div className="modal-field">
                                    <label>Gas Mensual</label>
                                    <p>{(selectedProyecto as any).gasMensual ?? 0} m³/mes</p>
                                </div>

                                <div className="modal-field">
                                    <label>Residuos Tipo</label>
                                    <p>{(selectedProyecto as any).residuosTipo || "-"}</p>
                                </div>

                                <div className="modal-field">
                                    <label>Cantidad de Residuos</label>
                                    <p>{(selectedProyecto as any).residuosCantidad ?? 0} Kg/mes </p>
                                </div>

                                <div className="modal-field">
                                    <label>Tratamiento Efluentes</label>
                                    <p>{(selectedProyecto as any).tratamientoEfluentes || "-"}</p>
                                </div>

                                <div className="modal-field">
                                    <label>Personal a Ocupar</label>
                                    <p>{selectedProyecto.personalAOcupar || 0}</p>
                                </div>
                            </fieldset>

                            {/* ================= INFRAESTRUCTURA ================= */}
                            <fieldset className="form-section">
                                <legend>Infraestructura Requerida</legend>

                                <div className="modal-field">
                                    <label>Balanza Pública</label>
                                    <p>{(selectedProyecto as any).balanzaPublica || "-"}</p>
                                </div>

                                <div className="modal-field">
                                    <label>Comedor</label>
                                    <p>{(selectedProyecto as any).comedor || "-"}</p>
                                </div>

                                <div className="modal-field">
                                    <label>SUM / Coworking</label>
                                    <p>{(selectedProyecto as any).sumCoworking || "-"}</p>
                                </div>
                            </fieldset>

                            {/* ================ DOCUMENTACIÓN =================== */}
                            <fieldset className="form-section">
                                <legend>Documentación</legend>

                                {selectedProyecto.linkPlanos && (
                                    <a className="document-link" href={selectedProyecto.linkPlanos} target="_blank">
                                        Planos
                                    </a>
                                )}

                                {selectedProyecto.linkViabilidadFinanciera && (
                                    <a className="document-link" href={selectedProyecto.linkViabilidadFinanciera} target="_blank">
                                        Viabilidad Financiera
                                    </a>
                                )}

                                {selectedProyecto.linkEstudioMercado && (
                                    <a className="document-link" href={selectedProyecto.linkEstudioMercado} target="_blank">
                                        Estudio Mercado
                                    </a>
                                )}

                                {selectedProyecto.linkImpactoAmbiental && (
                                    <a className="document-link" href={selectedProyecto.linkImpactoAmbiental} target="_blank">
                                        Impacto Ambiental
                                    </a>
                                )}

                                {selectedProyecto.linkHabilitacionMunicipal && (
                                    <a className="document-link" href={selectedProyecto.linkHabilitacionMunicipal} target="_blank">
                                        Habilitación Municipal
                                    </a>
                                )}

                                {selectedProyecto.linkCertificadoInhibiciones && (
                                    <a className="document-link" href={selectedProyecto.linkCertificadoInhibiciones} target="_blank">
                                        Certificado Inhibiciones
                                    </a>
                                )}
                            </fieldset>


                        </div>

                        {canTakeDecision && (
                            <div className="decision-block">

                                <div className="decision-panel">

                                    <textarea
                                        className="decision-textarea"
                                        placeholder="Escribí el motivo de la decisión..."
                                        value={decisionMessage}
                                        onChange={(e) => setDecisionMessage(e.target.value)}
                                        disabled={sendingDecision}
                                    />

                                    <input
                                        type="number"
                                        className="decision-lote-input"
                                        placeholder="ID del lote (opcional)"
                                        value={selectedLote}
                                        onChange={(e) => setSelectedLote(e.target.value)}
                                        disabled={sendingDecision}
                                    />

                                </div>

                                <div className="decision-actions">

                                    <button
                                        className="btn-rectify"
                                        onClick={solicitarRectificacion}
                                        disabled={sendingDecision}
                                    >
                                        Rectificar
                                    </button>

                                    <button
                                        className="btn-approve"
                                        onClick={aprobarProyecto}
                                        disabled={sendingDecision}
                                    >
                                        Aprobar
                                    </button>

                                    <button
                                        className="btn-reject"
                                        onClick={rechazarProyecto}
                                        disabled={sendingDecision}
                                    >
                                        Rechazar
                                    </button>

                                </div>

                            </div>
                        )}
                        {!canTakeDecision && (
                            <div className="modal-actions">
                                <div className={`status-pill ${selectedProyecto.estado}`}>
                                    {selectedProyecto.estado === "aprobado" && "Proyecto aprobado"}

                                    {selectedProyecto.estado === "rechazado" && "Proyecto rechazado"}

                                    {selectedProyecto.estado === "rectificar" && "Debe Rectificar"}
                                </div>
                            </div>
                        )}

                    </div>
                </div>
            )}
        </div>
    );
}