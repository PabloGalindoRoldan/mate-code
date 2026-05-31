import "./EmpresaNoRadicadaView.css";
import { useState, useEffect } from "react";
import { mensajeriaApi, proyectosApi } from "../../api/axios";
import { useAuth } from "../../context/AuthContext";
import { Save, Send, FileText } from "lucide-react";
import { toast } from 'sonner';


interface Props {
    isRectifying: boolean;
    onProyectoEnviado: () => void;
    proyectoExistente?: any;
}

interface FormDataType {
    nombre: string;
    descripcion: string;
    actividadPrincipal: string;
    actividadSecundaria: string;
    telefono: string;
    rubro: string;
    descripcionServicio: string;
    personaReferente: string;
    materiasPrimas: string;
    destinoProduccion: string;

    superficieRequerida: number;
    superficieTrabajo: number;
    superficieDeposito: number;
    superficieCubierta: number;
    superficieEstacionamiento: number;

    tienePlanos: string;
    linkPlanos: string;

    energiaRequerida: number;
    personalAOcupar: number;
    tensionAlimentacion: string;

    potenciaInstalada: number;
    aguaMensual: number;
    gasMensual: number;

    residuosTipo: string;
    residuosCantidad: number;

    tratamientoEfluentes: string;
    tipoEmpresa: string;

    direccion: string;
    pretensionTraslado: string;
    emplazamientoActual: string;
    tiempoRadicacion: string;

    balanzaPublica: string;
    comedor: string;
    sumCoworking: string;
}

