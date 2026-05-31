import CargarPresupuesto from "./CargarPresupuesto";
import { toast } from 'sonner';
import React, { useState, useEffect } from "react";
import { RefreshCw, ArrowLeftRight, FileText, AlertCircle, ChevronDown, ChevronUp } from "lucide-react";
import { presupuestoApi } from "../../api/axios";
import LoadingSpinner from "../../ui/loading/LoadingSpinner";
import CrearPartida from "./CrearPartida";
import "./PresupuestoPanel.css";

interface BalancePartida {
    presupuestoId: number;
    codigo: string;
    nombre: string;
    nivel: 'PRINCIPAL' | 'PARCIAL' | 'SUBPARCIAL';
    fuenteFinanciamiento: string;
    creditoOriginal: number;
    creditoVigente: number;
    comprometido: number;
    devengado: number;
    pagado: number;
    saldoDisponible: number;
}
type OrigenMovimiento = 'ASIGNACION_INICIAL' | 'MODIFICACION' | 'EJECUCION';

interface MovimientoBase {
    fecha: string;
    origen: OrigenMovimiento;
}

interface MovimientoAsignacion extends MovimientoBase {
    origen: 'ASIGNACION_INICIAL';
    monto: number;
}

// Mapea a la tabla: modificaciones_presupuestarias
interface MovimientoModificacion extends MovimientoBase {
    origen: 'MODIFICACION';
    tipo: 'INCREMENTO' | 'DISMINUCION'; // ENUM estricto de tu DB
    justificacion: string;
    monto: number;
}

// Mapea a la tabla: registro_ejecucion_gasto
interface MovimientoEjecucion extends MovimientoBase {
    origen: 'EJECUCION';
    fase: 'COMPROMISO' | 'DEVENGADO' | 'PAGADO'; // ENUM estricto de tu DB
    comprobante_tipo: string;
    comprobante_nro: string;
    descripcion: string;
    monto: number;
}

// El historial es un array que puede contener cualquiera de estos tres tipos
type MovimientoHistorial = MovimientoAsignacion | MovimientoModificacion | MovimientoEjecucion;

