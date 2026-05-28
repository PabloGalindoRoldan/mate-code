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
import { proyectosApi, mensajeriaApi, empresasApi, lotesApi } from "../../api/axios";

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
    const [selectedProyecto, setSelectedProyecto] = useState<Proyecto | null>(null);
    const [search, setSearch] = useState("");

    // Estados para rectificación
    const [decisionMessage, setDecisionMessage] = useState("");
    const [sendingDecision, setSendingDecision] = useState(false);

    // Estados para la aprobación
    const [showApproveForm, setShowApproveForm] = useState(false);
    const [lotesDisponibles, setLotesDisponibles] = useState<any[]>([]);
    const [loadingLotes, setLoadingLotes] = useState(false);
    const [selectedLote, setSelectedLote] = useState<number | "">("");
    const [sellingPrice, setSellingPrice] = useState<string>("");

    const isFinalState =
        selectedProyecto?.estado === "aprobado" ||
        selectedProyecto?.estado === "rechazado";

    useEffect(() => {
        cargarProyectos();
    }, []);

    // Reiniciar los formularios si se cierra el modal
    useEffect(() => {
        if (!selectedProyecto) {
            setShowApproveForm(false);
            setSelectedLote("");
            setSellingPrice("");
            setDecisionMessage("");
        }
    }, [selectedProyecto]);

    const cargarProyectos = async () => {
        try {
            setLoading(true);
            const response: ProyectosResponse = await proyectosApi.listarProyectos();

            const preliminares = (response.preliminares || []).map(p => ({
                ...p,
                tipoProyecto: "Preliminar" as const,
            }));

            const definitivos = (response.definitivos || []).map(p => ({
                ...p,
                tipoProyecto: "Definitivo" as const,
            }));

            setProyectos([...preliminares, ...definitivos]);
        } catch (error) {
            console.error("Error cargando proyectos", error);
        } finally {
            setLoading(false);
        }
    };

    const cargarLotes = async () => {
        try {
            setLoadingLotes(true);
            // 'response' is already your GeoJSON object data payload
            const response = await lotesApi.getMapaLotes();

            // Extract the features array directly from the GeoJSON root
            setLotesDisponibles(response.features || []);
        } catch (error) {
            console.error("Error cargando lotes", error);
        } finally {
            setLoadingLotes(false);
        }
    };

    useEffect(() => {
        if (showApproveForm) {
            cargarLotes();
        }
    }, [showApproveForm]);

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
            await mensajeriaApi.enviarMensaje(selectedProyecto.usuarioNombre, decisionMessage);
            await cambiarEstado("RECTIFICAR");

            setProyectos(prev =>
                prev.map(p =>
                    p.id === selectedProyecto.id ? { ...p, estado: "rectificar" } : p
                )
            );

            setSelectedProyecto(null);
        } catch (error) {
            console.error("Error al solicitar rectificación:", error);
        } finally {
            setSendingDecision(false);
        }
    };

    const confirmarAprobacion = async () => {
        if (!selectedProyecto) return;

        try {
            setSendingDecision(true);

            if (selectedProyecto.tipoProyecto === "Preliminar") {
                if (!selectedLote) return;

                // 1. Asignar el lote a la empresa
                await empresasApi.asignarLote(selectedProyecto.cuitEmpresa, Number(selectedLote));
                // 2. Cambiar el estado del proyecto
                await cambiarEstado("aprobado");

            } else if (selectedProyecto.tipoProyecto === "Definitivo") {
                if (!sellingPrice) return;

                // TODO: Aquí deberías enviar el sellingPrice a tu backend si tienes un endpoint específico para registrar la venta.
                // await proyectosApi.registrarPrecioVenta({ proyectoId: selectedProyecto.id, precio: sellingPrice });

                // 1. Actualizar estado de radicación a true
                await empresasApi.actualizarEstadoRadicacion(selectedProyecto.cuitEmpresa, true);
                // 2. Cambiar el estado del proyecto
                await cambiarEstado("aprobado");
            }

            // Actualizar la UI
            setProyectos(prev =>
                prev.map(p =>
                    p.id === selectedProyecto.id ? { ...p, estado: "aprobado" } : p
                )
            );

            setSelectedProyecto(null);

        } catch (error) {
            console.error("Error al confirmar aprobación:", error);
        } finally {
            setSendingDecision(false);
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
        preliminaresEn_revision: filtered.filter(
            (p: any) => p.tipoProyecto === "Preliminar" && p.estado === "en_revision"
        ),
        definitivosEn_revision: filtered.filter(
            (p: any) => p.tipoProyecto === "Definitivo" && p.estado === "en_revision"
        ),
        preliminaresRectificar: filtered.filter(
            (p: any) => p.tipoProyecto === "Preliminar" && p.estado === "rectificar"
        ),
        definitivosRectificar: filtered.filter(
            (p: any) => p.tipoProyecto === "Definitivo" && p.estado === "rectificar"
        ),
        aprobados: filtered.filter((p) => p.estado === "aprobado"),
        rechazados: filtered.filter((p) => p.estado === "rechazado"),
    };

    const renderSection = (title: string, icon: React.ReactNode, projects: Proyecto[], className: string) => (
        <section className="proyectos-section">
            <div className="section-header">
                <div className={`section-icon ${className}`}>{icon}</div>
                <div>
                    <h3>{title}</h3>
                    <span>{projects.length} proyectos</span>
                </div>
            </div>

            <div className="projects-grid">
                {projects.length === 0 && (
                    <div className="empty-projects">No hay proyectos en esta categoría.</div>
                )}
                {projects.map((proyecto: any) => (
                    <div key={`${proyecto.tipoProyecto}-${proyecto.id}`} className="project-card">
                        <div className="project-card-top">
                            <div>
                                <h4>{proyecto.nombre}</h4>
                                <span className="project-type">{proyecto.tipoProyecto}</span>
                            </div>
                            <button
                                className="btn-view"
                                onClick={() => setSelectedProyecto(proyecto)}
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
                                <span>{proyecto.actividadPrincipal || "Sin actividad"}</span>
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
                    <p>Administra proyectos preliminares y definitivos del parque industrial.</p>
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
                <div className="loading-projects">Cargando proyectos...</div>
            ) : (
                <>
                    {renderSection("Preliminares en Revisión", <Clock3 />, grouped.preliminaresEn_revision, "en_revision")}
                    {renderSection("Definitivos en Revisión", <Clock3 />, grouped.definitivosEn_revision, "en_revision")}
                    {renderSection("Preliminares a Rectificar", <AlertTriangle />, grouped.preliminaresRectificar, "rectificar")}
                    {renderSection("Definitivos a Rectificar", <AlertTriangle />, grouped.definitivosRectificar, "rectificar")}
                    {renderSection("Proyectos Aprobados", <CheckCircle2 />, grouped.aprobados, "approved")}
                    {renderSection("Proyectos Rechazados", <XCircle />, grouped.rechazados, "rejected")}
                </>
            )}

            {selectedProyecto && (
                <div className="modal-overlay" onClick={() => setSelectedProyecto(null)}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="close-modal" onClick={() => setSelectedProyecto(null)}>
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

                        {/* Campos del Modal Omitidos para brevedad - Mantenelos igual que antes */}
                        <div className="modal-grid">
                            <div className="modal-field"><label>Usuario</label><p>{selectedProyecto.usuarioNombre}</p></div>
                            <div className="modal-field"><label>CUIT</label><p>{selectedProyecto.cuitEmpresa}</p></div>
                            <div className="modal-field full-width"><label>Descripción</label><p>{selectedProyecto.descripcion || "-"}</p></div>
                            {/* ... Resto de tus campos ... */}
                        </div>

                        {!isFinalState && !showApproveForm && (
                            <div className="decision-block">
                                <div className="decision-input-row">
                                    <textarea
                                        className="decision-textarea"
                                        placeholder="Escribí el motivo de la decisión (para rectificar o rechazar)..."
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
                                    <button
                                        className="btn-approve"
                                        onClick={() => {
                                            setShowApproveForm(true);
                                            if (selectedProyecto.tipoProyecto === "Preliminar") {
                                                cargarLotes();
                                            }
                                        }}
                                    >
                                        Aprobar
                                    </button>
                                    <button className="btn-reject">
                                        Rechazar
                                    </button>
                                </div>
                            </div>
                        )}

                        {showApproveForm && (
                            <div className="decision-block approve-form">
                                <h3 style={{ marginBottom: "1rem", borderBottom: "1px solid #eee", paddingBottom: "0.5rem" }}>
                                    Confirmar Aprobación
                                </h3>

                                {selectedProyecto.tipoProyecto === "Preliminar" ? (
                                    <div className="modal-field full-width" style={{ marginBottom: "1rem" }}>
                                        <label>Asignar Lote (Quedará Reservado)</label>
                                        <select
                                            value={selectedLote}
                                            onChange={(e) => setSelectedLote(Number(e.target.value))}
                                            disabled={loadingLotes}
                                        >
                                            <option value="">--- Seleccionar Lote Asignable ---</option>
                                            {lotesDisponibles?.map((feature: any) => (
                                                <option key={feature.properties.lote} value={feature.properties.lote}>
                                                    Lote {feature.id} ({feature.properties.sup} m²) - {feature.properties.estado}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                ) : (
                                    <div className="modal-field full-width" style={{ marginBottom: "1rem" }}>
                                        <label>Precio de Venta ($)</label>
                                        <input
                                            type="number"
                                            placeholder="Ingrese el precio acordado..."
                                            value={sellingPrice}
                                            onChange={(e) => setSellingPrice(e.target.value)}
                                            style={{ width: "100%", padding: "0.5rem", marginTop: "0.5rem", borderRadius: "4px" }}
                                        />
                                    </div>
                                )}

                                <div className="modal-actions">
                                    <button
                                        className="btn-approve"
                                        onClick={confirmarAprobacion}
                                        disabled={
                                            sendingDecision ||
                                            (selectedProyecto.tipoProyecto === "Preliminar" ? !selectedLote : !sellingPrice)
                                        }
                                    >
                                        Confirmar
                                    </button>
                                    <button
                                        className="btn-reject"
                                        onClick={() => setShowApproveForm(false)}
                                        disabled={sendingDecision}
                                    >
                                        Cancelar
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