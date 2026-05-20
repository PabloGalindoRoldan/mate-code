import { useState, useEffect } from "react";
import { RefreshCw, ArrowLeftRight, FileText, AlertCircle } from "lucide-react";
import { presupuestoApi } from "../../api/axios";
import LoadingSpinner from "../../ui/loading/LoadingSpinner";
import "./PresupuestoPanel.css";

interface BalancePartida {
    presupuestoId: number;
    codigo: string;
    nombre: string;
    fuenteFinanciamiento: string;
    creditoOriginal: number;
    creditoVigente: number;
    comprometido: number;
    devengado: number;
    pagado: number;
    saldoDisponible: number;
}

export default function PresupuestoPanel() {
    const [balance, setBalance] = useState<BalancePartida[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [ejercicio, setEjercicio] = useState<number>(2025);

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

    const cargarLibroBalances = async () => {
        setLoading(true);
        setError(null);
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

    const handleReestructurar = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!modalModificacion || !montoMod || !justificacionMod) return;

        try {
            const res = await presupuestoApi.reestructurarPartida({
                presupuestoId: modalModificacion.partidaId,
                tipo: tipoMod,
                monto: parseFloat(montoMod),
                justificacion: justificacionMod
            });
            alert(res.message);
            setModalModificacion(null);
            resetFormularios();
            cargarLibroBalances();
        } catch (err: any) {
            // Captura los desvíos del Art 27 o falta de saldo del Backend y los muestra de forma estilizada
            alert(err.response?.data?.error || "Error al procesar la modificación presupuestaria.");
        }
    };

    const handleRegistrarGasto = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!modalGasto || !montoGasto || !nroComp) return;

        try {
            const res = await presupuestoApi.registrarGasto({
                presupuestoId: modalGasto.partidaId,
                tipoComprobante: tipoComp,
                nroComprobante: nroComp,
                descripcion: descGasto,
                fase: faseGasto,
                monto: parseFloat(montoGasto)
            });
            alert(res.message);
            setModalGasto(null);
            resetFormularios();
            cargarLibroBalances();
        } catch (err: any) {
            alert(err.response?.data?.error || "Error financiero al asentar la partida.");
        }
    };

    const resetFormularios = () => {
        setMontoMod(""); setJustificacionMod("");
        setMontoGasto(""); setNroComp(""); setDescGasto("");
    };

    // Formateador auxiliar de Moneda local
    const formatMoneda = (valor: number) => {
        return new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(valor);
    };

    return (
        <div className="presupuesto-panel">
            <div className="panel-header">
                <div className="header-title">
                    <h2>Libro de Estados y Balances de Gastos (ENREPAVI)</h2>
                    <span className="subtitle-ley">Marco Regulatorio Conforme Ley N° 5763 de la Provincia de Río Negro</span>
                </div>
                <div className="header-actions">
                    <select className="select-year" value={ejercicio} onChange={(e) => setEjercicio(parseInt(e.target.value))}>
                        <option value={2025}>Ejercicio Fiscal 2025</option>
                        <option value={2026}>Ejercicio Fiscal 2026</option>
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

            <div className="table-responsive">
                <table className="presupuesto-table">
                    <thead>
                        <tr>
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
                                <td colSpan={10} className="loading-row">
                                    <LoadingSpinner text="Consolidando partidas analíticas..." />
                                </td>
                            </tr>
                        ) : (
                            <>
                                {balance.map((item, idx) => (
                                    <tr key={idx} className={item.codigo.endsWith(".0.0") ? "row-partida-principal" : ""}>
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
                                ))}
                                {!loading && balance.length === 0 && (
                                    <tr>
                                        <td colSpan={10} className="empty-row">No se encontraron registros de créditos cargados para este ejercicio.</td>
                                    </tr>
                                )}
                            </>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Modal 1: Modificaciones Presupuestarias */}
            {modalModificacion && (
                <div className="modal-overlay">
                    <div className="modal-content budget-modal">
                        <h3><ArrowLeftRight size={20} /> Reestructuración Analítica de Crédito</h3>
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
        </div>
    );
}