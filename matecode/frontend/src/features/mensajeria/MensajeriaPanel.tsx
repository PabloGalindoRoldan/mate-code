import { useState, useEffect, useRef } from "react";
import { useAuth } from "../../context/AuthContext";
import { mensajeriaApi } from "../../api/axios";
import API from "../../api/axios";
import { Send, User, Megaphone } from "lucide-react";
import "./MensajeriaPanel.css";
import LoadingSpinner from "../../ui/loading/LoadingSpinner"; // Importamos tu nuevo componente

interface Conversacion {
    contactoUsername: string;
    contactoNombre: string;
    ultimoMensaje: string;
    fechaUltimoMensaje: string;
    mensajesSinLeer: number;
}

interface ContactoDisponible {
    nombreUsuario: string;
    nombre: string;
    apellido: string;
    rol?: string;
}

interface Mensaje {
    id?: number;
    emisorUsername: string;
    receptorUsername: string;
    contenido: string;
    fechaEnvio?: string;
}

export default function MensajeriaPanel() {
    const { user } = useAuth();
    const [conversaciones, setConversaciones] = useState<Conversacion[]>([]);
    const [contactosDisponibles, setContactosDisponibles] = useState<ContactoDisponible[]>([]);
    const [chatActivo, setChatActivo] = useState<string | null>(null);
    const [mensajes, setMensajes] = useState<Mensaje[]>([]);
    const [nuevoMensaje, setNuevoMensaje] = useState("");

    // Control de carga del panel lateral (conversaciones y contactos)
    const [sidebarLoading, setSidebarLoading] = useState(true);
    // Control de carga del historial del chat central
    const [loading, setLoading] = useState(false);

    const mensajesEndRef = useRef<HTMLDivElement>(null);

    // Carga inicial del panel
    useEffect(() => {
        const inicializarMensajeria = async () => {
            setSidebarLoading(true);
            try {
                // Ejecutamos ambas promesas en paralelo para acelerar el renderizado inicial
                await Promise.all([
                    cargarConversaciones(),
                    cargarContactosDisponibles()
                ]);
            } catch (error) {
                console.error("Error al inicializar mensajería", error);
            } finally {
                setSidebarLoading(false);
            }
        };

        inicializarMensajeria();
    }, []);

    // Pooling de actualización cada 5 segundos
    useEffect(() => {
        const interval = setInterval(() => {
            cargarConversaciones();
            if (chatActivo) {
                actualizarHistorialSilencioso(chatActivo);
            }
        }, 5000);

        return () => clearInterval(interval);
    }, [chatActivo]);

    // Auto-scroll al final del chat cuando entran nuevos mensajes
    useEffect(() => {
        if (!loading) {
            mensajesEndRef.current?.scrollIntoView({ behavior: "smooth" });
        }
    }, [mensajes, loading]);

    const cargarConversaciones = async () => {
        try {
            const data = await mensajeriaApi.getConversaciones() as Conversacion[];

            const conIndice = data.map((item, index) => ({ item, index }));

            conIndice.sort((a, b) => {
                const fechaA = new Date(a.item.fechaUltimoMensaje).getTime();
                const fechaB = new Date(b.item.fechaUltimoMensaje).getTime();

                if (fechaB !== fechaA) {
                    return fechaB - fechaA;
                }
                return b.index - a.index;
            });

            const conversacionesOrdenadas = conIndice.map(objeto => objeto.item);
            setConversaciones(conversacionesOrdenadas);
        } catch (error) {
            console.error("Error cargando conversaciones", error);
        }
    };

    const cargarContactosDisponibles = async () => {
        try {
            const response = await API.get("/api/mensajes/contactos-disponibles");
            setContactosDisponibles(response.data);
        } catch (error) {
            console.error("Error cargando contactos", error);
        }
    };

    const cargarHistorial = async (contactoUsername: string) => {
        setLoading(true);
        try {
            const data = await mensajeriaApi.getHistorial(contactoUsername);
            setMensajes(data);
        } catch (error) {
            console.error("Error cargando historial", error);
        } finally {
            setLoading(false);
        }
    };

    const actualizarHistorialSilencioso = async (contactoUsername: string) => {
        try {
            const data = await mensajeriaApi.getHistorial(contactoUsername);
            setMensajes(data);
        } catch (error) {
            console.error("Error en segundo plano", error);
        }
    };

    const handleSeleccionarChat = (contactoUsername: string) => {
        setChatActivo(contactoUsername);
        cargarHistorial(contactoUsername);
    };

    const handleIniciarNuevoChat = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const usernameSeleccionado = e.target.value;
        if (!usernameSeleccionado) return;

        setChatActivo(usernameSeleccionado);
        cargarHistorial(usernameSeleccionado);
        e.target.value = "";
    };

    const handleEnviar = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!nuevoMensaje.trim() || !chatActivo) return;

        const mensajeTemporal: Mensaje = {
            emisorUsername: user?.nombreUsuario || "",
            receptorUsername: chatActivo,
            contenido: nuevoMensaje.trim(),
        };

        setMensajes((prev) => [...prev, mensajeTemporal]);
        setNuevoMensaje("");

        try {
            await mensajeriaApi.enviarMensaje(chatActivo, mensajeTemporal.contenido);
            const historialActualizado = await mensajeriaApi.getHistorial(chatActivo);
            setMensajes(historialActualizado);
            cargarConversaciones();
        } catch (error) {
            console.error("Error al enviar mensaje", error);
        }
    };

    return (
        <div className="mensajeria-container">
            {/* --- SIDEBAR IZQUIERDO --- */}
            <div className="mensajeria-sidebar" style={{ position: "relative" }}>
                <h3>Mensajería interna</h3>

                <div className="nuevo-chat-selector">
                    <select onChange={handleIniciarNuevoChat} defaultValue="">
                        <option value="" disabled>Iniciar chat con...</option>
                        <option value="TODOS">📢 Mensaje a TODOS (Difusión)</option>
                        {contactosDisponibles.map(cont => (
                            <option key={cont.nombreUsuario} value={cont.nombreUsuario}>
                                {cont.nombre} {cont.apellido}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="conversaciones-list">
                    {sidebarLoading ? (
                        <div style={{ padding: "40px 0" }}>
                            <LoadingSpinner text="Cargando contactos..." />
                        </div>
                    ) : conversaciones.length === 0 ? (
                        <p className="no-chats">No hay chats iniciados. Elige un contacto arriba.</p>
                    ) : (
                        conversaciones.map((c) => (
                            <div
                                key={c.contactoUsername}
                                className={`conversacion-item ${chatActivo === c.contactoUsername ? "active" : ""}`}
                                onClick={() => handleSeleccionarChat(c.contactoUsername)}
                            >
                                <div className="avatar">
                                    {c.contactoUsername === "TODOS" ? <Megaphone size={18} /> : <User size={18} />}
                                </div>
                                <div className="info">
                                    <div className="info-header">
                                        <h4>{c.contactoUsername === "TODOS" ? "Difusión General" : (c.contactoNombre || c.contactoUsername)}</h4>
                                        {c.mensajesSinLeer > 0 && <span className="unread-dot" title={`${c.mensajesSinLeer} sin leer`} />}
                                    </div>
                                    <p className="truncate">{c.ultimoMensaje}</p>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            {/* --- CUERPO CENTRAL DE LA VENTANA DE CHAT --- */}
            <div className="chat-window">
                {chatActivo ? (
                    <>
                        <div className="chat-header">
                            {chatActivo === "TODOS" ? <Megaphone size={20} /> : <User size={20} />}
                            <h3>
                                {chatActivo === "TODOS"
                                    ? "Canal de Difusión General"
                                    : (conversaciones.find(c => c.contactoUsername === chatActivo)?.contactoNombre || chatActivo)
                                }
                            </h3>
                        </div>

                        <div className="chat-messages" style={{ position: "relative" }}>
                            {loading ? (
                                <div style={{
                                    position: "absolute", top: 0, left: 0, width: "100%", height: "100%",
                                    display: "flex", alignItems: "center", justifyContent: "center",
                                    backgroundColor: "rgba(255, 255, 255, 0.7)"
                                }}>
                                    <LoadingSpinner text="Abriendo conversación..." />
                                </div>
                            ) : (
                                mensajes.map((m, idx) => {
                                    const esMio = m.emisorUsername === user?.nombreUsuario;
                                    return (
                                        <div key={idx} className={`message-bubble ${esMio ? "mine" : "theirs"}`}>
                                            <span className="sender-tag">{esMio ? "Tú" : m.emisorUsername}</span>
                                            <p className="message-content">{m.contenido}</p>
                                        </div>
                                    );
                                })
                            )}
                            <div ref={mensajesEndRef} />
                        </div>

                        <form className="chat-input-area" onSubmit={handleEnviar}>
                            <input
                                type="text"
                                placeholder={chatActivo === "TODOS" ? "Escribe un comunicado para todo el parque..." : "Escribe un mensaje..."}
                                value={nuevoMensaje}
                                onChange={(e) => setNuevoMensaje(e.target.value)}
                                required
                            />
                            <button type="submit" aria-label="Enviar">
                                <Send size={18} />
                            </button>
                        </form>
                    </>
                ) : (
                    <div className="chat-empty-state">
                        <p>Selecciona un contacto o el canal general para ver los mensajes.</p>
                    </div>
                )}
            </div>
        </div>
    );
}