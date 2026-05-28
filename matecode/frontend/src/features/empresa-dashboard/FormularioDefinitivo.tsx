import "./EmpresaNoRadicadaView.css";
import { useState, useEffect } from "react";
import { mensajeriaApi, proyectosApi } from "../../api/axios";
import { useAuth } from "../../context/AuthContext";
import { Save, Send, FileText } from "lucide-react";


interface Props {
    isRectifying: boolean;
    onProyectoEnviado: () => void;
    proyectoExistente?: any;
}

export default function FormularioDefinitivo({ isRectifying, onProyectoEnviado, proyectoExistente }: Props) {

    const { user } = useAuth();
    const [enviado, setEnviado] = useState(false);
    const [rectify] = useState(isRectifying)

    const [formData, setFormData] = useState({
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
        tienePlanos: "si",
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
        emplazamientoActual: "Propio",
        tiempoRadicacion: "6 meses",
        balanzaPublica: "no",
        comedor: "no",
        sumCoworking: "no",
        linkViabilidadFinanciera: "",
        linkEstudioMercado: "",
        linkImpactoAmbiental: "",
        linkHabilitacionMunicipal: "",
        linkCertificadoInhibiciones: "",
    });

    useEffect(() => {

        if (isRectifying && proyectoExistente) {

            setFormData({
                ...proyectoExistente,

                superficieRequerida:
                    proyectoExistente.superficieRequerida?.parsedValue || 0,

                superficieTrabajo:
                    proyectoExistente.superficieTrabajo?.parsedValue || 0,

                superficieDeposito:
                    proyectoExistente.superficieDeposito?.parsedValue || 0,

                superficieCubierta:
                    proyectoExistente.superficieCubierta?.parsedValue || 0,

                superficieEstacionamiento:
                    proyectoExistente.superficieEstacionamiento?.parsedValue || 0,

                potenciaInstalada:
                    proyectoExistente.potenciaInstalada?.parsedValue || 0,

                aguaMensual:
                    proyectoExistente.aguaMensual?.parsedValue || 0,

                gasMensual:
                    proyectoExistente.gasMensual?.parsedValue || 0,

                residuosCantidad:
                    proyectoExistente.residuosCantidad?.parsedValue || 0,

                linkViabilidadFinanciera:
                    proyectoExistente.linkViabilidadFinanciera || "",

                linkEstudioMercado:
                    proyectoExistente.linkEstudioMercado || "",

                linkImpactoAmbiental:
                    proyectoExistente.linkImpactoAmbiental || "",

                linkHabilitacionMunicipal:
                    proyectoExistente.linkHabilitacionMunicipal || "",

                linkCertificadoInhibiciones:
                    proyectoExistente.linkCertificadoInhibiciones || "",
            });

            return;
        }

        if (!user?.nombreUsuario) return;

        const draft = localStorage.getItem(
            "draft_proyecto_definitivo_" + user.nombreUsuario
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
            [name]: type === 'number' ? parseFloat(value) || null : value
        }));
    };

    const handleSaveDraft = () => {
        localStorage.setItem("draft_proyecto_definitivo_" + user?.nombreUsuario, JSON.stringify(formData));
        alert("Borrador guardado localmente.");
    };

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();

        // 1. Validación de campos críticos
        const obligatorios = ['nombre', 'actividadPrincipal', 'personaReferente', 'superficieRequerida', 'personalAOcupar', 'potenciaInstalada'];
        const faltantes = obligatorios.filter(f => !formData[f as keyof typeof formData]);

        if (faltantes.length > 0) {
            alert("Debe completar los campos críticos: " + faltantes.join(", "));
            return;
        }

        // Validación extra de seguridad para el CUIT
        const cuit = user?.empresa?.cuit;
        if (!cuit) {
            alert("Error: No se pudo identificar el CUIT de su empresa. Por favor, contacte al administrador.");
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

                await proyectosApi.actualizarDefinitivo(payload);

            } else {

                const payload = {
                    ...formData,
                    usuarioNombre: user?.nombreUsuario || "Anonimo",
                    cuitEmpresa: user?.empresa?.cuit
                };

                await proyectosApi.crearProyectoDefinitivo(payload);
            }

            // 4. Notificación
            await mensajeriaApi.enviarMensaje(
                user?.nombreUsuario || "",
                `Sistema: Proyecto definitivo "${formData.nombre}" enviado exitosamente para evaluación.`
            );

            setEnviado(true);
            alert("¡Proyecto enviado correctamente!");
            console.log(enviado)

            // 5. Limpiar borrador
            localStorage.removeItem("draft_proyecto_" + user?.nombreUsuario);
            await onProyectoEnviado();

        } catch (error) {
            console.error("Error al enviar proyecto:", error);
            alert("Hubo un error al procesar el proyecto. Por favor, intente nuevamente.");
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
                            <h1>Presentar Proyecto Definitivo</h1>
                            <p>Complete los datos técnicos solicitados para presentar el proyecto que será enviado a consideracion del directorio.</p>
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

                        {/* DOCUMENTACION */}
                        <fieldset className="form-section">
                            <legend>
                                Links a Documentación Obligatoria
                            </legend>

                            <div className="input-group full-width">
                                <label>Viabilidad Financiera</label>
                                <input
                                    type="url"
                                    name="linkViabilidadFinanciera"
                                    placeholder="https://drive.google.com/..."
                                    value={formData.linkViabilidadFinanciera}
                                    onChange={handleInputChange}
                                />
                            </div>

                            <div className="input-group full-width">
                                <label>Estudio de Mercado</label>
                                <input
                                    type="url"
                                    name="linkEstudioMercado"
                                    placeholder="https://drive.google.com/..."
                                    value={formData.linkEstudioMercado}
                                    onChange={handleInputChange}
                                />
                            </div>

                            <div className="input-group full-width">
                                <label>Impacto Ambiental</label>
                                <input
                                    type="url"
                                    name="linkImpactoAmbiental"
                                    placeholder="https://drive.google.com/..."
                                    value={formData.linkImpactoAmbiental}
                                    onChange={handleInputChange}
                                />
                            </div>

                            <div className="input-group full-width">
                                <label>Habilitación Municipal</label>
                                <input
                                    type="url"
                                    name="linkHabilitacionMunicipal"
                                    placeholder="https://drive.google.com/..."
                                    value={formData.linkHabilitacionMunicipal}
                                    onChange={handleInputChange}
                                />
                            </div>

                            <div className="input-group full-width">
                                <label>Certificado de Inhibiciones</label>
                                <input
                                    type="url"
                                    name="linkCertificadoInhibiciones"
                                    placeholder="https://drive.google.com/..."
                                    value={formData.linkCertificadoInhibiciones}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="input-group full-width">
                                <label>Planos de efificacion</label>
                                <input
                                    type="url"
                                    name="linkPlanos"
                                    placeholder="https://drive.google.com/..."
                                    value={formData.linkPlanos}
                                    onChange={handleInputChange}
                                />
                            </div>
                        </fieldset>

                        <div className="form-actions">
                            <button type="button" className="btn-draft" onClick={handleSaveDraft}>
                                <Save size={18} /> Guardar Borrador
                            </button>
                            <button type="submit" className="btn-submit-noRadicada">
                                <Send size={18} /> Enviar Proyecto Definitivo
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    );
}