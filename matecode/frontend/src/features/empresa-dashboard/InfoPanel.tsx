import "./InfoPanel.css";
export default function InfoPanel(params: { empresaInfo: any }) {
    const { empresaInfo } = params;
    const empresa = empresaInfo.empresas[0];

    return (
        <div className="infoPanel">
            <h2>Información de la Empresa</h2>
            <p>Nombre: {empresa.nombre}</p>
            <p>Direccion: {empresa.direccion}</p>
            <p>Teléfono: {empresa.telefono}</p>
            <p>Email: {empresa.email}</p>
        </div>
    );
}