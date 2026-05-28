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
    CalendarDays,
    X
} from "lucide-react";

import "./ProyectosPanel.css";
import { proyectosApi, mensajeriaApi } from "../../api/axios";

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

    const isFinalState =
        selectedProyecto?.estado === "aprobado" ||
        selectedProyecto?.estado === "rechazado";

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
                    onClick={() => setSelectedProyecto(null)}
                >

                    <div
                        className="modal-content"
                        onClick={(e) => e.stopPropagation()}
                    >

                        <button
                            className="close-modal"
                            onClick={() => setSelectedProyecto(null)}
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

                            <div className="modal-field">
                                <label>Usuario</label>
                                <p>{selectedProyecto.usuarioNombre}</p>
                            </div>

                            <div className="modal-field">
                                <label>CUIT</label>
                                <p>{selectedProyecto.cuitEmpresa}</p>
                            </div>

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

                            <div className="modal-field">
                                <label>Persona Referente</label>
                                <p>{selectedProyecto.personaReferente || "-"}</p>
                            </div>

                            <div className="modal-field">
                                <label>Teléfono</label>
                                <p>{selectedProyecto.telefono || "-"}</p>
                            </div>

                            <div className="modal-field">
                                <label>Superficie</label>
                                <p>
                                    {selectedProyecto.superficieRequerida || 0} m²
                                </p>
                            </div>

                            <div className="modal-field">
                                <label>Personal</label>
                                <p>
                                    {selectedProyecto.personalAOcupar || 0}
                                </p>
                            </div>

                            <div className="modal-field">
                                <label>Potencia</label>
                                <p>
                                    {selectedProyecto.potenciaInstalada || 0} KW
                                </p>
                            </div>

                            <div className="modal-field">
                                <label>Fecha</label>

                                <p className="date-row">
                                    <CalendarDays size={16} />
                                    {selectedProyecto.fechaCreacion || "-"}
                                </p>
                            </div>
                        </div>

                        <div className="documents-section">

                            <h3>Documentación</h3>

                            <div className="documents-grid">

                                {selectedProyecto.linkPlanos && (
                                    <a
                                        href={selectedProyecto.linkPlanos}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="document-link"
                                    >
                                        Planos
                                    </a>
                                )}

                                {selectedProyecto.linkViabilidadFinanciera && (
                                    <a
                                        href={selectedProyecto.linkViabilidadFinanciera}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="document-link"
                                    >
                                        Viabilidad Financiera
                                    </a>
                                )}

                                {selectedProyecto.linkEstudioMercado && (
                                    <a
                                        href={selectedProyecto.linkEstudioMercado}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="document-link"
                                    >
                                        Estudio de Mercado
                                    </a>
                                )}

                                {selectedProyecto.linkImpactoAmbiental && (
                                    <a
                                        href={selectedProyecto.linkImpactoAmbiental}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="document-link"
                                    >
                                        Impacto Ambiental
                                    </a>
                                )}

                                {selectedProyecto.linkHabilitacionMunicipal && (
                                    <a
                                        href={selectedProyecto.linkHabilitacionMunicipal}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="document-link"
                                    >
                                        Habilitación Municipal
                                    </a>
                                )}

                                {selectedProyecto.linkCertificadoInhibiciones && (
                                    <a
                                        href={selectedProyecto.linkCertificadoInhibiciones}
                                        target="_blank"
                                        rel="noreferrer"
                                        className="document-link"
                                    >
                                        Certificado de Inhibiciones
                                    </a>
                                )}

                            </div>
                        </div>

                        {!isFinalState && (
                            <div className="decision-block">

                                <div className="decision-input-row">

                                    <textarea
                                        className="decision-textarea"
                                        placeholder="Escribí el motivo de la decisión..."
                                        value={decisionMessage}
                                        onChange={(e) => setDecisionMessage(e.target.value)}
                                    />

                                    <div className="decision-buttons">

                                        <button
                                            className="btn-rectify"
                                            onClick={solicitarRectificacion}
                                            disabled={sendingDecision}
                                        >
                                            Solicitar Rectificación
                                        </button>

                                    </div>
                                </div>

                                <div className="modal-actions">

                                    <button className="btn-approve">
                                        Aprobar
                                    </button>

                                    <button className="btn-reject">
                                        Rechazar
                                    </button>

                                </div>
                            </div>
                        )}
                        {isFinalState && (
                            <div className="modal-actions">
                                <div className={`status-pill ${selectedProyecto.estado}`}>
                                    {selectedProyecto.estado === "aprobado"
                                        ? "Proyecto aprobado"
                                        : "Proyecto rechazado"}
                                </div>
                            </div>
                        )}

                    </div>
                </div>
            )}
        </div>
    );
}