export default function PresupuestoPanel() {
    const [balance, setBalance] = useState<BalancePartida[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [ejercicio, setEjercicio] = useState<number>(new Date().getFullYear());

    // NUEVOS ESTADOS: Control de fila expandida e historial analítico
    const [expandedId, setExpandedId] = useState<number | null>(null);
    const [historialMovimientos, setHistorialMovimientos] = useState<MovimientoHistorial[]>([]);
    const [loadingHistorial, setLoadingHistorial] = useState(false);

    // Estados para Modales de Acción Presupuestaria
    const [modalModificacion, setModalModificacion] = useState<{ abierto: boolean; partidaId: number; codigo: string; nombre: string } | null>(null);
    const [modalGasto, setModalGasto] = useState<{ abierto: boolean; partidaId: number; codigo: string; nombre: string } | null>(null);

    // Formulario de Modificaciones
    const [tipoMod, setTipoMod] = useState<'INCREMENTO' | 'DISMINUCION'>('INCREMENTO');
    const [montoMod, setMontoMod] = useState<string>("");
    const [justificacionMod, setJustificacionMod] = useState<string>("");

    // Formulario de Transacciones de Gasto
    const [faseGasto, setFaseGasto] = useState<'COMPROMISO' | 'DEVENGADO' | 'PAGADO'>('COMPROMISO');
    const [tipoComp, setTipoComp] = useState<string>("Orden de Compra");
    const [nroComp, setNroComp] = useState<string>("");
    const [montoGasto, setMontoGasto] = useState<string>("");
    const [descGasto, setDescGasto] = useState<string>("");

    //Modales auxiliares
    const [mostrarModalCarga, setMostrarModalCarga] = useState(false);
    const [mostrarModalCrearPartida, setMostrarModalCrearPartida] = useState(false);

    const cargarLibroBalances = async () => {
        setLoading(true);
        setError(null);
        setExpandedId(null); // Cerramos expansiones al refrescar
        try {
            const data = await presupuestoApi.getBalance(ejercicio);
            setBalance(data);
        } catch (err: any) {
            const msg = err.response?.data?.error || "Error al cargar el libro de balances.";
            setError(msg);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        cargarLibroBalances();
    }, [ejercicio]);

    // NUEVA FUNCIÓN: Al expandir la fila, se descarga el historial unificado del backend
    const handleToggleExpand = async (presupuestoId: number, creditoOriginal: number) => {
        if (expandedId === presupuestoId) {
            setExpandedId(null);
            setHistorialMovimientos([]);
            return;
        }

        setExpandedId(presupuestoId);
        setLoadingHistorial(true);
        try {
            // Llamada al endpoint unificado que debes crear en tu API (mezcla modificaciones y gastos)
            const data: MovimientoHistorial[] = await presupuestoApi.getHistorialPartida(presupuestoId);

            // Insertamos el asiento de apertura del crédito original al inicio del libro
            // CORRECCIÓN: Ajustado para coincidir con la interfaz MovimientoAsignacion
            const asientoApertura: MovimientoHistorial = {
                fecha: `${ejercicio}-01-01`,
                origen: 'ASIGNACION_INICIAL',
                monto: creditoOriginal
            };

            setHistorialMovimientos([asientoApertura, ...data]);
        } catch (err) {
            console.error("Error al obtener extracto contable:", err);
            setHistorialMovimientos([]);
        } finally {
            setLoadingHistorial(false);
        }
    };

    const handleReestructurar = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!modalModificacion || !montoMod || !justificacionMod) return;

        const montoNumerico = parseFloat(montoMod);

        // REGLA DE NEGOCIO 1: Art. 27 - Intangibilidad de la Partida de Personal
        // Suponiendo que las partidas de personal empiezan con "1." (ej: 1.0.0, 1.1.0)
        if (modalModificacion.codigo.startsWith("1.") && tipoMod === 'DISMINUCION') {
            toast.info("Bloqueo Legal: Por Art. 27 de la Ley 5763, los créditos de la partida de Personal no pueden transferirse a otros destinos.");
            return;
        }

        try {
            const res = await presupuestoApi.reestructurarPartida({
                presupuestoId: modalModificacion.partidaId,
                tipo: tipoMod, // Manda 'INCREMENTO' o 'DISMINUCION' directo a la DB
                monto: montoNumerico,
                justificacion: justificacionMod
            });
            toast.success(res.message);
            setModalModificacion(null);
            resetFormularios();
            cargarLibroBalances();
        } catch (err: any) {
            toast.error(err.response?.data?.error || "Error al procesar la modificación.");
        }
    };

    const handleRegistrarGasto = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!modalGasto || !montoGasto || !nroComp) return;

        const montoNumerico = parseFloat(montoGasto);

        // REGLA DE NEGOCIO 2: Control de Saldo Disponible
        // Buscamos el saldo actual de la partida en el estado global
        const partidaActual = balance.find(b => b.presupuestoId === modalGasto.partidaId);

        if (faseGasto === 'COMPROMISO' && partidaActual) {
            if (montoNumerico > partidaActual.saldoDisponible) {
                toast.warning(`Falta de Crédito: No puede comprometer ${formatMoneda(montoNumerico)}. El saldo disponible es de solo ${formatMoneda(partidaActual.saldoDisponible)}.`);
                return;
            }
        }

        try {
            const res = await presupuestoApi.registrarGasto({
                presupuestoId: modalGasto.partidaId,
                tipoComprobante: tipoComp,
                nroComprobante: nroComp,
                descripcion: descGasto,
                fase: faseGasto, // Manda 'COMPROMISO', 'DEVENGADO' o 'PAGADO' directo a la DB
                monto: montoNumerico
            });
            toast.success(res.message);
            setModalGasto(null);
            resetFormularios();
            cargarLibroBalances();
        } catch (err: any) {
            toast.error(err.response?.data?.error || "Error financiero al asentar la partida.");
        }
    };

    const resetFormularios = () => {
        setMontoMod(""); setJustificacionMod("");
        setMontoGasto(""); setNroComp(""); setDescGasto("");
    };

    const formatMoneda = (valor: number) => {
        return new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(valor);
    };

    // Función auxiliar para formatear la fecha
    const formatFecha = (fechaStr: string) => {
        return new Date(fechaStr).toLocaleDateString('es-AR');
    };

    // Renderizado del "Libro Mayor de Afectaciones" con cálculo de saldo acumulado (Running Balance)
    const renderLibroMayor = () => {
        if (loadingHistorial) return <LoadingSpinner text="Abriendo folios contables..." />;
        if (historialMovimientos.length === 0) return <p className="no-movements">Sin afectaciones registradas.</p>;

        let saldoAcumulado = 0;

        return (
            <div className="libro-mayor-container">
                <h4><FileText size={16} /> Extracto Analítico de la Partida</h4>
                <table className="libro-mayor-table">
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Operación</th>
                            <th>Documento Resp.</th>
                            <th>Concepto / Justificación</th>
                            <th className="txt-right">Crédito (+)</th>
                            <th className="txt-right">Débito (-)</th>
                            <th className="txt-right">Saldo Vigente Disp.</th>
                        </tr>
                    </thead>
                    <tbody>
                        {historialMovimientos.map((mov, mIdx) => {
                            let credito = 0;
                            let debito = 0;
                            let operacionLabel = "";
                            let comprobante = "";
                            let concepto = "";
                            const esAsientoInformativo = mov.origen === 'EJECUCION' && (mov.fase === 'DEVENGADO' || mov.fase === 'PAGADO');

                            // Type Guard: Evaluamos de qué tabla de la BDD viene este movimiento
                            if (mov.origen === 'ASIGNACION_INICIAL') {
                                operacionLabel = "ASIGNACIÓN INICIAL";
                                concepto = "Presupuesto Aprobado por Ley";
                                comprobante = "Ley de presupuesto";
                                credito = mov.monto;
                                saldoAcumulado += credito;
                            }
                            else if (mov.origen === 'MODIFICACION') {
                                operacionLabel = mov.tipo; // INCREMENTO o DISMINUCION
                                concepto = mov.justificacion;
                                comprobante = "Resolución / Decreto";
                                if (mov.tipo === 'INCREMENTO') {
                                    credito = mov.monto;
                                    saldoAcumulado += credito;
                                } else {
                                    debito = mov.monto;
                                    saldoAcumulado -= debito;
                                }
                            }
                            else if (mov.origen === 'EJECUCION') {
                                operacionLabel = mov.fase; // COMPROMISO, DEVENGADO o PAGADO
                                concepto = mov.descripcion;
                                comprobante = `${mov.comprobante_tipo} ${mov.comprobante_nro}`;
                                debito = mov.monto; // Los gastos van al débito

                                // Solo el COMPROMISO resta del Saldo Disponible en la contabilidad pública
                                if (mov.fase === 'COMPROMISO') {
                                    saldoAcumulado -= debito;
                                }
                            }

                            return (
                                <tr key={mIdx} className={`row-mov-${operacionLabel.toLowerCase()}`}>
                                    <td>{formatFecha(mov.fecha)}</td>
                                    <td>
                                        <span className={`badge-operacion ${operacionLabel.toLowerCase()}`}>
                                            {operacionLabel}
                                        </span>
                                    </td>
                                    <td>{comprobante}</td>
                                    <td className="cell-desc-mov">{concepto}</td>
                                    <td className="txt-right text-success">{credito > 0 ? formatMoneda(credito) : "-"}</td>
                                    <td className={`txt-right ${esAsientoInformativo ? "text-gray" : "text-danger"}`}>
                                        {debito > 0 ? formatMoneda(debito) : "-"}
                                    </td>
                                    <td className="txt-right math-saldo">
                                        <strong>{formatMoneda(saldoAcumulado)}</strong>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        );
    };

    return (
        <div className="presupuesto-panel">
            <div className="panel-header">
                <div className="header-title">
                    <h2>Libro de Estados y Balances de Gastos</h2>
                    <span className="subtitle-ley">Marco Regulatorio Modelado Conforme L.P N° 5763 del año 2015 </span>
                </div>
                <div className="header-actions">
                    <button className="select-year" onClick={() => setMostrarModalCrearPartida(true)}>
                        Crear registro de partida presupuestaria
                    </button>
                    <button className="select-year" onClick={() => setMostrarModalCarga(true)}>
                        Asignar monto inicial a partida presupuestaria
                    </button>
                    <select
                        className="select-year"
                        value={ejercicio}
                        onChange={(e) => setEjercicio(parseInt(e.target.value))}
                    >
                        {[-1, 0, 1].map((offset) => {
                            const year = new Date().getFullYear() + offset;
                            return (
                                <option key={year} value={year}>
                                    Ejercicio Fiscal {year}
                                </option>
                            );
                        })}
                    </select>
                    <button className="btn-refresh" onClick={cargarLibroBalances} title="Refrescar datos">
                        <RefreshCw size={18} className={loading ? "spin" : ""} />
                    </button>
                </div>
            </div>

            {error && (
                <div className="budget-error-banner">
                    <AlertCircle size={20} /> <span>{error}</span>
                </div>
            )}

            {mostrarModalCarga && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <CargarPresupuesto
                            ejercicio={ejercicio}
                            onCargaExitosa={() => {
                                setMostrarModalCarga(false);
                                cargarLibroBalances();
                            }}
                        />
                        <button className="btn-cancel" onClick={() => setMostrarModalCarga(false)}>Cerrar</button>
                    </div>
                </div>
            )}

            <div className="table-responsive">
                <table className="presupuesto-table">
                    <thead>
                        <tr>
                            <th></th>{/* Columna para el botón de expansión */}
                            <th>Partida</th>
                            <th>Denominación</th>
                            <th>Fuente (FF)</th>
                            <th>Crédito Orig.</th>
                            <th>Crédito Vig.</th>
                            <th>Comprometido</th>
                            <th>Devengado</th>
                            <th>Pagado</th>
                            <th>Saldo Disponible</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr>
                                <td colSpan={11} className="loading-row">
                                    <LoadingSpinner text="Consolidando partidas analíticas..." />
                                </td>
                            </tr>
                        ) : (
                            balance.map((item, idx) => {
                                const isRowExpanded = expandedId === item.presupuestoId;
                                return (
                                    <React.Fragment key={item.presupuestoId}>
                                        {/* Fila Principal de la Partida */}
                                        <tr key={`main-${idx}`} className={`${item.codigo.endsWith(".0.0") ? "row-partida-principal" : ""} ${isRowExpanded ? "row-selected-parent" : ""}`}>
                                            <td>
                                                <button
                                                    className="btn-toggle-row"
                                                    onClick={() => handleToggleExpand(item.presupuestoId, item.creditoOriginal)}
                                                    title="Ver extracto contable mayor"
                                                >
                                                    {isRowExpanded ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                                                </button>
                                            </td>
                                            <td><strong>{item.codigo}</strong></td>
                                            <td className="cell-nombre">{item.nombre}</td>
                                            <td><span className="badge-ff">{item.fuenteFinanciamiento}</span></td>
                                            <td>{formatMoneda(item.creditoOriginal)}</td>
                                            <td>{formatMoneda(item.creditoVigente)}</td>
                                            <td className="txt-comprometido">{formatMoneda(item.comprometido)}</td>
                                            <td>{formatMoneda(item.devengado)}</td>
                                            <td>{formatMoneda(item.pagado)}</td>
                                            <td className={`cell-disponible ${item.saldoDisponible < 0 ? "negative" : ""}`}>
                                                <strong>{formatMoneda(item.saldoDisponible)}</strong>
                                            </td>
                                            <td className="cell-actions">
                                                <button
                                                    className="btn-action-budget mod"
                                                    title="Reestructurar Crédito (Incremento/Disminución)"
                                                    onClick={() => setModalModificacion({ abierto: true, partidaId: item.presupuestoId, codigo: item.codigo, nombre: item.nombre })}
                                                >
                                                    <ArrowLeftRight size={14} />
                                                </button>
                                                <button
                                                    className="btn-action-budget gasto"
                                                    title="Asentar Etapa del Gasto (Afectación Diaria)"
                                                    onClick={() => setModalGasto({ abierto: true, partidaId: item.presupuestoId, codigo: item.codigo, nombre: item.nombre })}
                                                >
                                                    <FileText size={14} />
                                                </button>
                                            </td>
                                        </tr>

                                        {/* FILA ANIDADA: Se despliega si la fila actual está expandida */}
                                        {isRowExpanded && (
                                            <tr key={`sub-${idx}`} className="row-expanded-ledger">
                                                <td colSpan={11}>
                                                    {renderLibroMayor()}
                                                </td>
                                            </tr>
                                        )}
                                    </React.Fragment>
                                );
                            })
                        )}
                        {!loading && balance.length === 0 && (
                            <tr>
                                <td colSpan={11} className="empty-row">No se encontraron registros de créditos cargados para este ejercicio.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Modal 1: Modificaciones Presupuestarias */}
            {modalModificacion && (
                <div className="modal-overlay">
                    <div className="modal-content budget-modal">
                        <h3> Reestructuración Analítica de Crédito</h3>
                        <p className="modal-partida-info">Partida seleccionada: <strong>{modalModificacion.codigo} - {modalModificacion.nombre}</strong></p>

                        <form onSubmit={handleReestructurar}>
                            <div className="modal-form-group">
                                <label>Tipo de Operación:</label>
                                <select value={tipoMod} onChange={(e) => setTipoMod(e.target.value as any)}>
                                    <option value="INCREMENTO">Incremento de Crédito (+)</option>
                                    <option value="DISMINUCION">Compensación / Disminución de Crédito (-)</option>
                                </select>
                            </div>
                            <div className="modal-form-group">
                                <label>Monto de la Operación ($):</label>
                                <input type="number" step="0.01" required value={montoMod} onChange={(e) => setMontoMod(e.target.value)} placeholder="0.00" />
                            </div>
                            <div className="modal-form-group">
                                <label>Justificación Legal (Resolución / Decreto):</label>
                                <textarea rows={3} required value={justificacionMod} onChange={(e) => setJustificacionMod(e.target.value)} placeholder="Ej: Resol. Interna de Directorio ENREPAVI Nro... " />
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="btn-cancel" onClick={() => setModalModificacion(null)}>Cancelar</button>
                                <button type="submit" className="btn-confirm-budget">Procesar Movimiento</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {/* Modal 2: Registro de Fases del Gasto */}
            {modalGasto && (
                <div className="modal-overlay">
                    <div className="modal-content budget-modal">
                        <h3><FileText size={20} /> Registrar Etapa de Ejecución del Gasto</h3>
                        <p className="modal-partida-info">Partida: <strong>{modalGasto.codigo} - {modalGasto.nombre}</strong></p>

                        <form onSubmit={handleRegistrarGasto}>
                            <div className="modal-form-row">
                                <div className="modal-form-group">
                                    <label>Fase Contable:</label>
                                    <select value={faseGasto} onChange={(e) => setFaseGasto(e.target.value as any)}>
                                        <option value="COMPROMISO">1. Compromiso Presupuestario</option>
                                        <option value="DEVENGADO">2. Gasto Devengado</option>
                                        <option value="PAGADO">3. Orden de Pago Emitida</option>
                                    </select>
                                </div>
                                <div className="modal-form-group">
                                    <label>Monto Impuesto ($):</label>
                                    <input type="number" step="0.01" required value={montoGasto} onChange={(e) => setMontoGasto(e.target.value)} placeholder="0.00" />
                                </div>
                            </div>
                            <div className="modal-form-row">
                                <div className="modal-form-group">
                                    <label>Tipo Comprobante:</label>
                                    <input type="text" required value={tipoComp} onChange={(e) => setTipoComp(e.target.value)} placeholder="Ej: Factura B, Orden de Compra" />
                                </div>
                                <div className="modal-form-group">
                                    <label>Número Identificador:</label>
                                    <input type="text" required value={nroComp} onChange={(e) => setNroComp(e.target.value)} placeholder="0001-00004521" />
                                </div>
                            </div>
                            <div className="modal-form-group">
                                <label>Descripción del Objeto del Gasto:</label>
                                <textarea rows={2} value={descGasto} onChange={(e) => setDescGasto(e.target.value)} placeholder="Detalle sintético de la contratación o adquisición..." />
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="btn-cancel" onClick={() => setModalGasto(null)}>Cancelar</button>
                                <button type="submit" className="btn-confirm-budget step">Asentar Registro</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {mostrarModalCrearPartida && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <CrearPartida
                            onGuardadoExitoso={() => {
                                setMostrarModalCrearPartida(false);
                                cargarLibroBalances();
                            }}
                        />
                        <button className="btn-cancel" onClick={() => setMostrarModalCrearPartida(false)}>Cerrar</button>
                    </div>
                </div>
            )}
        </div>
    );
}