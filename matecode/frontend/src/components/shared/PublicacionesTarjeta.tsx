import './PublicacionesTarjeta.css';

export default function PublicacionesTarjeta(params: any) {
    return (
        <div className="publicacionTarjeta">
            <img src={params.imagen} alt={params.alt} />
            <h2>{params.titulo}</h2>
            <p>{params.contenido}</p>
        </div>
    )
}