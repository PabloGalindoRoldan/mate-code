import { useState, useEffect } from "react";
import { Plus, Trash2, Calendar, Image, FileText } from "lucide-react";
import API from '../../api/axios';
import "./PublicacionesPanel.css";

interface Publicacion {
    id: number;
    titulo: string;
    imagen: string;
    alt: string;
    contenido: string;
    fechaCreacion?: string;
}

export default function PublicacionesPanel() {
    const [publicaciones, setPublicaciones] = useState<Publicacion[]>([]);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [loading, setLoading] = useState(true);

    // Form States
    const [titulo, setTitulo] = useState("");
    const [imagen, setImagen] = useState("");
    const [alt, setAlt] = useState("");
    const [contenido, setContenido] = useState("");
    const [error, setError] = useState("");

    const fetchPublicaciones = async () => {
        try {
            const res = await API.get<Publicacion[]>('/api/publicaciones');
            setPublicaciones(res.data);
        } catch (err) {
            console.error("Error fetching data:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPublicaciones();
    }, []);

    const handleOpenForm = () => {
        setIsFormOpen(true);
        setError("");
    };

    const handleCloseForm = () => {
        setIsFormOpen(false);
        setTitulo("");
        setImagen("");
        setAlt("");
        setContenido("");
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!titulo.trim() || !contenido.trim()) {
            setError("El título y el contenido son obligatorios.");
            return;
        }

        const payload = {
            titulo: titulo.trim(),
            imagen: imagen?.trim() || "https://images.pexels.com/photos/29207335/pexels-photo-29207335.jpeg",
            alt: alt.trim() || `Imagen de ${titulo}`,
            contenido: contenido.trim()
        };

        try {
            // Interceptor handles the sessionStorage token implicitly here
            const res = await API.post<Publicacion>('/api/publicaciones', payload);
            setPublicaciones([res.data, ...publicaciones]);
            handleCloseForm();
        } catch (err: any) {
            // Fallback checking for custom backend error structure or standard status message
            const serverMsg = err.response?.data?.message || "No se pudo guardar la publicación. Verifica permisos.";
            setError(serverMsg);
        }
    };

    const handleDelete = async (id: number) => {
        if (!window.confirm("¿Estás seguro de que deseas eliminar esta publicación?")) return;

        try {
            await API.delete(`/api/publicaciones/${id}`);
            setPublicaciones(publicaciones.filter(pub => pub.id !== id));
        } catch (err) {
            alert("Error al intentar eliminar la publicación. Verifica tus credenciales.");
        }
    };

    const formatFecha = (fechaStr?: any) => {
        if (!fechaStr) return "Reciente";
        if (Array.isArray(fechaStr)) {
            return `${fechaStr[2]}/${fechaStr[1]}/${fechaStr[0]}`;
        }
        return new Date(fechaStr).toLocaleDateString("es-AR");
    };

    if (loading) return <div style={{ padding: "20px" }}>Cargando panel de gestión...</div>;

    return (
        <div className="publicacionesPanel">
            {!isFormOpen ? (
                <>
                    <div className="panelHeader">
                        <div>
                            <h2>Gestión de Publicaciones</h2>
                            <p className="panelSubtitle">Crea y administra las novedades del parque que se muestran en el Inicio.</p>
                        </div>
                        <button className="btnNuevaPublicacion" onClick={handleOpenForm}>
                            <Plus size={18} /> Nueva Publicación
                        </button>
                    </div>

                    <div className="tableWrapper">
                        <table className="publicacionesTable">
                            <thead>
                                <tr>
                                    <th>Título</th>
                                    <th>Contenido Resumido</th>
                                    <th>Fecha</th>
                                    <th className="actionsColumnHeader">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {publicaciones.length === 0 ? (
                                    <tr>
                                        <td colSpan={4} className="emptyTableMessage">
                                            No hay publicaciones creadas todavía.
                                        </td>
                                    </tr>
                                ) : (
                                    publicaciones.map((pub) => (
                                        <tr key={pub.id}>
                                            <td className="pubTitleCell"><strong>{pub.titulo}</strong></td>
                                            <td className="pubTruncatedCell">{pub.contenido}</td>
                                            <td className="pubDateCell">
                                                <div className="dateBadge">
                                                    <Calendar size={14} /> {formatFecha(pub.fechaCreacion)}
                                                </div>
                                            </td>
                                            <td className="pubActionsCell">
                                                <button
                                                    className="btnActionDelete"
                                                    onClick={() => handleDelete(pub.id)}
                                                    title="Eliminar publicación"
                                                >
                                                    <Trash2 size={16} />
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </>
            ) : (
                <div className="formViewContainer">
                    <div className="formHeader">
                        <h3>Crear Nuevo Comunicado</h3>
                        <p>Completa el formulario para publicar una tarjeta informativa en la Landing.</p>
                    </div>

                    <form onSubmit={handleSubmit} className="panelForm">
                        {error && <div className="formErrorMessage">{error}</div>}

                        <div className="panelFormGroup">
                            <label><FileText size={16} /> Título del Comunicado *</label>
                            <input
                                type="text"
                                value={titulo}
                                onChange={(e) => setTitulo(e.target.value)}
                                placeholder="Ej: Corte de calle por obras en Av. Principal"
                            />
                        </div>

                        <div className="formRowGrid">
                            <div className="panelFormGroup">
                                <label><Image size={16} /> URL de la Imagen</label>
                                <input
                                    type="url"
                                    value={imagen}
                                    onChange={(e) => setImagen(e.target.value)}
                                    placeholder="https://images.pexels.com/..."
                                />
                            </div>

                            <div className="panelFormGroup">
                                <label>Texto descriptivo de imagen (Alt)</label>
                                <input
                                    type="text"
                                    value={alt}
                                    onChange={(e) => setAlt(e.target.value)}
                                    placeholder="Ej: Maquinaria trabajando en calle"
                                />
                            </div>
                        </div>

                        <div className="panelFormGroup">
                            <label>Cuerpo / Contenido del Mensaje *</label>
                            <textarea
                                value={contenido}
                                onChange={(e) => setContenido(e.target.value)}
                                rows={6}
                                placeholder="Escribe de manera detallada las novedades que las empresas y visitantes necesitan saber..."
                            />
                        </div>

                        <div className="formActionsBar">
                            <button type="button" className="btnFormCancel" onClick={handleCloseForm}>
                                Cancelar
                            </button>
                            <button type="submit" className="btnFormSubmit">
                                Publicar en Inicio
                            </button>
                        </div>
                    </form>
                </div>
            )}
        </div>
    );
}