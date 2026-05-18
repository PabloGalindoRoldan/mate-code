import { useState, useEffect, useRef } from "react";
import { useAuth } from "../../context/AuthContext";
import { mensajeriaApi } from "../../api/axios";
import API from "../../api/axios";
import { Send, User, Megaphone } from "lucide-react";
import "./MensajeriaPanel.css";

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
    const [loading, setLoading] = useState(false);

    const mensajesEndRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        cargarConversaciones();
        cargarContactosDisponibles();
    }, []);

    useEffect(() => {
        const interval = setInterval(() => {
            cargarConversaciones();
            if (chatActivo) {
                actualizarHistorialSilencioso(chatActivo);
            }
        }, 5000);

        return () => clearInterval(interval);
    }, [chatActivo]);

    const cargarConversaciones = async () => {
        try {
            const data = await mensajeriaApi.getConversaciones() as Conversacion[];

            // Mapeamos primero para guardar el orden original en el que vinieron desde el backend
            const conIndice = data.map((item, index) => ({ item, index }));

            conIndice.sort((a, b) => {
                const fechaA = new Date(a.item.fechaUltimoMensaje).getTime();
                const fechaB = new Date(b.item.fechaUltimoMensaje).getTime();

                // Si las fechas son distintas, ordenamos por fecha (descendiente)
                if (fechaB !== fechaA) {
                    return fechaB - fechaA;
                }

                // SI LAS FECHAS SON IGUALES: Desempatamos.
                // Si el backend te trae lo más viejo primero, acá invertimos usando el índice original
                return b.index - a.index;
            });

            // Volvemos a extraer solo el objeto de la conversación
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
            // PUNTO 1 (REVERTIDO): No invertimos el array. data viene en orden cronológico (viejos arriba).
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
            // PUNTO 1 (REVERTIDO): No invertimos el array.
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

    const handleEnviar = async (e: React.SubmitEvent<HTMLFormElement>) => {
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
            <div className="mensajeria-sidebar">
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
                    {conversaciones.length === 0 ? (
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

                                        {/* PUNTO 2: Indicador visual (puntito rojo) si tiene mensajes sin leer */}
                                        {c.mensajesSinLeer > 0 && <span className="unread-dot" title={`${c.mensajesSinLeer} sin leer`} />}
                                    </div>
                                    <p className="truncate">{c.ultimoMensaje}</p>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

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

                        <div className="chat-messages">
                            {loading && mensajes.length === 0 ? (
                                <p className="chat-state">Cargando mensajes...</p>
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

                            {/* PUNTO 1 (REVERTIDO): Marcador inferior para el scroll automático */}
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