export default function FormularioPreliminar({ isRectifying, onProyectoEnviado, proyectoExistente }: Props) {

    const { user } = useAuth();
    const [enviado, setEnviado] = useState(false);
    const [rectify] = useState(isRectifying)

    const parseNumeric = (value: any): number => {
        if (typeof value === "object" && value !== null) {
            return Number(value.parsedValue ?? 0);
        }

        return Number(value ?? 0);
    };

    const [formData, setFormData] = useState<FormDataType>({
        nombre: "",
        descripcion: "",
        actividadPrincipal: "",
        actividadSecundaria: "",
        telefono: "",
        rubro: "Bienes",
        descripcionServicio: "",
        personaReferente: "",
        materiasPrimas: "",
        destinoProduccion: "",
        superficieRequerida: 0,
        superficieTrabajo: 0,
        superficieDeposito: 0,
        superficieCubierta: 0,
        superficieEstacionamiento: 0,
        tienePlanos: "no",
        linkPlanos: "",
        energiaRequerida: 0,
        personalAOcupar: 0,
        tensionAlimentacion: "baja",
        potenciaInstalada: 0,
        aguaMensual: 0,
        gasMensual: 0,
        residuosTipo: "",
        residuosCantidad: 0,
        tratamientoEfluentes: "no",
        tipoEmpresa: "nueva",
        direccion: "",
        pretensionTraslado: "",
        emplazamientoActual: "",
        tiempoRadicacion: "",
        balanzaPublica: "no",
        comedor: "no",
        sumCoworking: "no"
    });

    //Normalizador para que mapee el json del back:

    const mapProyectoToForm = (p: any): FormDataType => ({
        nombre: p.nombre || "",
        descripcion: p.descripcion || "",
        actividadPrincipal: p.actividadPrincipal || "",
        actividadSecundaria: p.actividadSecundaria || "",
        telefono: p.telefono || "",
        rubro: p.rubro || "Bienes",
        descripcionServicio: p.descripcionServicio || "",
        personaReferente: p.personaReferente || "",
        materiasPrimas: p.materiasPrimas || "",
        destinoProduccion: p.destinoProduccion || "",

        superficieRequerida: parseNumeric(p.superficieRequerida),
        superficieTrabajo: parseNumeric(p.superficieTrabajo),
        superficieDeposito: parseNumeric(p.superficieDeposito),
        superficieCubierta: parseNumeric(p.superficieCubierta),
        superficieEstacionamiento: parseNumeric(p.superficieEstacionamiento),
        energiaRequerida: parseNumeric(p.energiaRequerida),
        potenciaInstalada: parseNumeric(p.potenciaInstalada),
        aguaMensual: parseNumeric(p.aguaMensual),
        gasMensual: parseNumeric(p.gasMensual),
        residuosCantidad: parseNumeric(p.residuosCantidad),
        tienePlanos: p.tienePlanos || "no",
        linkPlanos: p.linkPlanos || "",
        personalAOcupar: Number(p.personalAOcupar ?? 0),
        tensionAlimentacion: p.tensionAlimentacion || "baja",
        residuosTipo: p.residuosTipo || "",
        tratamientoEfluentes: p.tratamientoEfluentes || "no",
        tipoEmpresa: p.tipoEmpresa || "nueva",
        direccion: p.direccion || "",
        pretensionTraslado: p.pretensionTraslado || "",
        emplazamientoActual: p.emplazamientoActual || "Propio",
        tiempoRadicacion: p.tiempoRadicacion || "6 meses",
        balanzaPublica: p.balanzaPublica || "no",
        comedor: p.comedor || "no",
        sumCoworking: p.sumCoworking || "no"
    });



    useEffect(() => {
        if (isRectifying && proyectoExistente) {
            setFormData(mapProyectoToForm(proyectoExistente));
            console.log("PROYECTO:", proyectoExistente);
            console.log("AGUA:", proyectoExistente?.aguaMensual);
            console.log("AGUA PARSED:", proyectoExistente?.aguaMensual?.parsedValue);
            console.log("PERSONAL:", proyectoExistente?.personalAOcupar);
            return;
        }

        if (!user?.nombreUsuario) return;

        const draft = localStorage.getItem(
            "draft_proyecto_" + user.nombreUsuario
        );

        if (draft) {
            try {
                setFormData(JSON.parse(draft));
            } catch (e) {
                console.error("Error al parsear borrador", e);
            }
        }

    }, [user, isRectifying, proyectoExistente]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'number'
                ? value === '' ? 0 : Number(value)
                : value
        }));
    };

    const handleSaveDraft = () => {
        localStorage.setItem("draft_proyecto_" + user?.nombreUsuario, JSON.stringify(formData));
        toast.success("Borrador guardado localmente.");
    };

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();

        // 1. Validación de campos críticos
        const obligatorios = ['nombre', 'actividadPrincipal', 'personaReferente', 'superficieRequerida', 'personalAOcupar', 'potenciaInstalada'];
        const faltantes = obligatorios.filter(f => !formData[f as keyof typeof formData]);

        if (faltantes.length > 0) {
            toast.warning("Debe completar los campos críticos: " + faltantes.join(", "));
            return;
        }

        // Validación extra de seguridad para el CUIT
        const cuit = user?.empresa?.cuit;
        if (!cuit) {
            toast.error("Error: No se pudo identificar el CUIT de su empresa. Por favor, contacte al administrador.");
            return;
        }

        try {
            // 3. Llamada a la API
            if (isRectifying && proyectoExistente?.id) {

                const payload = {
                    ...formData,
                    usuarioNombre: user?.nombreUsuario || "Anonimo",
                    cuitEmpresa: user?.empresa?.cuit,
                    id: proyectoExistente.id
                };

                await proyectosApi.actualizarPreliminar(payload);

            } else {

                const payload = {
                    ...formData,
                    usuarioNombre: user?.nombreUsuario || "Anonimo",
                    cuitEmpresa: user?.empresa?.cuit
                };

                await proyectosApi.crearProyecto(payload);
            }

            // 4. Notificación
            await mensajeriaApi.enviarMensaje(
                user?.nombreUsuario || "",
                `Sistema: Proyecto preliminar "${formData.nombre}" enviado exitosamente para evaluación.`
            );

            setEnviado(true);
            toast.success("¡Proyecto enviado correctamente!");
            console.log(enviado)

            // 5. Limpiar borrador
            localStorage.removeItem("draft_proyecto_" + user?.nombreUsuario);
            await onProyectoEnviado();

        } catch (error) {
            console.error("Error al enviar proyecto:", error);
            toast.error("Hubo un error al procesar el proyecto. Por favor, intente nuevamente.");
        }
    };

    return (
        <div>
            <div className="empresaNoRadicadaBody">
                {rectify && <h2 className="mensaje-rectificar">Su proyecto debe ser rectificado. Consulte sus mensajes para ver las observaciones efectuadas.</h2>}
                <div className="formulario-container">
                    <header className="form-header">
                        <FileText size={32} />
                        <div>
                            <h1>Presentar Proyecto Preliminar</h1>
                            <p>Complete los datos técnicos para iniciar el proceso de evaluación.</p>
                        </div>
                    </header>

                    <form className="project-form-grid" onSubmit={handleSubmit}>

                        {/* SECCIÓN INICIAL */}
                        <fieldset className="form-section">
                            <legend>Identificación y Localización</legend>
                            <div className="input-group full-width">
                                <label>Nombre del Proyecto *</label>
                                <input type="text" name="nombre" value={formData.nombre} onChange={handleInputChange} required />
                            </div>

                            <div className="input-group">
                                <label>Persona Referente / Responsable *</label>
                                <input type="text" name="personaReferente" value={formData.personaReferente} onChange={handleInputChange} required />
                            </div>
                            <div className="input-group">
                                <label>Teléfono de Contacto</label>
                                <input type="text" name="telefono" value={formData.telefono} onChange={handleInputChange} />
                            </div>
                            <div className="input-group">
                                <label>Actividad Principal *</label>
                                <input type="text" name="actividadPrincipal" value={formData.actividadPrincipal} onChange={handleInputChange} required />
                            </div>
                            <div className="input-group">
                                <label>Actividad Secundaria </label>
                                <input type="text" name="actividadSecundaria" value={formData.actividadSecundaria} onChange={handleInputChange} />
                            </div>
                            <div className="input-group">
                                <label>Rubro</label>
                                <select name="rubro" value={formData.rubro} onChange={handleInputChange}>
                                    <option value="Servicios">Servicios</option>
                                    <option value="Bienes">Bienes</option>
                                    <option value="Ambos">Bienes y Servicios</option>
                                    <option value="Otros">Otros</option>
                                </select>
                            </div>
                            <div className="input-group full-width">
                                <label>Descripción del Proyecto</label>
                                <textarea name="descripcion" value={formData.descripcion} onChange={handleInputChange} />
                            </div>
                            <div className="input-group">
                                <label>Destino de la producción </label>
                                <input type="text" name="destinoProduccion" value={formData.destinoProduccion} onChange={handleInputChange} />
                            </div>
                        </fieldset>

                        {/* SECCIÓN SUPERFICIE */}
                        <fieldset className="form-section">
                            <legend>Superficie Requerida (M²)</legend>
                            <div className="grid-2-col">
                                <div className="input-group">
                                    <label>Total Requerida *</label>
                                    <input type="number" name="superficieRequerida" min="0" value={formData.superficieRequerida} onChange={handleInputChange} required />
                                </div>
                                <div className="input-group">
                                    <label>Áreas de Trabajo</label>
                                    <input type="number" name="superficieTrabajo" min="0" value={formData.superficieTrabajo} onChange={handleInputChange} />
                                </div>
                                {/* Campos Nuevos: Superficies */}
                                <div className="input-group">
                                    <label>Superficie Depósito</label>
                                    <input type="number" name="superficieDeposito" min="0" value={formData.superficieDeposito} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>Superficie Estacionamiento</label>
                                    <input type="number" name="superficieEstacionamiento" min="0" value={formData.superficieEstacionamiento} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>Cubierta Estimada</label>
                                    <input type="number" name="superficieCubierta" min="0" value={formData.superficieCubierta} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>¿Tiene Planos?</label>
                                    <select name="tienePlanos" value={formData.tienePlanos} onChange={handleInputChange}>
                                        <option value="si">Sí</option>
                                        <option value="no">No</option>
                                    </select>
                                </div>
                                {formData.tienePlanos === "si" && (
                                    <div className="input-group full-width">
                                        <label>Enlace a Google Drive con planos (asegúrate de que sea público)</label>
                                        <input
                                            type="url"
                                            name="linkPlanos"
                                            placeholder="https://drive.google.com/..."
                                            value={formData.linkPlanos}
                                            onChange={handleInputChange}
                                        />
                                    </div>
                                )}
                                {/* {subseccion} */}
                                <div className="input-group">
                                    <label>¿Requiere Balanza Publica?</label>
                                    <select name="balanzaPublica" value={formData.balanzaPublica} onChange={handleInputChange}>
                                        <option value="si">Sí</option>
                                        <option value="no">No</option>
                                    </select>
                                </div>
                                <div className="input-group">
                                    <label>¿Requiere Comedor Comunitario?</label>
                                    <select name="comedor" value={formData.comedor} onChange={handleInputChange}>
                                        <option value="si">Sí</option>
                                        <option value="no">No</option>
                                    </select>
                                </div>
                                <div className="input-group">
                                    <label>¿Requiere SUM o Coworking?</label>
                                    <select name="sumCoworking" value={formData.sumCoworking} onChange={handleInputChange}>
                                        <option value="si">Sí</option>
                                        <option value="no">No</option>
                                    </select>
                                </div>
                            </div>
                        </fieldset>

                        {/* SECCIÓN CONSUMOS */}
                        <fieldset className="form-section">
                            <legend>Consumos y Potencia estimados</legend>
                            <div className="grid-2-col">
                                <div className="input-group">
                                    <label>Consumo Electrico (KW/mes) *</label>
                                    <input type="number" name="potenciaInstalada" min="0" value={formData.potenciaInstalada} onChange={handleInputChange} required />
                                </div>
                                <div className="input-group">
                                    <label>Tensión</label>
                                    <select name="tensionAlimentacion" value={formData.tensionAlimentacion} onChange={handleInputChange}>
                                        <option value="baja">Baja Tensión</option>
                                        <option value="media">Media Tensión</option>
                                    </select>
                                </div>
                                <div className="input-group">
                                    <label>Personal a Ocupar *</label>
                                    <input type="number" name="personalAOcupar" min="0" value={formData.personalAOcupar} onChange={handleInputChange} required />
                                </div>
                                <div className="input-group">
                                    <label>Agua (M³/mes)</label>
                                    <input type="number" name="aguaMensual" min="0" value={formData.aguaMensual} onChange={handleInputChange} />
                                </div>
                                {/* Campos Nuevos: Consumos y Residuos */}
                                <div className="input-group">
                                    <label>Gas (M³/mes)</label>
                                    <input type="number" name="gasMensual" min="0" value={formData.gasMensual} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>Materias Primas a utilizar</label>
                                    <input type="text" name="materiasPrimas" value={formData.materiasPrimas} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>Tipo de Residuos</label>
                                    <input type="text" name="residuosTipo" value={formData.residuosTipo} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>Cantidad Residuos (Kg/mes)</label>
                                    <input type="number" name="residuosCantidad" min="0" value={formData.residuosCantidad} onChange={handleInputChange} />
                                </div>
                                <div className="input-group">
                                    <label>¿Tratamiento de Efluentes?</label>
                                    <select name="tratamientoEfluentes" value={formData.tratamientoEfluentes} onChange={handleInputChange}>
                                        <option value="no">No</option>
                                        <option value="si">Sí</option>
                                    </select>
                                </div>
                            </div>
                        </fieldset>

                        {/* SECCIÓN ANTECEDENTES */}
                        <fieldset className="form-section">
                            <legend>Antecedentes de la Empresa</legend>
                            <div className="input-group">
                                <label>Situación de la Empresa</label>
                                <select name="tipoEmpresa" value={formData.tipoEmpresa} onChange={handleInputChange}>
                                    <option value="nueva">Empresa Nueva</option>
                                    <option value="existente">Empresa Existente</option>
                                </select>
                            </div>
                            {formData.tipoEmpresa === "existente" && (
                                <>
                                    <div className="input-group">
                                        <label>Emplazamiento actual</label>
                                        <select name="emplazamientoActual" value={formData.emplazamientoActual} onChange={handleInputChange}>
                                            <option value="propio">Propio</option>
                                            <option value="alquilado">Alquilado</option>
                                        </select>
                                    </div>
                                    <div className="input-group">
                                        <label>Tiempo de Radicación</label>
                                        <select name="tiempoRadicacion" value={formData.tiempoRadicacion} onChange={handleInputChange}>
                                            <option value="6 meses">6 Meses</option>
                                            <option value="12 meses">12 Meses</option>
                                            <option value="24 meses">24 Meses</option>
                                            <option value="más">36 meses o más</option>
                                        </select>
                                    </div>
                                    <div className="input-group full-width">
                                        <label>En caso de traslado, indicar pretensión:</label>
                                        <input type="text" name="pretensionTraslado" placeholder="Ej: Nuevos productos, incrementar produccion..." value={formData.pretensionTraslado} onChange={handleInputChange} />
                                    </div>
                                    <div className="input-group full-width">
                                        <label>Direccion actual:</label>
                                        <input type="text" name="direccion" placeholder="" value={formData.direccion} onChange={handleInputChange} />
                                    </div>
                                    <div className="input-group full-width">
                                        <label>Descripcion Servicio:</label>
                                        <textarea name="descripcionServicio" placeholder="" value={formData.descripcionServicio} onChange={handleInputChange} />
                                    </div>
                                </>
                            )}
                        </fieldset>

                        <div className="form-actions">
                            <button type="button" className="btn-draft" onClick={handleSaveDraft}>
                                <Save size={18} /> Guardar Borrador
                            </button>
                            <button type="submit" className="btn-submit-noRadicada">
                                <Send size={18} /> Enviar Proyecto Preliminar
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    );
}