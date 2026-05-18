import "./Alert.css";

export default function Alert({ message, type }: { message: string; type: "success" | "error" }) {

    return (
        <div className={`alert alert-${type}`} role="alert">
            {message}
        </div>
    );
}