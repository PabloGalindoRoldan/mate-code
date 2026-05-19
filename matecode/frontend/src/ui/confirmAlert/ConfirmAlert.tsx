import "./ConfirmAlert.css";

interface ConfirmAlertProps {
    isOpen: boolean;
    title?: string;
    message: string;
    onConfirm: () => void;
    onCancel: () => void;
    confirmText?: string;
    cancelText?: string;
    type?: "danger" | "info";
}

export default function ConfirmAlert({
    isOpen,
    title = "Confirmar acción",
    message,
    onConfirm,
    onCancel,
    confirmText = "Aceptar",
    cancelText = "Cancelar",
    type = "info"
}: ConfirmAlertProps) {
    if (!isOpen) return null;

    return (
        <div className="confirm-alert-overlay" onClick={onCancel}>
            <div
                className={`confirm-alert-box ${type}`}
                onClick={(e) => e.stopPropagation()}
            >
                <div className="confirm-alert-header">
                    <h3>{title}</h3>
                </div>
                <div className="confirm-alert-body">
                    <p>{message}</p>
                </div>
                <div className="confirm-alert-actions">
                    <button className="btn-cancel" onClick={onCancel}>
                        {cancelText}
                    </button>
                    <button className={`btn-confirm ${type}`} onClick={onConfirm}>
                        {confirmText}
                    </button>
                </div>
            </div>
        </div>
    );
}